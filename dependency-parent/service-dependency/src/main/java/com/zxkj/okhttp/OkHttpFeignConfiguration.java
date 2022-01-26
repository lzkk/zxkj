package com.zxkj.okhttp;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass({OkHttpClient.class})
@AutoConfigureBefore(FeignAutoConfiguration.class)
@ConditionalOnProperty({"feign.okhttp.enabled"})
public class OkHttpFeignConfiguration {

    private OkHttpClient okHttpClient;

    @Resource
    private FeignHttpClientProperties httpClientProperties;

    @Bean
    public ConnectionPool httpClientConnectionPool() {
        Integer maxTotalConnections = httpClientProperties.getMaxConnections();
        Long timeToLive = httpClientProperties.getTimeToLive();
        TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
        ConnectionPool connectionPool = new ConnectionPool(maxTotalConnections, timeToLive, ttlUnit);
        return connectionPool;
    }

    @Bean
    public OkHttpClient okHttpClient(ConnectionPool connectionPool) {
        Integer connectTimeout = httpClientProperties.getConnectionTimeout();
        Boolean followRedirects = httpClientProperties.isFollowRedirects();
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(httpClientProperties.getMaxConnectionsPerRoute());
        dispatcher.setMaxRequests(httpClientProperties.getMaxConnections());
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout((long) connectTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects)
                .connectionPool(connectionPool)
                .dispatcher(dispatcher)
                .addInterceptor(new MyOkHttpInterceptor())
                .build();
        return this.okHttpClient;
    }


    @PreDestroy
    public void destroy() {
        if (this.okHttpClient != null) {
            this.okHttpClient.dispatcher().executorService().shutdown();
            this.okHttpClient.connectionPool().evictAll();
        }

    }

}
