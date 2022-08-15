package com.zxkj.service.ribbon;

import com.zxkj.common.util.net.NetUtil;
import com.zxkj.service.grey.support.GreyContextConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Ribbon配置
 *
 * @author ：yuhui
 * @date ：Created in 2021/8/16 13:47
 */
@Slf4j
@Configuration
@Import(GreyContextConfig.class)
@RibbonClients(defaultConfiguration = MyZoneAvoidanceRule.class)
public class RibbonConfig {

    @PostConstruct
    public void initLocalIP() {
        String ip = NetUtil.getLocalIp();
        log.info("当前服务ip:{}", ip);
    }

    @Bean
    @Scope("prototype")
    public MyServerListUpdater myServerListUpdater() {
        return new MyServerListUpdater();
    }
}
