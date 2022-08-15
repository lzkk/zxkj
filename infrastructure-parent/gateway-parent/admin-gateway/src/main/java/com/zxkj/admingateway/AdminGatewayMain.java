package com.zxkj.admingateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.zxkj"})
@SpringBootApplication
public class AdminGatewayMain {

    public static void main(String[] args) {
        SpringApplication.run(AdminGatewayMain.class, args);
    }

}