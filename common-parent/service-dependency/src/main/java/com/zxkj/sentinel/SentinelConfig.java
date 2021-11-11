package com.zxkj.sentinel;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
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
import com.zxkj.common.util.ClassScanUtil;
import com.zxkj.common.web.JsonUtil;
import com.zxkj.common.web.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Sentinel配置类
 *
 * @author yuhui
 */
@Configuration
@Slf4j
@ConditionalOnProperty({"feign.sentinel.enabled"})
public class SentinelConfig {

    @Autowired
    private Environment environment;
    @Autowired
    private NacosConfigManager nacosConfigManager;

    private static final String RESOURCE_PATTERN = "%s/**/*.class";
    private static final String REQUEST_PROTOCAL = "http://";
    private static final String DEFAULT_GROUP = "SENTINEL_GROUP";
    private static final String APPLICATION_NAME = "spring.application.name";
    private static final String DEGRADE_RULES_PREFIX = "degrade-rules-";
    private static final String DEGRADE_RULES_SUFFIX = ".json";

    private static final String REQUEST_METHOD_GET = "GET:";
    private static final String REQUEST_METHOD_POST = "POST:";
    private static final String REQUEST_METHOD_PUT = "PUT:";
    private static final String REQUEST_METHOD_DELETE = "DELETE:";

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

    /**
     * 异常处理定制化
     *
     * @return
     */
    @Bean
    public BlockExceptionHandler customUrlBlockHandler() {
        return (httpServletRequest, httpServletResponse, ex) -> {
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpServletResponse.setContentType("application/json;charset=utf-8");
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
            String json = JsonUtil.jsonFromObject(RespResult.error(msg));
            httpServletResponse.getWriter().write(json);
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();
        };
    }

    @Bean(name = "degradeRuleInitializer")
    public ApplicationRunner degradeRuleInitializer() {
        return (ApplicationArguments args) -> {
            Class<?> mainClass = deduceMainApplicationName();
            clz = mainClass;
            invoke(mainClass);
        };
    }

    private void invoke(Class<?> mainClass) {
        log.info("开始加载默认降级规则===========================");
        if (mainClass == null) {
            throw new RuntimeException("没有发现主类");
        }
        EnableFeignClients enableFeignClients = mainClass.getAnnotation(EnableFeignClients.class);
        String[] feignClientPackages = null;
        if (enableFeignClients != null) {
            String[] feignClientDeclaredPackages = enableFeignClients.basePackages();
            if (feignClientDeclaredPackages.length == 0) {
                feignClientPackages = new String[]{mainClass.getPackage().getName()};
            } else {
                feignClientPackages = feignClientDeclaredPackages;
            }
        }
        // 初始化降级规则
        initDegradeRule(feignClientPackages);

        log.info("===========================加载默认降级规则完成");
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

    /**
     * 初始化降级规则
     *
     * @param feignClientPackages
     */
    private void initDegradeRule(String[] feignClientPackages) {
        List<DegradeRule> degradeRuleList = new ArrayList<>();
        Set<Class> feignClientClazz = getFeignClientClass(feignClientPackages);
        for (Class clz : feignClientClazz) {
            List<DegradeRule> tmpList = initRules(clz);
            degradeRuleList.addAll(tmpList);
        }
        pushRules(degradeRuleList);
    }

    /**
     * 根据扫码包遍历FeignClient的所有类
     *
     * @param feignClientPackages
     * @return
     */
    private Set<Class> getFeignClientClass(String[] feignClientPackages) {
        Set<Class> feignClientClassSet = new HashSet<>();
        for (String packageName : feignClientPackages) {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + String.format(RESOURCE_PATTERN, ClassUtils.convertClassNameToResourcePath(packageName));
            feignClientClassSet.addAll(ClassScanUtil.scan(pattern, FeignClient.class));
        }
        return feignClientClassSet;
    }

    /**
     * 根据FeignClient类方法自动生成降级规则列表
     *
     * @param clz
     * @return
     */
    private List<DegradeRule> initRules(Class<?> clz) {
        FeignClient feignClient = clz.getAnnotation(FeignClient.class);
        String feignClientUrl = feignClient.url();
        String feignClientServiceName = feignClient.value();
        String requestHost = "";
        if (StringUtils.isNotBlank(feignClientUrl)) {
            requestHost = feignClientUrl;
        } else {
            requestHost = feignClientServiceName;
        }
        if (!requestHost.startsWith(REQUEST_PROTOCAL)) {
            requestHost = REQUEST_PROTOCAL + requestHost;
        }
        List<DegradeRule> degradeRuleList = new ArrayList<>();
        List<String> resourceNameList = new ArrayList<>();
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            if (annotations == null || annotations.length == 0) {
                continue;
            }
            String requestMethod = "";
            String methodValue = "";
            for (Annotation annotation : annotations) {
                Class<?> clazz = annotation.annotationType();
                if (clazz.isAssignableFrom(PostMapping.class)) {
                    requestMethod = REQUEST_METHOD_POST;
                    methodValue = ((PostMapping) annotation).value()[0];
                } else if (clazz.isAssignableFrom(GetMapping.class)) {
                    requestMethod = REQUEST_METHOD_GET;
                    methodValue = ((GetMapping) annotation).value()[0];
                } else if (clazz.isAssignableFrom(RequestMapping.class)) {
                    requestMethod = REQUEST_METHOD_GET;
                    methodValue = ((RequestMapping) annotation).value()[0];
                } else if (clazz.isAssignableFrom(PutMapping.class)) {
                    requestMethod = REQUEST_METHOD_PUT;
                    methodValue = ((PutMapping) annotation).value()[0];
                } else if (clazz.isAssignableFrom(DeleteMapping.class)) {
                    requestMethod = REQUEST_METHOD_DELETE;
                    methodValue = ((DeleteMapping) annotation).value()[0];
                } else {
                    continue;
                }
            }
            String resourceName = requestMethod + requestHost + methodValue;
            resourceNameList.add(resourceName);
        }
        for (String resourceName : resourceNameList) {
            DegradeRule degradeRule = new DegradeRule();
            degradeRule.setResource(resourceName);
            degradeRule.setLimitApp(degradeRuleLimitApp);
            degradeRule.setGrade(degradeRuleGrade);
            degradeRule.setCount(degradeRuleCount);
            degradeRule.setTimeWindow(degradeRuleTimeWindow);
            degradeRuleList.add(degradeRule);
        }
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
            String degradeRuleJson = JsonUtil.jsonFromObject(normalMap.values());
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

