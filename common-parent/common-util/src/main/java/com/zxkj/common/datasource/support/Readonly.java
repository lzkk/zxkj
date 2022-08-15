package com.zxkj.common.datasource.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解标记在Controller方法上
 *
 * @author yuhui
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Readonly {
    String value() default DynamicDataSource.READONLY;
}
