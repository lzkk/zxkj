package com.zxkj.common.activemq.support;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Activemq初始化入口
 *
 * @author ：yuhui
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ActivemqMessageConfig.class)
public @interface EnableActivemqMessage {
}
