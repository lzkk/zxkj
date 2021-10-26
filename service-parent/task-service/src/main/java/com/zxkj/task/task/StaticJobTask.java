package com.zxkj.task.task;

import com.zxkj.task.bean.Job;
import com.zxkj.task.config.ElasticJobService;
import com.zxkj.task.job.MyJob2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StaticJobTask implements InitializingBean {

    @Autowired
    private ElasticJobService elasticJobService;

    private void method1() {
        try {
            elasticJobService.processSimpleJob(Job.getInstance(MyJob2.class, "0/3 * * * * ? *"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void method2() {
        try {
            elasticJobService.processSimpleJob(Job.getInstance(MyJob2.class, "0/3 * * * * ? *"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                method1();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                method2();
            }
        }).start();


    }
}

