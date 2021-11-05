package com.zxkj.task.task;

import com.zxkj.task.bean.Job;
import com.zxkj.task.config.ElasticJobService;
import com.zxkj.task.job.MyJob3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 1、多片的任务调用在微服务里处理(分布式计算)
 * 2、单片的任务可以在这里统一管理(通过feign调用、便于排查统计)，也可以在微服务管理
 */
@Slf4j
@Component
public class StaticJobTask implements InitializingBean {

    @Autowired
    private ElasticJobService elasticJobService;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
//                elasticJobService.processSimpleJob(Job.getInstance(MyJob2.class, "0/3 * * * * ? *"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
//                elasticJobService.processSimpleJob(Job.getInstance(MyJob3.class, "0/4 * * * * ? *", "1-30", 2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

