//package com.zxkj.goods.listener;
//
//import com.alibaba.fastjson.JSON;
//import com.zxkj.common.kafka.KafkaMessageListener;
//import com.zxkj.common.kafka.support.KafkaTopicTypeEnum;
//import com.zxkj.goods.model.Sku;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaConsumerService {
//    protected final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
//
//    @KafkaMessageListener(value = KafkaTopicTypeEnum.TOPIC_KAFKA)
//    public void consume(String key, String value) {
//        logger.info("--receive-{},{}", key, value);
//    }
//
//    @KafkaMessageListener(value = KafkaTopicTypeEnum.TOPIC_KAFKA_TEST)
//    public void consume2(String key, Long value) {
//        logger.info("--receiveTest-{},{}", key, JSON.toJSONString(value));
//    }
//
//}
