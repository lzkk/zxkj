//package com.zxkj.goods.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.zxkj.common.activemq.ActivemqMessageSender;
//import com.zxkj.common.activemq.support.ActivemqDestinationEnum;
//import com.zxkj.common.cache.redis.RedisUtil;
//import com.zxkj.common.kafka.KafkaMessageSender;
//import com.zxkj.common.kafka.support.KafkaTopicTypeEnum;
//import com.zxkj.common.rabbitmq.RabbitmqMessageSender;
//import com.zxkj.common.rocketmq.RocketmqMessageSender;
//import com.zxkj.common.rocketmq.support.RocketmqTopicTagEnum;
//import com.zxkj.common.web.RespResult;
//import com.zxkj.goods.model.Sku;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Date;
//
///**
// * 测试专用
// *
// * @author ：yuhui
// * @date ：Created in 2020/10/9 10:01
// */
//@RestController
//public class TestController {
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    @Autowired
//    private RabbitmqMessageSender rabbitmqMessageSender;
//
//    //    @Autowired
////    private BlockQueueProcessor blockQueueProcessor;
////
//    @Autowired
//    private KafkaMessageSender kafkaMessageSender;
//
//    @Autowired
//    private ActivemqMessageSender activemqSender;
//
//    @Autowired
//    private RocketmqMessageSender rocketmqMessageSender;
//
//    @PostMapping("/test")
//    public RespResult<String> test(@RequestBody String name) {
//        // 测试Redis
//        redisUtil.set(name, "test_" + name);
//        logger.info("test_redis:" + redisUtil.get(name));
//
//        if (rocketmqMessageSender != null) {
//            SendResult sendResult = rocketmqMessageSender.send(RocketmqTopicTagEnum.TOPIC_TAG_TEST, name);
//            logger.info("sendResult:" + JSON.toJSONString(sendResult));
//            SendResult sendResult2 = rocketmqMessageSender.send(RocketmqTopicTagEnum.TOPIC_TAG_TEST2, name);
//            logger.info("sendResult2:" + JSON.toJSONString(sendResult2));
//        }
//        return RespResult.ok(name);
//    }
//
//    @PostMapping("/delayMsg")
//    public RespResult<String> delayMsg(String msg, Integer delayTime) {
//        logger.info("发送消息：{}，时间：{},延迟时间:{}", msg, new Date(), delayTime);
//        rabbitmqMessageSender.sendDelayMsg(msg, delayTime);
//
////        new Thread(() -> {
////            for (int k = 1; k <= 50; k++) {
////                blockQueueProcessor.pushToQueue(BlockQueueEnum.BLOCK_QUEUE_TEST, "test-" + k);
////            }
////        }).start();
////        new Thread(() -> {
////            for (int k = 51; k <= 100; k++) {
////                blockQueueProcessor.pushToQueue(BlockQueueEnum.BLOCK_QUEUE_TEST2, "test-" + k);
////            }
////        }).start();
//        return RespResult.ok("ok");
//    }
//
//    @PostMapping("/sendMsg")
//    public RespResult<String> sendMsg(String value, Integer delayTime) {
//        logger.info("sendMsg,value：{}", value);
//        Sku sku = new Sku();
//        sku.setName(value);
//        try {
//            kafkaMessageSender.send(KafkaTopicTypeEnum.TOPIC_KAFKA, String.valueOf(System.currentTimeMillis()), JSON.toJSONString(sku));
//            kafkaMessageSender.producerSend(KafkaTopicTypeEnum.TOPIC_KAFKA_TEST, String.valueOf(System.currentTimeMillis()), 22l);
//        } catch (Exception e) {
//            logger.error("kafka send message error,please see db log", e);
//        }
//
//        activemqSender.send(ActivemqDestinationEnum.ACTIVEMQ_TEST, "test");
//        activemqSender.send(ActivemqDestinationEnum.ACTIVEMQ_TEST2, JSON.toJSONString(sku), delayTime);
//        return RespResult.ok("ok");
//    }
//
//}
