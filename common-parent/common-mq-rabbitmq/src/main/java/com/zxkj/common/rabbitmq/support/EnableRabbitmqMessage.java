package com.zxkj.common.rabbitmq.support;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Rabbitmq初始化入口
 *
 * @author ：yuhui
 * @date ：Created in 2021/2/4 14:34
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RabbitmqMessageConfig.class)
public @interface EnableRabbitmqMessage {
}
