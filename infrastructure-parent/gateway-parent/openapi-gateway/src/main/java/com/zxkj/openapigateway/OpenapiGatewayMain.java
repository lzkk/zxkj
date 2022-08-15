package com.zxkj.openapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.zxkj"})
@SpringBootApplication
public class OpenapiGatewayMain {

    public static void main(String[] args) {
        SpringApplication.run(OpenapiGatewayMain.class, args);
    }

}