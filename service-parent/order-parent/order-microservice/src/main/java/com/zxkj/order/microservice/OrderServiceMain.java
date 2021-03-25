package com.zxkj.order.microservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单微服务
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/4 14:59
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.zxkj.common.feign"})
@ComponentScan(basePackages = {"com.zxkj"})
public class OrderServiceMain implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceMain.class);

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceMain.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("OrderServiceMain is running!");
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
