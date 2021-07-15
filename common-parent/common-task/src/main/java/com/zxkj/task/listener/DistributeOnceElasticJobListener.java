package com.zxkj.task.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.AbstractDistributeOnceElasticJobListener;

public class DistributeOnceElasticJobListener extends AbstractDistributeOnceElasticJobListener {


    public DistributeOnceElasticJobListener() {
        super(5000l, 5000l);
    }


    /**
     * 设置间隔时间
     *
     * @param startedTimeoutMilliseconds
     */
    public DistributeOnceElasticJobListener(long startedTimeoutMilliseconds) {
        super(startedTimeoutMilliseconds, 5000l);
    }

    /**
     * 设置间隔时间
     *
     * @param startedTimeoutMilliseconds
     * @param completedTimeoutMilliseconds
     */
    public DistributeOnceElasticJobListener(long startedTimeoutMilliseconds, long completedTimeoutMilliseconds) {
        super(startedTimeoutMilliseconds, completedTimeoutMilliseconds);
    }

    /**
     * 任务开始
     *
     * @param shardingContexts
     */
    @Override
    public void doBeforeJobExecutedAtLastStarted(ShardingContexts shardingContexts) {
        System.out.println("任务开始," + Thread.currentThread().getName());
    }

    /**
     * 任务结束
     *
     * @param shardingContexts
     */
    @Override
    public void doAfterJobExecutedAtLastCompleted(ShardingContexts shardingContexts) {
        System.out.println("任务结束," + Thread.currentThread().getName());
    }
}
