package com.zxkj.gateway.feign;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.zxkj.common.context.domain.ContextInfo;
import com.zxkj.gateway.util.GreyPublishUtil;

import java.util.concurrent.Callable;

/**
 * 自定义Feign的隔离策略;
 *
 * @see FeignHystrixConcurrencyStrategy
 */
public class FeignHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        ContextInfo contextInfo = GreyPublishUtil.getCurrentContext();
        return () -> {
            try {
                GreyPublishUtil.fillContext(contextInfo);
                return callable.call();
            } finally {
                GreyPublishUtil.clearContext();
            }
        };
    }
}


