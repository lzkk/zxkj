package com.zxkj.gateway.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.zxkj.common.web.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.PostConstruct;

/**
 * Sentinel配置类
 *
 * @author yuhui
 */
@Slf4j
@Configuration
public class SentinelConfig {

    /***
     * 限流输出定制
     */
    @PostConstruct
    public void initBlockHandlers() {
        BlockRequestHandler blockRequestHandler = (ServerWebExchange serverWebExchange, Throwable throwable) -> {
            String uri = serverWebExchange.getRequest().getURI().getPath();
            log.warn("uri:{}，接口限流了!", uri);
            String errMsg = "系统繁忙，请稍后再试！";
            return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(RespResult.error(errMsg)));
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

}

