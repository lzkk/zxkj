package com.zxkj.feign;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.zxkj.common.context.CustomerContext;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.context.domain.CustomerInfo;
import com.zxkj.feign.decoder.BusinessDecoder;
import com.zxkj.feign.encoder.BusinessEncoder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        CustomerInfo customerInfo = CustomerContext.getCurrentCustomer();
        if (customerInfo == null) {
            return;
        }
        requestTemplate.header(ContextConstant.GREY_PUBLISH_FLAG, customerInfo.getGreyPublish());
        requestTemplate.header(ContextConstant.REGION_PUBLISH_FLAG, customerInfo.getRegionPublish());
        requestTemplate.header(ContextConstant.TRACE_ID_FLAG, customerInfo.getTraceId());
    }

    @Bean
    @ConditionalOnProperty({"feign.hystrix.enabled"})
    public HystrixConcurrencyStrategy hystrixConcurrencyStrategy() {
        FeignHystrixConcurrencyStrategy concurrencyStrategy = new FeignHystrixConcurrencyStrategy();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(concurrencyStrategy);
        return concurrencyStrategy;
    }

    @Bean
    public Logger.Level config() {
        return Logger.Level.BASIC;
    }

    //    @Bean
    public BusinessDecoder businessDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new BusinessDecoder(messageConverters);
    }

    //    @Bean
    public BusinessEncoder businessEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new BusinessEncoder(messageConverters);
    }
}
