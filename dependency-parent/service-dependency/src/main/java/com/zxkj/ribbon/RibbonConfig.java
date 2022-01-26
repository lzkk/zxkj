package com.zxkj.ribbon;

import com.zxkj.grey.support.GreyContextConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Ribbon配置
 *
 * @author ：yuhui
 * @date ：Created in 2021/8/16 13:47
 */
@Configuration
@Import(GreyContextConfig.class)
@RibbonClients(defaultConfiguration = MyZoneAvoidanceRule.class)
public class RibbonConfig {

}
