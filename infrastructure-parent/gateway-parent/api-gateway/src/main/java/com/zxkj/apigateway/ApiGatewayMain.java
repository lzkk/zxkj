package com.zxkj.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.zxkj"})
@SpringBootApplication
public class ApiGatewayMain {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayMain.class, args);
    }

}