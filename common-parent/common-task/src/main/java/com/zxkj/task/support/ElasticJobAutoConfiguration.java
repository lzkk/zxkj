
package com.zxkj.task.support;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.dangdang.elasticjob.lite.annotation.ElasticSimpleJob;
import com.zxkj.task.support.jobinit.DataflowJobInitialization;
import com.zxkj.task.support.jobinit.DynamicJobInitialization;
import com.zxkj.task.support.jobinit.ScriptJobInitialization;
import com.zxkj.task.support.jobinit.SimpleJobInitialization;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({ElasticJobProperties.class})
public class ElasticJobAutoConfiguration {

    @Autowired
    private ElasticJobProperties elasticJobProperties;
    @Autowired
    private ApplicationContext applicationContext;

    public ElasticJobAutoConfiguration() {
    }

    @Bean(name = {"elasticJobRegistryCenter"}, initMethod = "init")
    @ConditionalOnMissingBean
    public ZookeeperRegistryCenter elasticJobRegistryCenter() {
        ElasticJobProperties.ZkConfiguration regCenterProperties = this.elasticJobProperties.getZookeeper();
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(regCenterProperties.getServerLists(), regCenterProperties.getNamespace());
        zookeeperConfiguration.setBaseSleepTimeMilliseconds(regCenterProperties.getBaseSleepTimeMilliseconds());
        zookeeperConfiguration.setConnectionTimeoutMilliseconds(regCenterProperties.getConnectionTimeoutMilliseconds());
        zookeeperConfiguration.setMaxSleepTimeMilliseconds(regCenterProperties.getMaxSleepTimeMilliseconds());
        zookeeperConfiguration.setSessionTimeoutMilliseconds(regCenterProperties.getSessionTimeoutMilliseconds());
        zookeeperConfiguration.setMaxRetries(regCenterProperties.getMaxRetries());
        zookeeperConfiguration.setDigest(regCenterProperties.getDigest());
        ZookeeperRegistryCenter zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
        return zookeeperRegistryCenter;
    }

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    @ConditionalOnBean({ZookeeperRegistryCenter.class})
    public SimpleJobInitialization simpleJobInitialization() {
        return new SimpleJobInitialization(this.elasticJobProperties.getSimples());
    }

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    @ConditionalOnBean({ZookeeperRegistryCenter.class})
    public DataflowJobInitialization dataflowJobInitialization() {
        return new DataflowJobInitialization(this.elasticJobProperties.getDataflows());
    }

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    @ConditionalOnBean({ZookeeperRegistryCenter.class})
    public ScriptJobInitialization scriptJobInitialization() {
        return new ScriptJobInitialization(this.elasticJobProperties.getScripts());
    }

    /**
     * 动态任务初始化
     *
     * @return
     */
    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    public DynamicJobInitialization dynamicJobInitialization() {
        return new DynamicJobInitialization(elasticJobRegistryCenter());
    }

    /**
     * 静态任务集成
     */
    @PostConstruct
    public void initElasticJob() {
        Map<String, SimpleJob> map = this.applicationContext.getBeansOfType(SimpleJob.class);
        Iterator var3 = map.entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry<String, SimpleJob> entry = (Map.Entry) var3.next();
            SimpleJob simpleJob = (SimpleJob) entry.getValue();
            ElasticSimpleJob elasticSimpleJobAnnotation = (ElasticSimpleJob) simpleJob.getClass().getAnnotation(ElasticSimpleJob.class);
            if (elasticSimpleJobAnnotation != null) {
                String cron = (String) StringUtils.defaultIfBlank(elasticSimpleJobAnnotation.cron(), elasticSimpleJobAnnotation.value());
                SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(simpleJob.getClass().getName(), cron, elasticSimpleJobAnnotation.shardingTotalCount()).shardingItemParameters(elasticSimpleJobAnnotation.shardingItemParameters()).build(), simpleJob.getClass().getCanonicalName());
                LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build();
                String dataSourceRef = elasticSimpleJobAnnotation.dataSource();
                if (StringUtils.isNotBlank(dataSourceRef)) {
                    if (!this.applicationContext.containsBean(dataSourceRef)) {
                        throw new RuntimeException("not exist datasource [" + dataSourceRef + "] !");
                    }

                    DataSource dataSource = (DataSource) this.applicationContext.getBean(dataSourceRef);
                    JobEventRdbConfiguration jobEventRdbConfiguration = new JobEventRdbConfiguration(dataSource);
                    SpringJobScheduler jobScheduler = new SpringJobScheduler(simpleJob, elasticJobRegistryCenter(), liteJobConfiguration, jobEventRdbConfiguration, new ElasticJobListener[0]);
                    jobScheduler.init();
                } else {
                    SpringJobScheduler jobScheduler = new SpringJobScheduler(simpleJob, elasticJobRegistryCenter(), liteJobConfiguration, new ElasticJobListener[0]);
                    jobScheduler.init();
                }
            }
        }

    }
}
