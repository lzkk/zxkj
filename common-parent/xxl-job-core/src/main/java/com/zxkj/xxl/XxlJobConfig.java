package com.zxkj.xxl;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.zxkj.xxl.util.XxlHttpClientUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Configuration
public class XxlJobConfig {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.ip:}")
    private String ip;

    @Value("${xxl.job.executor.port:0}")
    private int port;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;

    @Bean
    @DependsOn(value = {"flushGroup"})
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appName);
//        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }

    @Bean(value = "flushGroup")
    public List<String> flushGroup() {
        try {
            XxlJobGroup jobGroup = XxlHttpClientUtil.getJobGroup(adminAddresses, appName);
            if (jobGroup == null) {
                String res = XxlHttpClientUtil.createJobGroup(adminAddresses, appName);
                log.info("----JobGroupCreate:{},result:{}", appName, res);
            } else {
                log.info("----JobGroupExist:{}", appName);
            }
        } catch (Exception e) {
            log.error("----flushGroupError", e);
        }
        return new ArrayList<>();
    }

}