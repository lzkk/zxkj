package com.zxkj.task.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyJob2 implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        String jobParameter = shardingContext.getJobParameter();
        String[] aa = jobParameter.split("-");
        int begin = Integer.parseInt(aa[0]);
        int end = Integer.parseInt(aa[1]);
        int shardingTotalCount = shardingContext.getShardingTotalCount();
        int shardingParameter = Integer.parseInt(shardingContext.getShardingParameter());
        for (int k = begin; k <= end; k++) {
            if (k % shardingTotalCount == shardingParameter) {
                log.info("shardingItem:" + shardingContext.getShardingItem() + "," + k);
            }
        }
    }
}

