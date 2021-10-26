package com.zxkj.task.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class MyJob2 implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("test::::" +   Thread.currentThread().getName());
    }
}

