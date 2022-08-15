package com.zxkj.service.feign;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.zxkj.service.grey.GreyContext;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.context.domain.ContextInfo;
import com.zxkj.service.feign.decoder.BusinessDecoder;
import com.zxkj.service.feign.encoder.BusinessEncoder;
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
        ContextInfo contextInfo = GreyContext.getCurrentContext();
        if (contextInfo == null) {
            return;
        }
        requestTemplate.header(ContextConstant.GREY_PUBLISH_FLAG, contextInfo.getGreyPublish());
        requestTemplate.header(ContextConstant.REGION_PUBLISH_FLAG, contextInfo.getRegionPublish());
        requestTemplate.header(ContextConstant.TRACE_ID_FLAG, contextInfo.getTraceId());
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
