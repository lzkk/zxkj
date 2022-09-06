package com.zxkj.service.ribbon;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.netflix.loadbalancer.*;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.util.net.NetUtil;
import com.zxkj.service.grey.GreyContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

import java.util.*;

@Slf4j
public class MyZoneAvoidanceRule extends ZoneAvoidanceRule {
    private volatile boolean fetchFlag = false;
    private volatile List<Server> localServerList = new ArrayList<>();
    private static final String ENVIRONMENT_DEV = "dev";
    private static final String ENV_ONLINE = "null-null";
    private AbstractServerPredicate predicate;
    private String clientName;

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
            if (this.clientName == null) {
                this.clientName = clientName;
            }
            serverList = getServerListByClientName(zoneAwareLoadBalancer, clientName);
            if (localServerList.hashCode() != serverList.hashCode()) {
                log.info("node:{} current serverList:{}", clientName, serverList);
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
            NamingService naming = NacosNamingUtil.getNamingService(getNacosProperties());
            naming.subscribe(clientName, event -> {
                if (event instanceof NamingEvent) {
                    MyServerListUpdater serverListUpdater = (MyServerListUpdater) lb.getServerListUpdater();
                    if (serverListUpdater != null) {
                        serverListUpdater.getUpdateAction().doUpdate();
                        log.info("node:{} change over!serverList:{}", clientName, lb.getAllServers());
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
            String regionPublishStr = GreyContext.getCurrentContext().getRegionPublish();
            String greyPublishStr = GreyContext.getCurrentContext().getGreyPublish();
            List<Server> matchResults = Lists.newArrayList();
            for (Server server : serverList) {
                if (server instanceof NacosServer) {
                    NacosServer nacosServer = (NacosServer) server;
                    if (developEnvMatch(server) && greyMatch(nacosServer, regionPublishStr, greyPublishStr)) {
                        log.info("reqClientName:{},matchResult:{}", clientName, nacosServer.getHostPort());
                        matchResults.add(server);
                    }
                }
            }
            if (matchResults.size() == 0) {
                // 这一步慎用，范围内无节点时是否允许跨节点访问
                return compatibleMatch(serverList, regionPublishStr, greyPublishStr);
            }
            return matchResults;
        }

        /**
         * 灰度/线上路由完全匹配
         *
         * @param nacosServer
         * @param regionPublishStr
         * @param greyPublishStr
         * @return
         */
        private boolean greyMatch(NacosServer nacosServer, String regionPublishStr, String greyPublishStr) {
            final Map<String, String> metadata = nacosServer.getInstance().getMetadata();
            String metaGreyPublish = metadata.get(ContextConstant.GREY_PUBLISH_FLAG);
            String metaRegionPublish = metadata.get(ContextConstant.REGION_PUBLISH_FLAG);
            String reqGrey = regionPublishStr + "-" + greyPublishStr;
            String metaGrey = metaGreyPublish + "-" + metaRegionPublish;
            return reqGrey.equals(metaGrey);
        }

        /**
         * 多灰度环境，灰度请求若未完全匹配则线上服务兜底
         *
         * @param serverList
         * @param regionPublishStr
         * @param greyPublishStr
         * @return
         */
        private List<Server> compatibleMatch(List<Server> serverList, String regionPublishStr, String greyPublishStr) {
            List<Server> compatibleList = new ArrayList<>();
            String reqGrey = regionPublishStr + "-" + greyPublishStr;
            for (Server server : serverList) {
                if (server instanceof NacosServer) {
                    NacosServer nacosServer = (NacosServer) server;
                    Map<String, String> metadata = nacosServer.getInstance().getMetadata();
                    String metaGreyPublish = metadata.get(ContextConstant.GREY_PUBLISH_FLAG);
                    String metaRegionPublish = metadata.get(ContextConstant.REGION_PUBLISH_FLAG);
                    String metaGrey = metaGreyPublish + "-" + metaRegionPublish;
                    if (!ENV_ONLINE.equals(reqGrey) && ENV_ONLINE.equals(metaGrey)) {
                        compatibleList.add(server);
                    }
                }
            }
            if (compatibleList.size() > 0) {
                log.info("reqClientName:{},matchCompatible", clientName);
                return compatibleList;
            } else {
                log.info("reqClientName:{},matchAll", clientName);
                return serverList;
            }
        }

        private boolean developEnvMatch(Server server) {
            if (ENVIRONMENT_DEV.equals(currentEnv)) {
                // 开发环境，本地优先
                return getNacosLocalIp().contains(server.getHost());
            }
            return true;
        }

        private List<String> getNacosLocalIp() {
            String ip = NetUtil.getLocalIp();
            return Collections.singletonList(ip);
        }

    }
}