package com.zxkj.ribbon;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.netflix.loadbalancer.*;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.util.NetUtil;
import com.zxkj.grey.GreyContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

import java.util.*;

@Slf4j
public class MyZoneAvoidanceRule extends ZoneAvoidanceRule {
    private volatile boolean fetchFlag = false;
    private volatile List<Server> localServerList = new ArrayList<>();
    private static final String ENVIRONMENT_DEV = "dev";
    private AbstractServerPredicate predicate;

    @Value("${spring.profiles.active}")
    private String currentEnv;
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddress;
    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;

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
            if (localServerList.hashCode() != serverList.hashCode()) {
                log.info("node:{} current nodes:{}", clientName, serverList);
                localServerList = serverList;
            }
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
            MyServerListUpdater serverListUpdater = (MyServerListUpdater) lb.getServerListUpdater();
            if (serverListUpdater != null) {
                serverListUpdater.getUpdateAction().doUpdate();
            }
            fetchFlag = true;
        }
        subscribe(lb, clientName);
        return lb.getAllServers();
    }

    private void subscribe(ZoneAwareLoadBalancer lb, String clientName) {
        try {
            NamingService naming = NamingFactory.createNamingService(getNacosProperties());
            naming.subscribe(clientName, event -> {
                if (event instanceof NamingEvent) {
                    MyServerListUpdater serverListUpdater = (MyServerListUpdater) lb.getServerListUpdater();
                    if (serverListUpdater != null) {
                        serverListUpdater.getUpdateAction().doUpdate();
                        log.info("node:{} change over!nodes:{}", clientName, lb.getAllServers());
                    }
                }
            });
        } catch (NacosException e) {
            log.error("订阅NACOS出错", e);
        }
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
            String regionPublish = GreyContext.getCurrentContext().getRegionPublish();
            String greyPublish = GreyContext.getCurrentContext().getGreyPublish();
            boolean isDevEnv = ENVIRONMENT_DEV.equals(currentEnv);
            List<Server> matchResults = Lists.newArrayList();
            Iterator<Server> serverIterator = serverList.iterator();
            while (serverIterator.hasNext()) {
                Server server = serverIterator.next();
                if (isDevEnv) {
                    // 开发环境，本地优先
                    String host = server.getHost();
                    List<String> localIpList = getNacosLocalIp();
                    if (localIpList.contains(host)) {
                        matchResults.add(server);
                        break;
                    }
                }
                if (server instanceof NacosServer) {
                    NacosServer nacosServer = (NacosServer) server;
                    if (greyMatch(nacosServer, greyPublish, regionPublish)) {
                        matchResults.add(server);
                    }
                }
            }
            if (matchResults.size() == 0) {
                // 这一步慎用，范围内无节点时是否允许跨节点访问
                matchResults.addAll(serverList);
            }
            return matchResults;
        }

        private boolean greyMatch(NacosServer nacosServer, String greyPublish, String regionPublish) {
            final Map<String, String> metadata = nacosServer.getInstance().getMetadata();
            String metaGreyPublish = metadata.get(ContextConstant.GREY_PUBLISH_FLAG);
            String metaRegionPublish = metadata.get(ContextConstant.REGION_PUBLISH_FLAG);
            String reqGrey = greyPublish + "-" + regionPublish;
            String metaGrey = metaGreyPublish + "-" + metaRegionPublish;
            if (reqGrey.equals(metaGrey)) {
                return true;
            }
            return false;
        }

        private List<String> getNacosLocalIp() {
            String ip = NetUtil.getLocalIp();
            return Collections.singletonList(ip);
        }

    }
}