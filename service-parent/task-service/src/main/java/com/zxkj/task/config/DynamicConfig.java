package com.zxkj.task.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("'${elaticjob.zookeeper.server-lists}'.length() >0")
public class DynamicConfig {

    @Value("${elaticjob.zookeeper.server-lists}")
    private String serverLists;
    @Value("${elaticjob.zookeeper.namespace}")
    private String namespace;

    /****
     * 指定当前注册地址信息
     */
    @Bean
    public ZookeeperConfiguration zookeeperConfiguration() {
        return new ZookeeperConfiguration(serverLists, namespace);
    }

    /****
     * 向Zookeeper服务注册
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(ZookeeperConfiguration zookeeperConfiguration) {
        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }

    @Bean
    public MyDistributeOnceElasticJobListener elasticJobListener() {
        return new MyDistributeOnceElasticJobListener(100l, 100l);
    }
}
