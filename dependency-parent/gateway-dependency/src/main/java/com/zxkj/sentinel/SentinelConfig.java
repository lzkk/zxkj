package com.zxkj.sentinel;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.custom.SentinelBeanPostProcessor;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.zxkj.common.util.NetUtil;
import com.zxkj.common.web.JsonUtil;
import com.zxkj.common.web.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Sentinel配置类
 *
 * @author yuhui
 */
@Slf4j
@Configuration
@ConditionalOnProperty({"feign.sentinel.enabled"})
public class SentinelConfig {

    @Autowired
    private Environment environment;
    @Autowired
    private NacosConfigManager nacosConfigManager;
    @Resource
    private GatewayProperties gatewayProperties;
    @Autowired
    private SentinelProperties properties;
    @Value("${server.port}")
    private Integer serverPort;

    private static final String DEFAULT_GROUP = "SENTINEL_GROUP";
    private static final String APPLICATION_NAME = "spring.application.name";
    private static final String DEGRADE_RULES_PREFIX = "degrade-rules-";
    private static final String DEGRADE_RULES_SUFFIX = ".json";

    private Class<?> clz = null;

    @Value("${sentinel.degradeRule.count:0.5}")
    private Double degradeRuleCount;

    @Value("${sentinel.degradeRule.timeWindow:5}")
    private Integer degradeRuleTimeWindow;

    // 降级规则(0:RT 1:EXCEPTION_RATIO 2:EXCEPTION_COUNT)
    @Value("${sentinel.degradeRule.grade:1}")
    private Integer degradeRuleGrade;

    @Value("${sentinel.degradeRule.limitApp:default}")
    private String degradeRuleLimitApp;

    /***
     * 限流输出定制
     */
    @PostConstruct
    public void initBlockHandlers() {
        BlockRequestHandler blockRequestHandler = (ServerWebExchange serverWebExchange, Throwable ex) -> {
            String msg = null;
            if (ex instanceof FlowException) {
                msg = "限流了";
            } else if (ex instanceof DegradeException) {
                msg = "降级了";
            } else if (ex instanceof ParamFlowException) {
                msg = "热点参数限流";
            } else if (ex instanceof SystemBlockException) {
                msg = "系统规则（负载/...不满足要求）";
            } else if (ex instanceof AuthorityException) {
                msg = "授权规则不通过";
            }
            String uri = serverWebExchange.getRequest().getURI().getPath();
            log.warn("uri-{},{}", uri, msg);
            return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(RespResult.error(msg)));
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

    @Bean
    @ConditionalOnClass(
            name = {"org.springframework.web.client.RestTemplate"}
    )
    @ConditionalOnProperty(
            name = {"resttemplate.sentinel.enabled"},
            havingValue = "true",
            matchIfMissing = true
    )
    public SentinelBeanPostProcessor sentinelBeanPostProcessor(ApplicationContext applicationContext) {
        int port = NetUtil.getFormatPort(String.valueOf(serverPort));
        String transportStr = System.getProperty("spring.cloud.sentinel.transport.port");
        if (!StringUtils.isEmpty(transportStr)) {
            port = Integer.parseInt(transportStr);
        }
        properties.getTransport().setPort(String.valueOf(NetUtil.findAvailablePort(port)));
        return new SentinelBeanPostProcessor(applicationContext);
    }

    @Bean(name = "degradeRuleInitializer")
    public ApplicationRunner degradeRuleInitializer() {
        return (ApplicationArguments args) -> {
            Class<?> mainClass = deduceMainApplicationName();
            clz = mainClass;
            invoke(mainClass);
        };
    }

    /**
     * 获得服务运行主类
     *
     * @return
     */
    private Class<?> deduceMainApplicationName() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void invoke(Class<?> mainClass) {
        log.info("开始加载默认降级规则===========================");
        if (mainClass == null) {
            throw new RuntimeException("没有发现主类");
        }
        List<RouteDefinition> routeDefinitionList = gatewayProperties.getRoutes();
        // 初始化降级规则
        initDegradeRule(routeDefinitionList);
        log.info("===========================加载默认降级规则完成");
    }

    /**
     * 初始化降级规则
     *
     * @param routeDefinitionList
     */
    private void initDegradeRule(List<RouteDefinition> routeDefinitionList) {
        List<DegradeRule> degradeRuleList = new ArrayList<>();
        for (RouteDefinition routeDefinition : routeDefinitionList) {
            List<DegradeRule> tmpList = initRules(routeDefinition);
            degradeRuleList.addAll(tmpList);
        }
        pushRules(degradeRuleList);
    }

    /**
     * 自动生成降级规则列表
     *
     * @param routeDefinition
     * @return
     */
    private List<DegradeRule> initRules(RouteDefinition routeDefinition) {
        List<DegradeRule> degradeRuleList = new ArrayList<>();
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource(routeDefinition.getId());
        degradeRule.setLimitApp(degradeRuleLimitApp);
        degradeRule.setGrade(degradeRuleGrade);
        degradeRule.setCount(degradeRuleCount);
        degradeRule.setTimeWindow(degradeRuleTimeWindow);
        degradeRuleList.add(degradeRule);
        return degradeRuleList;
    }

    /**
     * 把降级规则推送至NACOS
     *
     * @param degradeRuleList
     */
    private void pushRules(List<DegradeRule> degradeRuleList) {
        try {
            String customJson = fetchNacosData(true);
            if (StringUtils.isNotBlank(customJson)) {
                List<DegradeRule> customDegradeRuleList = JSON.parseArray(customJson, DegradeRule.class);
                pushRules(degradeRuleList, customDegradeRuleList);
            } else {
                pushRules(degradeRuleList, null);
            }
        } catch (Exception e) {
            log.error("NACOS客户端推送数据失败", e);
        }
    }

    /**
     * 把降级规则推送至NACOS
     *
     * @param degradeRuleList
     */
    private void pushRules(List<DegradeRule> degradeRuleList, List<DegradeRule> customDegradeRuleList) {
        try {
            Map<String, DegradeRule> normalMap = new HashMap<>();
            if (degradeRuleList != null && degradeRuleList.size() > 0) {
                for (DegradeRule degradeRule : degradeRuleList) {
                    String resourceName = degradeRule.getResource();
                    normalMap.put(resourceName, degradeRule);
                }
            }

            if (customDegradeRuleList != null && customDegradeRuleList.size() > 0) {
                for (DegradeRule degradeRule : customDegradeRuleList) {
                    String resourceName = degradeRule.getResource();
                    if (!normalMap.containsKey(resourceName)) {
                        normalMap.put(resourceName, degradeRule);
                    } else {
                        DegradeRule degradeRuleNormal = normalMap.get(resourceName);
                        degradeRuleNormal.setLimitApp(degradeRule.getLimitApp());
                        degradeRuleNormal.setTimeWindow(degradeRule.getTimeWindow());
                        degradeRuleNormal.setCount(degradeRule.getCount());
                        degradeRuleNormal.setGrade(degradeRule.getGrade());
                        degradeRuleNormal.setMinRequestAmount(degradeRule.getMinRequestAmount());
                        degradeRuleNormal.setRtSlowRequestAmount(degradeRule.getRtSlowRequestAmount());
                    }
                }
            }
            String degradeRuleJson = JsonUtil.toJsonString(normalMap.values());
            ConfigService configService = nacosConfigManager.getConfigService();
            String applicationName = environment.getProperty(APPLICATION_NAME);
            String dataId = DEGRADE_RULES_PREFIX + applicationName + DEGRADE_RULES_SUFFIX;
            boolean isPublishOk = configService.publishConfig(dataId, DEFAULT_GROUP, degradeRuleJson);
            log.info("dataId:{},publishOk:{}", dataId, isPublishOk);
        } catch (NacosException e) {
            log.error("NACOS客户端推送数据失败", e);
        }
    }

    private String fetchNacosData(boolean isCustom) {
        String json = null;
        try {
            String customStr = "";
            if (isCustom) {
                customStr = "-custom";
            }
            String applicationName = environment.getProperty(APPLICATION_NAME);
            String dataId = DEGRADE_RULES_PREFIX + applicationName + customStr + DEGRADE_RULES_SUFFIX;
            json = nacosConfigManager.getConfigService().getConfig(dataId, DEFAULT_GROUP, 5000);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return json;
    }


    @PostConstruct
    public void monitorDegradeCustom() {
        ConfigService configService = nacosConfigManager.getConfigService();
        String applicationName = environment.getProperty(APPLICATION_NAME);
        String customDataId = DEGRADE_RULES_PREFIX + applicationName + "-custom" + DEGRADE_RULES_SUFFIX;
        try {
            configService.addListener(customDataId, DEFAULT_GROUP, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("changeContent:" + configInfo);
                    invoke(clz);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }


}

