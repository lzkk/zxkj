package com.zxkj.search;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 搜索微服务
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/4 14:59
 */
@EnableElasticsearchRepositories(basePackages = "com.zxkj.search.mapper")
@EnableFeignClients(basePackages = {"com.zxkj.*.feign"})
@ComponentScan(basePackages = {"com.zxkj"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SearchServiceMain implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SearchServiceMain.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("SearchServiceMain is running!");
    }

}
