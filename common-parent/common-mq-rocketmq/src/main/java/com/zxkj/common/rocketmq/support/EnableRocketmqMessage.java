package com.zxkj.common.rocketmq.support;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Rocketmq初始化入口
 *
 * @author ：yuhui
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RocketmqMessageConfig.class)
public @interface EnableRocketmqMessage {
}
