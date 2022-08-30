//package com.zxkj.goods.listener;
//
//import com.zxkj.common.kafka.KafkaMessageListener;
//import com.zxkj.common.kafka.support.enums.KafkaTopicTypeEnum;
//import com.zxkj.common.web.JsonUtil;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class KafkaConsumerService {
//    protected final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
//
//    @KafkaMessageListener(value = KafkaTopicTypeEnum.TOPIC_KAFKA)
//    public void consume(String key, String value) {
//        logger.info("--receive-{},{}", key, value);
//        key = key.replace("order_", "");
//        int keyInt = Integer.parseInt(key);
//        if (keyInt >= 11 && keyInt <= 15) {
//            throw new RuntimeException("error");
//        }
//    }
//
////    @KafkaListener(topics = "topic_kafka_test", containerFactory = "kafkaListenerContainerFactory")
////    public void consume2(List<ConsumerRecord<?, ?>> records) {
////        for (ConsumerRecord record : records) {
////            logger.info("topic:{},offset:{},info:{}", record.topic(), record.offset(), JsonUtil.toJsonString(record.value()));
////        }
////    }
//
//}
