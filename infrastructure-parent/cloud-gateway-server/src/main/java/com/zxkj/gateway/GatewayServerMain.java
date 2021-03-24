package com.zxkj.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayServerMain {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerMain.class, args);
    }

    @RestController
    @RefreshScope
    static class TestController {

        @Value("${didispace.title:}")
        private String hello;

        @GetMapping("/hello")
        public String hello() {
            return hello;
        }

        @GetMapping("/test")
        public String test() {
            return hello;
        }

    }

}