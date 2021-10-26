package com.zxkj.task.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyJob3 implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        int shardingTotalCount = shardingContext.getShardingTotalCount();
        int shardingItem = shardingContext.getShardingItem();
        String jobParameter = shardingContext.getJobParameter();
        log.info("分片总数:{},当前分片:{},参数:{}", shardingTotalCount, shardingItem, jobParameter);
        if (shardingItem == 0) {
            //处理北京、上海

        } else if (shardingItem == 1) {
            //处理重庆
        }

    }
}

