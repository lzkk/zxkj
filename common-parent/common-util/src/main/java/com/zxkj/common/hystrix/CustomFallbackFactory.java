package com.zxkj.common.hystrix;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * CustomFallbackFactory
 *
 * @author ：yuhui
 * @date ：Created in 2022/3/4 15:59
 */
@Slf4j
public class CustomFallbackFactory<T> implements FallbackFactory<T> {
    protected Throwable throwable;
    private boolean isLoaded = false;

    @Value("${feign.hystrix.enabled}")
    private boolean feignHystrixEnabled;

    public T create(Throwable cause) {
        this.throwable = cause;
        if (!feignHystrixEnabled || isLoaded) {
            log.error("异常信息：" + cause.toString(), cause);
        }
        if (!isLoaded) {
            isLoaded = true;
        }
        return (T) this;
    }

}
