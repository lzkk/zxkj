package com.zxkj.task.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DynamicTaskCreate {

    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;
    @Autowired
    private MyDistributeOnceElasticJobListener myDistributeOnceElasticJobListener;

    /***
     * 作业创建
     * @param jobName:作业名字
     * @param cron:表达式
     * @param shardingTotalCount:分片数量
     * @param instance:作业实例
     * @param parameters:额外参数
     * @param shardingItemParameters:分配配置参数
     */
    public void create(String jobName, String cron, int shardingTotalCount, SimpleJob instance, String parameters, String shardingItemParameters) {
        if (shardingTotalCount > 0 && shardingItemParameters == null) {
            shardingItemParameters = "0=0";
            for (int k = 1; k < shardingTotalCount; k++) {
                shardingItemParameters += "," + k + "=" + k;
            }
        }

        //1.配置作业->Builder->构建：LiteJobConfiguration
        LiteJobConfiguration.Builder builder = LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
                JobCoreConfiguration.newBuilder(
                        jobName,
                        cron,
                        shardingTotalCount
                ).jobParameter(parameters).shardingItemParameters(shardingItemParameters).build(),
                instance.getClass().getName()
        )).overwrite(true);
        LiteJobConfiguration liteJobConfiguration = builder.build();

        //2.开启作业
        new SpringJobScheduler(instance, zookeeperRegistryCenter, liteJobConfiguration, myDistributeOnceElasticJobListener).init();
    }

    /***
     * 作业创建
     * @param jobName:作业名字
     * @param cron:表达式
     * @param shardingTotalCount:分片数量
     * @param instance:作业实例
     * @param parameters:额外参数
     */
    public void create(String jobName, String cron, int shardingTotalCount, SimpleJob instance, String parameters) {
        create(jobName, cron, shardingTotalCount, instance, parameters, null);
    }
}
