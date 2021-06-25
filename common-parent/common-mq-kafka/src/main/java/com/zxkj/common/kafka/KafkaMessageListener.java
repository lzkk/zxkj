package com.zxkj.common.kafka;

import com.zxkj.common.kafka.support.KafkaTopicTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KafkaMessageListener {
    KafkaTopicTypeEnum value();

    String[] topics() default {};
}