package com.zxkj.monitor.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * MetadataConfig
 *
 * @author ：yuhui
 * @date ：Created in 2021/9/1 23:33
 */
@Configuration
public class MetadataConfig {

    @Bean
    @ConditionalOnMissingBean
    public NacosDiscoveryProperties nacosProperties() {
        NacosDiscoveryProperties nacosDiscoveryProperties = new NacosDiscoveryProperties();
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        metadata.put("management.context-path", "/");
        return nacosDiscoveryProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public NacosServiceDiscovery nacosServiceDiscovery(NacosDiscoveryProperties discoveryProperties) {
        return new NacosServiceDiscovery(discoveryProperties);
    }

}
