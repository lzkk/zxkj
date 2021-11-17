package com.zxkj.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class RuleConfiguration {

    /***
     * 规则和Api加载
     */
    @PostConstruct
    public void doInit() {
//        initCustomizedApis();
//        initGatewayRules();
//        initSystemRules();
    }

    /****
     * 定义Api组
     */
    private void initCustomizedApis() {
        //定义集合存储要定义的API组
        Set<ApiDefinition> definitions = new HashSet<ApiDefinition>();

        //创建每个Api，并配置相关规律
        ApiDefinition cartApi = new ApiDefinition("goods_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/api-goods/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        //将创建好的Api添加到Api集合中
        definitions.add(cartApi);
        //手动加载Api到Sentinel
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }


    /***
     * 限流规则定义
     */
    public void initGatewayRules() {
        //创建集合存储所有规则
        Set<GatewayFlowRule> rules = new HashSet<GatewayFlowRule>();

        //创建新的规则，并添加到集合中
        rules.add(new GatewayFlowRule("order_route")
                //请求的阈值
                .setCount(1)
                //突发流量额外允许并发数量
                .setBurst(1)
                //限流行为
                //CONTROL_BEHAVIOR_RATE_LIMITER  匀速排队
                //CONTROL_BEHAVIOR_DEFAULT  直接失败
                //.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
                //排队时间
                //.setMaxQueueingTimeoutMs(10000)
                //统计时间窗口，单位：秒，默认为1秒
                .setIntervalSec(1));

        //创建新的规则，并添加到集合中
        rules.add(new GatewayFlowRule("goods_api")
                //请求的阈值
                .setCount(1)
                //统计时间窗口，单位：秒，默认为1秒
                .setIntervalSec(1));
        //手动加载规则配置
        GatewayRuleManager.loadRules(rules);
    }

    public void initSystemRules() {
        List<SystemRule> list = new ArrayList<>();
        SystemRule systemRule = new SystemRule();
        systemRule.setMaxThread(100);
        list.add(systemRule);
        SystemRuleManager.loadRules(list);

    }

}