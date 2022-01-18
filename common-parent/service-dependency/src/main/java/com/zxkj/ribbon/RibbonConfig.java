package com.zxkj.ribbon;

import com.zxkj.common.context.support.EnableGreyContext;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * Ribbon配置
 *
 * @author ：yuhui
 * @date ：Created in 2021/8/16 13:47
 */
@Configuration
@EnableGreyContext
@RibbonClients(defaultConfiguration = MyZoneAvoidanceRule.class)
public class RibbonConfig {

}
