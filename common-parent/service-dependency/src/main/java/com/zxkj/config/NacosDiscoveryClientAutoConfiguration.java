package com.zxkj.config;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosWatch;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.util.sys.SysConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class NacosDiscoveryClientAutoConfiguration {

    @Value("${preserved.heart.beat.interval:3000}")
    public String heartBeatInterval;

    @Value("${preserved.heart.beat.timeout:6000}")
    public String heartBeatTimeout;

    @Value("${preserved.ip.delete.timeout:9000}")
    public String ipDeleteTimeout;

    @Value("${spring.profiles.active}")
    public String profile;

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
        nacosDiscoveryProperties.getMetadata().put(ContextConstant.REGION_PUBLISH_FLAG, SysConfigUtil.getSysConfigValue(ContextConstant.REGION_PUBLISH_FLAG));
        nacosDiscoveryProperties.getMetadata().put(ContextConstant.GREY_PUBLISH_FLAG, SysConfigUtil.getSysConfigValue(ContextConstant.GREY_PUBLISH_FLAG));
        nacosDiscoveryProperties.getMetadata().put("spring.profiles.active", profile);
        nacosDiscoveryProperties.getMetadata().put("startup.time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        // 设置心跳的周期，单位为秒
//        nacosDiscoveryProperties.getMetadata().put(PreservedMetadataKeys.HEART_BEAT_INTERVAL, heartBeatInterval);
//        // 设置心跳超时时间，单位为秒
//        // 即服务端6秒收不到客户端心跳，会将该客户端注册的实例设为不健康：
//        nacosDiscoveryProperties.getMetadata().put(PreservedMetadataKeys.HEART_BEAT_TIMEOUT, heartBeatTimeout);
//        // 设置实例删除的超时时间，单位为秒
//        // 即服务端9秒收不到客户端心跳，会将该客户端注册的实例删除：
//        nacosDiscoveryProperties.getMetadata().put(PreservedMetadataKeys.IP_DELETE_TIMEOUT, ipDeleteTimeout);
        return new NacosWatch(nacosDiscoveryProperties);
    }

}