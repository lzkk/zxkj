package com.zxkj.feign.fallback;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * CustomFallbackFactory
 *
 * @author ：yuhui
 */
public class CustomFallbackFactory<T> implements FallbackFactory<T> {
    private static Logger log = LoggerFactory.getLogger(CustomFallbackFactory.class);

    protected Throwable throwable;
    private boolean isLoaded = false;

    @Value("${feign.hystrix.enabled}")
    private boolean feignHystrixEnabled;

    public T create(Throwable cause) {
        this.throwable = cause;
        if (!feignHystrixEnabled || isLoaded) {
//            log.error("errMsg：" + cause.toString(), cause);
            log.error("errMsg：" + cause.toString());
        }
        if (!isLoaded) {
            isLoaded = true;
        }
        return (T) this;
    }

}
