package com.zxkj.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class MonitorServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorServerMain.class);

    public static void main(String[] args) {
        SpringApplication.run(MonitorServerMain.class, args);
        LOGGER.info("监控服务端启动完成...");
    }
}
