package com.zxkj.task.config;

import com.dangdang.ddframe.job.api.JobType;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.zxkj.common.util.json.JsonUtils;
import com.zxkj.task.bean.Job;
import com.zxkj.task.support.ElasticJobProperties;
import com.zxkj.task.support.jobinit.DynamicJobInitialization;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author ：yuhui
 * @date ：Created in 2021/7/15 14:27
 */
@Component
@Slf4j
public class ElasticJobService {

    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;
    @Autowired
    private DynamicJobInitialization dynamicJobInitialization;
    @Autowired
    private ApplicationContext ctx;

    /**
     * 保存/更新任务(SIMPLE)
     *
     * @param job
     */
    public void processSimpleJob(Job job) {
        processJob(job, JobType.SIMPLE);
    }

    /**
     * 保存/更新任务(DATAFLOW)
     *
     * @param job
     */
    public void processDataFlowJob(Job job) {
        processJob(job, JobType.DATAFLOW);
    }

    /**
     * 保存/更新任务
     *
     * @param job
     * @param jobType
     */
    private void processJob(Job job, JobType jobType) {
        ElasticJobProperties.JobConfiguration configuration = new ElasticJobProperties.JobConfiguration();
        configuration.setJobClass(job.getJobClass());
        configuration.setCron(job.getCron());
        configuration.setJobParameter(job.getJobParameter());
        configuration.setShardingTotalCount(job.getShardingTotalCount());
        configuration.setShardingItemParameters(job.getShardingItemParameters());
        ElasticJobProperties.JobConfiguration.Listener listener = dynamicJobInitialization.getDistributedListener();
        if (listener != null) {
            configuration.setListener(listener);
        }
        dynamicJobInitialization.initJob(job.getJobName(), jobType, configuration);
    }

    /**
     * 移除任务
     *
     * @param jobName
     * @throws Exception
     */
    public void removeJob(String jobName) {
        dynamicJobInitialization.removeJob(jobName);
    }

    @PostConstruct
    public void monitorJobRegister() {
        CuratorFramework client = zookeeperRegistryCenter.getClient();
        PathChildrenCache childrenCache = new PathChildrenCache(client, "/", true);
        PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED:
                        String config = new String(client.getData().forPath(data.getPath() + "/config"));
                        Job job = JsonUtils.toBean(Job.class, config);
                        Object bean = null;
                        // 获取bean失败则添加任务
                        try {
                            bean = ctx.getBean(job.getJobName());
                        } catch (BeansException e) {
                            log.error("ERROR NO BEAN,CREATE BEAN ->" + job.getJobName(), e);
                        }
                        if (Objects.isNull(bean)) {
                            processSimpleJob(job);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        childrenCache.getListenable().addListener(childrenCacheListener);
        try {
            childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
