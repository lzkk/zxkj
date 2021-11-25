package com.zxkj.gateway.ribbon;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * Ribbon配置
 *
 * @author ：yuhui
 * @date ：Created in 2021/8/16 13:47
 */
@Configuration
@RibbonClients(defaultConfiguration = MyZoneAvoidanceRule.class)
public class RibbonConfig {

}
