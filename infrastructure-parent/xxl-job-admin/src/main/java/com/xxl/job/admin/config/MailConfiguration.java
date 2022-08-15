package com.xxl.job.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@Slf4j
public class MailConfiguration {

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        log.info("MailConfiguration==========邮箱配置加载......");
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qiye.aliyun.com");
        mailSender.setUsername("data_service@winzct.com");
        mailSender.setPassword("PuPXg9PKGnbDPgnl");
        mailSender.setProtocol("smtp");
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setPort(25);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.starttls.required", "true");
        javaMailProperties.put("mail.smtp.timeout", 5000);
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }
}
