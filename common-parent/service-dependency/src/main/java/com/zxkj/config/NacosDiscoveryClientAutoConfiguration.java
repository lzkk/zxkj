package com.zxkj.config;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosWatch;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.context.domain.CustomerInfo;
import com.zxkj.common.util.greyPublish.GreyPublishUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * nacos客户端注册至服务端时，更改服务详情中的元数据，元数据显示服务注册时间
 */
@Configuration
@ConditionalOnNacosDiscoveryEnabled
@AutoConfigureBefore({SimpleDiscoveryClientAutoConfiguration.class, CommonsClientAutoConfiguration.class})
public class NacosDiscoveryClientAutoConfiguration {

    @Value("${spring.profiles.active}")
    public String profile;

    @Autowired
    private ApplicationArguments applicationArguments;

    public NacosDiscoveryClientAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public NacosDiscoveryProperties nacosProperties() {
        return new NacosDiscoveryProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = {"spring.cloud.nacos.discovery.watch.enabled"}, matchIfMissing = true)
    public NacosWatch nacosWatch(NacosDiscoveryProperties nacosDiscoveryProperties) {
        //更改服务详情中的元数据，增加服务注册时间
        CustomerInfo customerInfo = GreyPublishUtil.getPublishInfo(applicationArguments.getSourceArgs());
        nacosDiscoveryProperties.getMetadata().put(ContextConstant.REGION_PUBLISH_FLAG, customerInfo.getRegionPublish());
        nacosDiscoveryProperties.getMetadata().put(ContextConstant.GREY_PUBLISH_FLAG, customerInfo.getGreyPublish());
        nacosDiscoveryProperties.getMetadata().put("spring.profiles.active", profile);
        nacosDiscoveryProperties.getMetadata().put("startup.time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return new NacosWatch(nacosDiscoveryProperties);
    }
}