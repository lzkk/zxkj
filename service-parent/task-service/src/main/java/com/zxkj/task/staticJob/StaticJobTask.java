package com.zxkj.task.staticJob;

import com.zxkj.common.cache.redis.RedisUtil;
import com.zxkj.task.config.DynamicTaskCreate;
import com.zxkj.task.job.MyJob3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Slf4j
@RefreshScope
@Component
public class StaticJobTask implements InitializingBean {

    @Value("${elaticjob.shardingTotalCount}")
    private int shardingTotalCount;

    @Autowired
    private DynamicTaskCreate dynamicTaskCreate;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
//            dynamicTaskCreate.create("myTestJob", "0/10 * * * * ? *", shardingTotalCount, new MyJob2(), "1-5");

            dynamicTaskCreate.create("myTestJob3", "0/5 * * * * ? *", shardingTotalCount, new MyJob3(redisUtil), "11-15");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

