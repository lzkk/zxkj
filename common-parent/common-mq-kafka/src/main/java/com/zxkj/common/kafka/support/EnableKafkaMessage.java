package com.zxkj.common.kafka.support;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Kafka初始化入口
 *
 * @author ：yuhui
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(KafkaMessageConfig.class)
public @interface EnableKafkaMessage {
}
