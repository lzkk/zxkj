package com.zxkj.gateway.feign;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.zxkj.common.context.domain.ContextInfo;
import com.zxkj.gateway.grey.GreyUtil;

import java.util.concurrent.Callable;

/**
 * 自定义Feign的隔离策略;
 *
 * @see FeignHystrixConcurrencyStrategy
 */
public class FeignHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        ContextInfo contextInfo = GreyUtil.getCurrentContext();
        return () -> {
            try {
                GreyUtil.fillContext(contextInfo);
                return callable.call();
            } finally {
                GreyUtil.clearContext();
            }
        };
    }
}


