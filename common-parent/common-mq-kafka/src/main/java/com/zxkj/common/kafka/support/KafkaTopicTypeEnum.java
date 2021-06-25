package com.zxkj.common.kafka.support;

/**
 * 主体类型
 */
public enum KafkaTopicTypeEnum {

    TOPIC_KAFKA("topic_kafka", String.class),
    TOPIC_KAFKA_TEST("topic_kafka_test", Long.class)//
    ;

    KafkaTopicTypeEnum(String topicName, Class<?> busiObjectClass) {
        this.topicName = topicName;
        this.busiObjectClass = busiObjectClass;
    }

    private String topicName;
    private Class<?> busiObjectClass;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Class<?> getBusiObjectClass() {
        return busiObjectClass;
    }

    public void setBusiObjectClass(Class<?> busiObjectClass) {
        this.busiObjectClass = busiObjectClass;
    }}
