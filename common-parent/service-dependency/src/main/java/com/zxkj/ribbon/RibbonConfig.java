package com.zxkj.ribbon;

import com.zxkj.ribbon.EnvLocalPreferRule;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Ribbon配置
 *
 * @author ：yuhui
 * @date ：Created in 2021/8/16 13:47
 */
@Profile("dev")
@Configuration
@RibbonClients(defaultConfiguration = EnvLocalPreferRule.class)
public class RibbonConfig {

}
