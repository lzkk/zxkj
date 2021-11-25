package com.zxkj.order;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 订单微服务
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/4 14:59
 */
@EnableFeignClients(basePackages = {"com.zxkj.*.feign"})
@ComponentScan(basePackages = {"com.zxkj"})
@SpringBootApplication
public class OrderServiceMain implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceMain.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("OrderServiceMain is running!");
    }

}
