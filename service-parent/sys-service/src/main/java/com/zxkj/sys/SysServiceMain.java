package com.zxkj.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 系统基础微服务
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/4 14:59
 */
@MapperScan(basePackages = {"com.zxkj.sys.mapper"})
@EnableFeignClients(basePackages = {"com.zxkj.feign"})
@ComponentScan(basePackages = {"com.zxkj"})
@SpringBootApplication
public class SysServiceMain implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SysServiceMain.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("SysServiceMain is running!");
    }

}
