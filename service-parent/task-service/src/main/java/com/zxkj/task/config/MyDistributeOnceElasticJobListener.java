package com.zxkj.task.config;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.AbstractDistributeOnceElasticJobListener;
import org.springframework.stereotype.Component;

@Component
public class MyDistributeOnceElasticJobListener extends AbstractDistributeOnceElasticJobListener {


    public MyDistributeOnceElasticJobListener() {
        super(0l, 0l);
    }


    /**
     * 设置间隔时间
     *
     * @param startedTimeoutMilliseconds
     */
    public MyDistributeOnceElasticJobListener(long startedTimeoutMilliseconds) {
        super(startedTimeoutMilliseconds, 0l);
    }

    /**
     * 设置间隔时间
     *
     * @param startedTimeoutMilliseconds
     * @param completedTimeoutMilliseconds
     */
    public MyDistributeOnceElasticJobListener(long startedTimeoutMilliseconds, long completedTimeoutMilliseconds) {
        super(startedTimeoutMilliseconds, completedTimeoutMilliseconds);
    }

    /**
     * 任务开始
     *
     * @param shardingContexts
     */
    @Override
    public void doBeforeJobExecutedAtLastStarted(ShardingContexts shardingContexts) {
        System.out.println("任务开始");
    }

    /**
     * 任务结束
     *
     * @param shardingContexts
     */
    @Override
    public void doAfterJobExecutedAtLastCompleted(ShardingContexts shardingContexts) {
        System.err.println("任务结束");
    }
}
