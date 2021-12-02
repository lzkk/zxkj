package com.zxkj.ribbon;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.google.common.collect.Lists;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.zxkj.common.context.CustomerContext;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.util.ip.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class MyZoneAvoidanceRule extends ZoneAvoidanceRule {
    private static final String ENVIRONMENT_DEV = "dev";
    private AbstractServerPredicate predicate;

    @Value("${spring.profiles.active}")
    private String currentEnv;

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