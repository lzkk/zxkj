package com.zxkj.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class EnvLocalPreferRule extends AbstractLoadBalancerRule {
    private static Logger log = LoggerFactory.getLogger(EnvLocalPreferRule.class);

    public EnvLocalPreferRule() {

    }

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        }
        List<Server> serverList = lb.getReachableServers();
        if (serverList.size() == 0) {
            log.warn("No up servers available from load balancer: " + lb);
            return null;
        }
        List<String> localIpList = getLocalIp();
        for (Server s : serverList) {
            String host = s.getHost();
            if (localIpList.contains(host)) {
                return s;
            }
        }
        return serverList.get(0);
    }

    public Server choose(Object key) {
        ILoadBalancer loadBalancer = getLoadBalancer();
        log.info("ILoadBalancer:"+loadBalancer.toString());
        return choose(loadBalancer, key);
    }

    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    private List<String> getLocalIp() {
        List<String> ipList = new ArrayList<String>();
        InetAddress localHost = null;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String ip = localHost.getHostAddress();  // 返回格式为：xxx.xxx.xxx
        ipList.add(ip);
        return ipList;
    }
}
