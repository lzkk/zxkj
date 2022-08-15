package com.zxkj.service.feign;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.zxkj.service.grey.GreyContext;
import com.zxkj.common.context.domain.ContextInfo;

import java.util.concurrent.Callable;

/**
 * 自定义Feign的隔离策略;
 *
 * @see FeignHystrixConcurrencyStrategy
 */
public class FeignHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        ContextInfo contextInfo = GreyContext.getCurrentContext();
        return () -> {
            try {
                GreyContext.fillContext(contextInfo);
                return callable.call();
            } finally {
                GreyContext.clearContext();
            }
        };
    }
}


