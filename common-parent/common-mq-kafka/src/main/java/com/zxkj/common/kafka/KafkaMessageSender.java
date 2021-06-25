package com.zxkj.common.kafka;

import com.zxkj.common.kafka.support.KafkaMessageHelper;
import com.zxkj.common.kafka.support.KafkaTopicTypeEnum;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Objects;

/**
 * Kafka消息发送类
 *
 * @author yuhui
 */
public class KafkaMessageSender {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageSender.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private KafkaProducer kafkaProducer;

    /**
     * 发送事件消息
     *
     * @param topicType
     * @param busiKey
     * @param busiObject
     * @param <T>
     */
    public <T> void send(KafkaTopicTypeEnum topicType, String busiKey, T busiObject) {
        Objects.requireNonNull(busiKey, "busiKey不能为null");
        Objects.requireNonNull(busiObject, "busiObject不能为null");
        if (!topicType.getBusiObjectClass().isAssignableFrom(busiObject.getClass())) {
            throw new RuntimeException("busiObject格式不正确，应该是：" + topicType.getBusiObjectClass());
        }
        String jsonStr = KafkaMessageHelper.toJson(busiKey, busiObject);
        logger.info("业务消息发送开始: {} - {}", busiObject, busiKey);
        kafkaTemplate.send(topicType.getTopicName(), jsonStr);
    }


    /**
     * 发送事件消息
     *
     * @param topicType
     * @param busiKey
     * @param busiObject
     * @param <T>
     */
    public <T> void producerSend(KafkaTopicTypeEnum topicType, String busiKey, T busiObject) {
        Objects.requireNonNull(busiKey, "busiKey不能为null");
        Objects.requireNonNull(busiObject, "busiObject不能为null");
        if (!topicType.getBusiObjectClass().isAssignableFrom(busiObject.getClass())) {
            throw new RuntimeException("busiObject格式不正确，应该是：" + topicType.getBusiObjectClass());
        }
        String jsonStr = KafkaMessageHelper.toJson(busiKey, busiObject);
        logger.info("业务消息发送开始: {} - {}", busiObject, busiKey);
        kafkaProducer.send(new ProducerRecord<String, String>(topicType.getTopicName(), jsonStr));
    }

}
