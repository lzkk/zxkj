package com.zxkj.seckill;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 秒杀微服务
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/4 14:59
 */
@EnableFeignClients(basePackages = {"com.zxkj.*.feign"})
@ComponentScan(basePackages = {"com.zxkj"})
@SpringBootApplication
public class SeckillServiceMain implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SeckillServiceMain.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("SeckillServiceMain is running!");
    }

}
