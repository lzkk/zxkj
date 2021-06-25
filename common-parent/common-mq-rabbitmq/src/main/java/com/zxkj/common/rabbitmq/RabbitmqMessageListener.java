package com.zxkj.common.rabbitmq;

import com.zxkj.common.rabbitmq.support.enums.BusiTypeHandler;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbitmqMessageListener {
    BusiTypeHandler value();

    /**
     * 同时监听数量
     */
    String concurrentConsumers();

    /**
     * 最大同时监听数量
     */
    String maxConcurrentConsumers();

    /**
     * 设置预取数量
     */
    int prefetchCount() default -1;
}