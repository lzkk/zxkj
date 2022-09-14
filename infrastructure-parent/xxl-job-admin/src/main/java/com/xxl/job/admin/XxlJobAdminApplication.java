package com.xxl.job.admin;

import com.xxl.job.core.config.Config;
import com.zxkj.xxl.XxlJobConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication(exclude = {XxlJobConfig.class, Config.class})
public class XxlJobAdminApplication {

	public static void main(String[] args) {
        SpringApplication.run(XxlJobAdminApplication.class, args);
	}

}