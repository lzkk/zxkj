package com.zxkj.task.staticJob;

import com.zxkj.task.config.DynamicTaskCreate;
import com.zxkj.task.job.MyJob2;
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

    @Autowired
    private DynamicTaskCreate dynamicTaskCreate;

    @Value("${elaticjob.shardingTotalCount}")
    private int shardingTotalCount;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            dynamicTaskCreate.create("myTestJob", "0/10 * * * * ? *", shardingTotalCount, new MyJob2(), "1-10");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

