package com.zxkj.common.context.support;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用微服务上下文
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GreyContextConfig.class)
public @interface EnableGreyContext {
}
