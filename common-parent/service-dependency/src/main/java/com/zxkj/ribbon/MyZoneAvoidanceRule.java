package com.zxkj.ribbon;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.netflix.loadbalancer.*;
import com.zxkj.common.context.CustomerContext;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.util.ip.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

import java.util.*;

@Slf4j
public class MyZoneAvoidanceRule extends ZoneAvoidanceRule {
    private volatile boolean fetchFlag = false;
    private static final String ENVIRONMENT_DEV = "dev";
    private AbstractServerPredicate predicate;

    @Value("${spring.profiles.active}")
    private String currentEnv;
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddress;
    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;

    @Autowired
    private MyServerListUpdater myServerListUpdater;

    public MyZoneAvoidanceRule() {
        super();
        this.predicate = new MyServerPredicate(super.getPredicate());
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return this.predicate;
    }

    public Server choose(Object key) {
        ILoadBalancer lb = this.getLoadBalancer();
        List<Server> serverList = new ArrayList<>();
        if (lb instanceof ZoneAwareLoadBalancer) {
            ZoneAwareLoadBalancer zoneAwareLoadBalancer = (ZoneAwareLoadBalancer) lb;
            String clientName = zoneAwareLoadBalancer.getName();
            serverList = getServerListByClientName(zoneAwareLoadBalancer, clientName);
        }
        Optional<Server> server = this.getPredicate().chooseRoundRobinAfterFiltering(serverList, key);
        return server.isPresent() ? server.get() : null;
    }

    private List<Server> getServerListByClientName(ZoneAwareLoadBalancer lb, String clientName) {
        if (fetchFlag) {
            return lb.getAllServers();
        }
        synchronized (this) {
            if (fetchFlag) {
                return lb.getAllServers();
            }
            log.info("node:{} init begin", clientName);
            ServerListUpdater.UpdateAction updateAction = myServerListUpdater.getUpdateAction();
            if (updateAction != null) {
                updateAction.doUpdate();
            }
            fetchFlag = true;
            log.info("node:{} init end", clientName);
        }
        try {
            NamingService naming = NamingFactory.createNamingService(getNacosProperties());
            naming.subscribe(clientName, event -> {
                if (event instanceof NamingEvent) {
                    ServerListUpdater.UpdateAction updateAction = myServerListUpdater.getUpdateAction();
                    if (updateAction != null) {
                        updateAction.doUpdate();
                    }
                }
            });
        } catch (NacosException e) {
            log.error("订阅NACOS出错", e);
        }
        return lb.getAllServers();
    }

    private Properties getNacosProperties() {
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddress);
        properties.put("namespace", namespace);
        return properties;
    }

    private class MyServerPredicate extends AbstractServerPredicate {
        private AbstractServerPredicate myPredicate;

        public MyServerPredicate(AbstractServerPredicate compositePredicate) {
            this.myPredicate = compositePredicate;
        }

        @Override
        public boolean apply(@Nullable PredicateKey input) {
            return myPredicate.apply(input);
        }

        @Override
        public List<Server> getEligibleServers(List<Server> servers, Object loadBalancerKey) {
            List<Server> serverList = super.getEligibleServers(servers, loadBalancerKey);
            if (serverList == null || serverList.size() == 0) {
                return serverList;
            }
            String regionPublish = CustomerContext.getCurrentCustomer().getRegionPublish();
            String greyPublish = CustomerContext.getCurrentCustomer().getGreyPublish();
            boolean isDevEnv = ENVIRONMENT_DEV.equals(currentEnv) ? true : false;
            List<Server> matchResults = Lists.newArrayList();
            Iterator<Server> serverIterator = serverList.iterator();
            while (serverIterator.hasNext()) {
                Server server = serverIterator.next();
                if (isDevEnv) {
                    // 开发环境，本地优先
                    String host = server.getHost();
                    List<String> localIpList = IPUtils.getNacosLocalIp();
                    if (localIpList.contains(host)) {
                        matchResults.add(server);
                        break;
                    }
                }
                if (server instanceof NacosServer) {
                    NacosServer nacosServer = (NacosServer) server;
                    if (!greyMatch(nacosServer, greyPublish)) {
                        continue;
                    }
                    if (!regionMatch(nacosServer, regionPublish)) {
                        continue;
                    }
                    matchResults.add(server);
                }
            }
            if (matchResults.size() == 0) {
                // 这一步慎用，范围内无节点时是否允许跨节点访问
                matchResults.addAll(serverList);
            }
            return matchResults;
        }

        private boolean regionMatch(NacosServer nacosServer, String regionPublish) {
            final Map<String, String> metadata = nacosServer.getInstance().getMetadata();
            String metaRegionPublish = metadata.get(ContextConstant.REGION_PUBLISH_FLAG);
            if (StringUtils.isNotBlank(metaRegionPublish)) {
                if (metaRegionPublish.equals(regionPublish)) {
                    return true;
                }
            } else {
                if (regionPublish == null) {
                    return true;
                }
            }
            return false;
        }

        private boolean greyMatch(NacosServer nacosServer, String greyPublish) {
            final Map<String, String> metadata = nacosServer.getInstance().getMetadata();
            String metaGreyPublish = metadata.get(ContextConstant.GREY_PUBLISH_FLAG);
            if (StringUtils.isNotBlank(metaGreyPublish)) {
                if (metaGreyPublish.equals(greyPublish)) {
                    return true;
                }
            } else {
                if (greyPublish == null) {
                    return true;
                }
            }
            return false;
        }

    }
}