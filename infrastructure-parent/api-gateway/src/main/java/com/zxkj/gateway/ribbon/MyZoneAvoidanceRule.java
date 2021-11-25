package com.zxkj.gateway.ribbon;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.google.common.collect.Lists;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.zxkj.common.context.CustomerContext;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.util.RegionPublishUtil;
import com.zxkj.common.util.ip.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

import java.util.*;

public class MyZoneAvoidanceRule extends ZoneAvoidanceRule {
    private AbstractServerPredicate predicate;

    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationArguments applicationArguments;

    public MyZoneAvoidanceRule() {
        super();
        this.predicate = new MyServerPredicate(super.getPredicate());
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return this.predicate;
    }

    private class MyServerPredicate extends AbstractServerPredicate {
        private AbstractServerPredicate myPredicate;
        private static final String CURRENT_ENVIRONMENT_PRE = "spring.profiles.active";
        private static final String ENVIRONMENT_DEV = "dev";

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
            boolean regionPublish = RegionPublishUtil.isRegionPublish(applicationArguments.getSourceArgs());
            String activeEnv = environment.getProperty(CURRENT_ENVIRONMENT_PRE);
            List<Server> matchResults = Lists.newArrayList();
            Iterator var4 = serverList.iterator();
            while (var4.hasNext()) {
                Server server = (Server) var4.next();
                if (ENVIRONMENT_DEV.equals(activeEnv)) {
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
                    if (!regionMatch(nacosServer, regionPublish)) {
                        continue;
                    }
                    matchResults.add(server);
                }
            }
            if (matchResults.size() == 0) {
                // 这一步慎用，本区域无节点时是否允许跨节点访问
                 matchResults.addAll(serverList);
            }
            return matchResults;
        }

        private boolean regionMatch(NacosServer nacosServer, boolean regionPublish) {
            Map<String, String> tmpMap = new HashMap<>();
            tmpMap.put(ContextConstant.REGION_PUBLISH, String.valueOf(regionPublish));
            final Set<Map.Entry<String, String>> attributes = Collections.unmodifiableSet(tmpMap.entrySet());
            final Map<String, String> metadata = nacosServer.getInstance().getMetadata();
            return metadata.entrySet().containsAll(attributes);
        }

    }
}