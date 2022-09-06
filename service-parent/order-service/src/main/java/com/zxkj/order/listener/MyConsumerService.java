//package com.zxkj.order.mq.consume;
//
//import com.zxkj.common.rocketmq.RocketmqMessageListener;
//import com.zxkj.common.rocketmq.support.RocketmqTopicTagEnum;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyConsumerService {
//    protected final Logger logger = LoggerFactory.getLogger(MyConsumerService.class);
//
//    @RocketmqMessageListener(value = RocketmqTopicTagEnum.TOPIC_TAG_TEST)
//    public void receiveTransaction(String message) {
//        logger.info("rocketmq数据普通：" + message);
//    }
//
//    @RocketmqMessageListener(value = RocketmqTopicTagEnum.TOPIC_TAG_TEST)
//    public void receiveTransaction2(String message) {
//        logger.info("rocketmq数据普通2：" + message);
//    }
//
//    @RocketmqMessageListener(value = RocketmqTopicTagEnum.TOPIC_TAG_TEST2)
//    public void receive(String message) {
//        logger.info("rocketmq数据事务：" + message);
//    }
//
//    @RocketmqMessageListener(value = RocketmqTopicTagEnum.TOPIC_TAG_TEST2)
//    public void receive2(String message) {
//        logger.info("rocketmq数据事务2：" + message);
//    }
//
//}
