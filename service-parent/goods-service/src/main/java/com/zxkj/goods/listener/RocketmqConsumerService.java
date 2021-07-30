//package com.zxkj.goods.listener;
//
//import com.zxkj.common.rocketmq.RocketmqMessageListener;
//import com.zxkj.common.rocketmq.support.RocketmqTopicTagEnum;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RocketmqConsumerService {
//    protected final Logger logger = LoggerFactory.getLogger(RocketmqConsumerService.class);
//
//    @RocketmqMessageListener(value = RocketmqTopicTagEnum.TOPIC_TAG_TEST)
//    public void receive(String message) throws Exception {
//        logger.info("rocketmq数据：" + message);
//    }
//
//    @RocketmqMessageListener(value = RocketmqTopicTagEnum.TOPIC_TAG_TEST2)
//    public void receive2(String message) throws Exception {
//        logger.info("rocketmq2数据：" + message);
//    }
//
//}
