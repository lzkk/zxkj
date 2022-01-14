package com.zxkj.gateway.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient sampleWebClient(WebClient.Builder builder) {
        return builder.uriBuilderFactory(new DefaultUriBuilderFactory(WebClientController.endpoint)).build();
    }

}