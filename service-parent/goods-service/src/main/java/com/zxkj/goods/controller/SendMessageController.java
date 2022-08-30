//package com.zxkj.goods.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.zxkj.common.activemq.ActivemqMessageSender;
//import com.zxkj.common.activemq.support.enums.ActivemqDestinationEnum;
//import com.zxkj.common.kafka.KafkaMessageSender;
//import com.zxkj.common.kafka.support.enums.KafkaTopicTypeEnum;
//import com.zxkj.common.rabbitmq.RabbitmqMessageSender;
//import com.zxkj.common.rabbitmq.support.enums.BusiType;
//import com.zxkj.common.rocketmq.RocketmqMessageSender;
//import com.zxkj.common.rocketmq.support.enums.RocketmqTopicTagEnum;
//import com.zxkj.common.web.RespResult;
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
// * 测试消息类
// *
// * @author ：yuhui
// * @date ：Created in 2020/10/9 10:01
// */
//@RestController
//public class SendMessageController {
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private ActivemqMessageSender activemqSender;
//
//    @Autowired
//    private RabbitmqMessageSender rabbitmqMessageSender;
//
//    @Autowired
//    private KafkaMessageSender kafkaMessageSender;
//
//    @Autowired
//    private RocketmqMessageSender rocketmqMessageSender;
//
//    @PostMapping("/testActivemq")
//    public RespResult<String> testActivemq(String msg, Long delayTime) {
//        logger.info("发送Activemq消息：{}，时间：{},延迟时间:{}", msg, new Date(), delayTime);
//        activemqSender.send(ActivemqDestinationEnum.ACTIVEMQ_TEST, msg, delayTime);
////        activemqSender.send(ActivemqDestinationEnum.ACTIVEMQ_TEST2, msg, delayTime);
//        return RespResult.ok("ok");
//    }
//
//    @PostMapping("/testRabbitmq")
//    public RespResult<String> testRabbitmq(Integer begin, Integer end, Integer delayTime) {
//        logger.info("发送Rabbitmq消息：{}，时间：{},延迟时间:{}", begin + "," + end, new Date(), delayTime);
//        for (int k = begin; k <= end; k++) {
//            rabbitmqMessageSender.send(BusiType.ORDER_CREATE_BUSI, "order_" + k, "orderData_" + k, delayTime);
//        }
//        return RespResult.ok("ok");
//    }
//
//    @PostMapping("/testKafka")
//    public RespResult<String> testKafka(Integer begin, Integer end) {
//        logger.info("发送Kafka消息：{}，时间：{}", begin + "," + end, new Date());
//        for (int k = begin; k <= end; k++) {
//            kafkaMessageSender.send(KafkaTopicTypeEnum.TOPIC_KAFKA, "order_" + k, "orderData_" + k);
////            kafkaMessageSender.producerSend(KafkaTopicTypeEnum.TOPIC_KAFKA_TEST, String.valueOf(System.currentTimeMillis()), System.currentTimeMillis());
//        }
//        return RespResult.ok("ok");
//    }
//
//    @PostMapping("/testRocketmq")
//    public RespResult<String> testRocketmq(@RequestBody String name) {
//        if (rocketmqMessageSender != null) {
//            SendResult sendResult = rocketmqMessageSender.send(RocketmqTopicTagEnum.TOPIC_TAG_TEST, name);
//            logger.info("sendResult:" + JSON.toJSONString(sendResult));
////            SendResult sendResult2 = rocketmqMessageSender.send(RocketmqTopicTagEnum.TOPIC_TAG_TEST2, name);
////            logger.info("sendResult2:" + JSON.toJSONString(sendResult2));
//        }
//        return RespResult.ok(name);
//    }
//
//}
