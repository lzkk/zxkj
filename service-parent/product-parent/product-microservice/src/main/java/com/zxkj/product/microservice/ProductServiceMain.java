package com.zxkj.product.microservice;

import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 产品微服务
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/4 14:59
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ProductServiceMain implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceMain.class);

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceMain.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.setProperty(TransportConfig.HEARTBEAT_CLIENT_IP, "172.17.53.138");
        System.out.println("ProductServiceMain is running!");
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
