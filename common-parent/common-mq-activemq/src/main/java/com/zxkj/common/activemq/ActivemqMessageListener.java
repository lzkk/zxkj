package com.zxkj.common.activemq;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActivemqMessageListener {

    String destination();

    int concurrency() default 1;
}