package com.zxkj.canal;

import com.baomidou.mybatisplus.autoconfigure.IdentifierGeneratorAutoConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/*****
 * @Author:
 * @Description:
 ****/
@EnableFeignClients(basePackages = {"com.zxkj.*.feign"})
@ComponentScan(basePackages = {"com.zxkj"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, IdentifierGeneratorAutoConfiguration.class})
public class CanalServiceMain implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CanalServiceMain.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("CanalServiceMain is running!");
    }

}
