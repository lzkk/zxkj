package com.zxkj.gateway.ribbon;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @author yuhui
 */
@Slf4j
public class NacosNamingUtil {

    private static volatile NamingService naming = null;

    public static NamingService getNamingService(Properties properties) {
        if (naming == null) {
            synchronized (NacosNamingUtil.class) {
                if (naming == null) {
                    try {
                        log.info("create nacos namingservice");
                        naming = NamingFactory.createNamingService(properties);
                    } catch (NacosException e) {
                        log.error("nacos invoke error", e);
                    }
                }
            }
        }
        return naming;
    }
}
