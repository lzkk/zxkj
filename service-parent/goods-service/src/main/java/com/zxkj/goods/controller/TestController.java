package com.zxkj.goods.controller;

import com.zxkj.common.web.RespResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试专用
 *
 * @author ：yuhui
 * @date ：Created in 2020/10/9 10:01
 */
@RestController
public class TestController {
    private Logger logger = LoggerFactory.getLogger(getClass());

//    @Autowired
//    private KafkaTemplate kafkaTemplate;

//    @Autowired
//    private Cache cache;

//    @Autowired
//    private RabbitmqMessageSender rabbitmqMessageSender;

    //    @Autowired
//    private BlockQueueProcessor blockQueueProcessor;
//
//    @Autowired
//    private KafkaMessageSender kafkaMessageSender;

//    @Autowired
//    private ActivemqMessageSender activemqSender;
//
//    @Autowired
//    private RocketmqMessageSender rocketmqMessageSender;

//    @PostMapping("/test")
//    public RespResult<String> test(@RequestBody String name) {
//        // 测试Redis
//        cache.set(name, "test_" + name);
//        logger.info("test_redis:" + cache.get(name));
//
////        if (rocketmqMessageSender != null) {
////            SendResult sendResult = rocketmqMessageSender.send(RocketmqTopicTagEnum.TOPIC_TAG_TEST, name);
////            logger.info("sendResult:" + JSON.toJSONString(sendResult));
////            SendResult sendResult2 = rocketmqMessageSender.send(RocketmqTopicTagEnum.TOPIC_TAG_TEST2, name);
////            logger.info("sendResult2:" + JSON.toJSONString(sendResult2));
////        }
//        return RespResult.ok(name);
//    }
//
//    @PostMapping("/delayMsg")
//    public RespResult<String> delayMsg(String msg, Integer delayTime) {
//        logger.info("发送消息：{}，时间：{},延迟时间:{}", msg, new Date(), delayTime);
////        rabbitmqMessageSender.sendDelayMsg(msg, delayTime);
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

    @PostMapping("/sendMsg")
    public RespResult<String> sendMsg(String value, Integer delayTime) {
//        for (int k = 1; k <= 100; k++) {
//            rabbitmqMessageSender.send(BusiType.ORDER_CREATE_BUSI, "order_" + k, "orderData_" + k);
//        }
//
//        logger.info("sendMsg,value：{}", value);
//        Sku sku = new Sku();
//        sku.setName(value);
//        try {
////            kafkaTemplate.send("topic_kafka",value);
////            kafkaMessageSender.send(KafkaTopicTypeEnum.TOPIC_KAFKA, String.valueOf(System.currentTimeMillis()), JSON.toJSONString(sku));
////            kafkaMessageSender.producerSend(KafkaTopicTypeEnum.TOPIC_KAFKA_TEST, String.valueOf(System.currentTimeMillis()), System.currentTimeMillis());
//        } catch (Exception e) {
//            logger.error("kafka send message error,please see db log", e);
//        }
//
////        activemqSender.send(ActivemqDestinationEnum.ACTIVEMQ_TEST, "test");
////        activemqSender.send(ActivemqDestinationEnum.ACTIVEMQ_TEST2, JSON.toJSONString(sku), delayTime);
//        return RespResult.ok("ok");
        return  null;
    }

}
