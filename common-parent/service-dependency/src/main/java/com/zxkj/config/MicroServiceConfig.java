package com.zxkj.config;

import com.zxkj.exception.support.ServiceExceptionHandler;
import com.zxkj.feign.decoder.BusinessDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微服务通用配置类
 *
 * @author yuhui
 */
@Configuration
public class MicroServiceConfig {

    @Bean
    public BusinessDecoder businessDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new BusinessDecoder(messageConverters);
    }

    @Bean
    public ServiceExceptionHandler serviceExceptionHandler() {
        return new ServiceExceptionHandler();
    }

}
