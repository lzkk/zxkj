package com.zxkj.feign;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.zxkj.feign.decoder.BusinessDecoder;
import com.zxkj.feign.encoder.BusinessEncoder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Configuration
public class FeignConfiguration implements RequestInterceptor {

    private List<String> headKeyList = new ArrayList<>();

    {
        headKeyList.add("version");
        headKeyList.add("regionPublish");
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames == null) {
                return;
            }
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if (headKeyList.contains(name)) {
                    String values = request.getHeader(name);
                    requestTemplate.header(name, values);
                }
            }
        }
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

    @Bean
    public BusinessDecoder businessDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new BusinessDecoder(messageConverters);
    }

    @Bean
    public BusinessEncoder businessEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new BusinessEncoder(messageConverters);
    }
}
