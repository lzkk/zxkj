package com.zxkj.task.task;

import com.zxkj.order.feign.OrderFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ：yuhui
 * @date ：Created in 2022/1/14 10:10
 */
@Slf4j
@Component
public class ScheduleTask {

    @Autowired
    private OrderFeign orderFeign;

    @Scheduled(cron = "0/5 * *  * * ?")
    public void test() {
        orderFeign.ribbonTest();
        log.info("complete!");
    }

}
