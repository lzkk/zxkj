//package com.zxkj.goods.listener;
//
//import com.zxkj.common.rabbitmq.RabbitmqMessageListener;
//import com.zxkj.common.rabbitmq.support.enums.BusiTypeHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//
///**
// * 2019/5/13
// * created by 余辉
// */
//@Component
//public class RabbitmqConsumerService {
//    private static final Logger log = LoggerFactory.getLogger(RabbitmqConsumerService.class);
//
//    @RabbitmqMessageListener(value = BusiTypeHandler.ORDER_CREATE_BUSI_TASK, concurrentConsumers = 2, maxConcurrentConsumers = 2)
//    public void processMessage(String key, String value) {
//        log.info("队列收到消息：{}，当前时间：{}", key + "," + value, new Date());
//        key = key.replace("order_", "");
//        int keyInt = Integer.parseInt(key);
//        if (keyInt >= 11 && keyInt <= 15) {
//            throw new RuntimeException("error");
//        }
//    }
//
////    @RabbitListener(queues = "ORDER_CREATE_BUSI_TASK", concurrency = "2")
////    public void processMessage(Message message) {
////        log.info("队列收到消息：{}，当前时间：{}", new String(message.getBody()), new Date());
////    }
//
//}
