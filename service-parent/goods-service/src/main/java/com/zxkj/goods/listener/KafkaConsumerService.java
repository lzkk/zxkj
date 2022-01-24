//package com.zxkj.goods.listener;
//
//import com.zxkj.common.kafka.KafkaMessageListener;
//import com.zxkj.common.kafka.KafkaMessageSender;
//import com.zxkj.common.kafka.support.KafkaTopicTypeEnum;
//import com.zxkj.common.web.JsonUtil;
//import com.zxkj.goods.model.Sku;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.util.List;
//
//@Service
//public class KafkaConsumerService implements InitializingBean {
//    protected final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
//    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
//
//    @Autowired
//    private KafkaMessageSender kafkaMessageSender;
//    @Autowired
//    private KafkaTemplate kafkaTemplate;
//
//    @KafkaMessageListener(value = KafkaTopicTypeEnum.TOPIC_KAFKA)
//    public void consume(String key, String value) {
////        logger.info("--receive-{},{}", key, value);
//    }
//
//    @KafkaListener(topics = "topic_kafka", containerFactory = "kafkaListenerContainerFactory")
//    public void consume2(List<ConsumerRecord<?, ?>> records) {
//        for (ConsumerRecord record : records) {
//            logger.info("topic:{},offset:{},info:{}", record.topic(), record.offset(), JsonUtil.toJsonString(record.value()));
//        }
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
////        for (int k = 0; k < 10; k++) {
////            try {
////                Sku sku = new Sku();
////                sku.setName(k + "");
////                kafkaTemplate.send(KafkaTopicTypeEnum.TOPIC_KAFKA.getTopicName(), "" + k);
////                kafkaMessageSender.send(KafkaTopicTypeEnum.TOPIC_KAFKA, String.valueOf(System.currentTimeMillis()), JsonUtil.toJsonString(sku));
////            } catch (Exception e) {
////                logger.error("kafka send message error,please see db log", e);
////            }
////        }
//    }
//}
