package com.zxkj.feign;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.zxkj.common.context.CustomerContext;
import com.zxkj.common.context.domain.CustomerInfo;

import java.util.concurrent.Callable;

/**
 * 自定义Feign的隔离策略;
 *
 * @see FeignHystrixConcurrencyStrategy
 */
public class FeignHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        CustomerInfo customerInfo = CustomerContext.getCurrentCustomer();
        return () -> {
            try {
                CustomerContext.fillContext(customerInfo);
                return callable.call();
            } finally {
                CustomerContext.clearContext();
            }
        };
    }
}


