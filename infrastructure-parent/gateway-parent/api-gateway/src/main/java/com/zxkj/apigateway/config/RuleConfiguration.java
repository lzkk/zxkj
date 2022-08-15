package com.zxkj.apigateway.config;

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
        Set<ApiDefinition> definitions = new HashSet<>();

        //创建每个Api，并配置相关规律
        ApiDefinition cartApi = new ApiDefinition("goods_api3")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/api-goods/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        //将创建好的Api添加到Api集合中
        definitions.add(cartApi);
        ApiDefinition cartApi2 = new ApiDefinition("order_api3")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/api-order/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        //将创建好的Api添加到Api集合中
        definitions.add(cartApi2);
        //手动加载Api到Sentinel
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }


    /***
     * 限流规则定义
     */
    public void initGatewayRules() {
        //创建集合存储所有规则
        Set<GatewayFlowRule> rules = new HashSet<>();

        //创建新的规则，并添加到集合中
        rules.add(new GatewayFlowRule("order_api3").setGrade(1)
                //请求的阈值
                .setCount(1)
                //统计时间窗口，单位：秒，默认为1秒
                .setIntervalSec(1));

        //创建新的规则，并添加到集合中
        rules.add(new GatewayFlowRule("goods_api3").setGrade(1)
                //请求的阈值
                .setCount(1)
                //统计时间窗口，单位：秒，默认为1秒
                .setIntervalSec(1));

        GatewayRuleManager.loadRules(rules);
    }

    /**
     * 系统规则定义
     */
    public void initSystemRules() {
        List<SystemRule> list1 = new ArrayList<>();
        SystemRule systemRule = new SystemRule();
        systemRule.setMaxThread(100);
        list1.add(systemRule);
        SystemRuleManager.loadRules(list1);
    }

}