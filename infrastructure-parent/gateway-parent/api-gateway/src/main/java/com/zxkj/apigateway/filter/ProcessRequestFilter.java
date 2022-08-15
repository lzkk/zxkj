package com.zxkj.apigateway.filter;

import com.zxkj.apigateway.permission.AuthorizationInterceptor;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.context.domain.ContextInfo;
import com.zxkj.common.exception.gateway.GatewayExceptionCodes;
import com.zxkj.apigateway.hot.HotQueue;
import com.zxkj.gateway.grey.GreyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
@RefreshScope
@Slf4j
public class ProcessRequestFilter extends BaseFilter implements GlobalFilter, Ordered {
    private static final String START_TIME = "startTime";

    @Autowired
    private HotQueue hotQueue;
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    @Value("${greyPublish:}")
    private String greyPublish;

    /***
     * 执行拦截处理
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().getPath();
        log.info("请求uri:{}", uri);
        //uri是否有效
        if (!authorizationInterceptor.isValid(uri)) {
            return error(exchange, 404, "url bad");
        }
        //uri是否需要拦截
        if (!authorizationInterceptor.isIntercept(exchange)) {
            return chain.filter(exchange);
        }
        //令牌校验
        Map<String, Object> resultMap = authorizationInterceptor.tokenIntercept(exchange);
        if (resultMap == null || !authorizationInterceptor.rolePermission(exchange, resultMap)) {
            //令牌校验失败 或者没有权限
            return error(exchange, 401, "Access denied");
        }
        boolean shouldFilter = super.shouldFilter(exchange);
        if (!shouldFilter) {
            return chain.filter(exchange);
        }
        //秒杀过滤
        if (uri.equals("/seckill/order")) {
            boolean isHot = secKillFilter(exchange, request, resultMap.get("username").toString());
            if (isHot) {
                return error(exchange, 0, "秒杀抢购处理中");
            }
        }
        HttpHeaders newHttpHeader = new HttpHeaders();
        newHttpHeader.putAll(exchange.getRequest().getHeaders());
        newHttpHeader.put(ContextConstant.TRACE_ID_FLAG, getEncodedString(uuid));
        try {
            if (HttpMethod.GET == request.getMethod()) {
                return doGet(exchange, chain, newHttpHeader);
            } else {
                return doPost(exchange, chain, newHttpHeader);
            }
        } catch (Exception e) {
            log.error("数据解密错误", e);
            return error(exchange, GatewayExceptionCodes.CODE_10000, "服务器内部错误");
        }
    }

    /**
     * Get请求处理
     *
     * @param exchange
     * @param chain
     * @param newHttpHeader
     * @return
     * @throws Exception
     */
    private Mono<Void> doGet(ServerWebExchange exchange, GatewayFilterChain chain, HttpHeaders newHttpHeader) throws Exception {
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        StringBuilder query = new StringBuilder();
        Iterator<String> it = queryParams.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = queryParams.getFirst(key);
            String encodeValue = URLEncoder.encode(value, "UTF-8");
            query.append("&").append(key).append("=").append(encodeValue);
        }
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);

            String json = new String(bytes, StandardCharsets.UTF_8);
            Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                DataBufferUtils.retain(buffer);
                return Mono.just(buffer);
            });
            log.info("get requestBody:{}", StringUtils.normalizeSpace(json));
            exchange.getAttributes().put("requestBody", json);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(newHttpHeader);
            String version = newHttpHeader.getFirst("version");
            String grp = newHttpHeader.getFirst("grp");
            ContextInfo contextInfo = GreyUtil.initContext(greyPublish, version, grp);
            headers.put(ContextConstant.REGION_PUBLISH_FLAG, getEncodedString(contextInfo.getRegionPublish()));
            headers.put(ContextConstant.GREY_PUBLISH_FLAG, getEncodedString(contextInfo.getGreyPublish()));
            headers.remove(HttpHeaders.CONTENT_LENGTH);
            ServerHttpRequest mutatedRequest = getServerHttpRequest(exchange, query, headers, cachedFlux);
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        });
    }

    /**
     * Post请求处理
     *
     * @param exchange
     * @param chain
     * @param newHttpHeader
     * @return
     * @throws IOException
     */
    private Mono doPost(ServerWebExchange exchange, GatewayFilterChain chain, HttpHeaders newHttpHeader) throws Exception {
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);

            String json = new String(bytes, StandardCharsets.UTF_8);
            Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                DataBufferUtils.retain(buffer);
                return Mono.just(buffer);
            });
            log.info("post requestBody:{}", StringUtils.normalizeSpace(json));
            exchange.getAttributes().put("requestBody", json);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(newHttpHeader);
            String version = newHttpHeader.getFirst("version");
            String grp = newHttpHeader.getFirst("grp");
            ContextInfo contextInfo = GreyUtil.initContext(greyPublish, version, grp);
            headers.put(ContextConstant.REGION_PUBLISH_FLAG, getEncodedString(contextInfo.getRegionPublish()));
            headers.put(ContextConstant.GREY_PUBLISH_FLAG, getEncodedString(contextInfo.getGreyPublish()));
            headers.remove(HttpHeaders.CONTENT_LENGTH);
            ServerHttpRequest mutatedRequest = postServerHttpRequest(exchange, headers, cachedFlux);
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        });
    }


    /***
     * 秒杀过滤
     * @param exchange
     * @param request
     * @param username
     * @return
     */
    private boolean secKillFilter(ServerWebExchange exchange, ServerHttpRequest request, String username) {
        //商品ID
        String id = request.getQueryParams().getFirst("id");
        //数量
        Integer num = Integer.valueOf(request.getQueryParams().getFirst("num"));

        //排队结果
        int result = hotQueue.hotToQueue(username, id, num);

        //QUEUE_ING、HAS_QUEUE
        if (result == HotQueue.QUEUE_ING || result == HotQueue.HAS_QUEUE) {
            return true;
        }
        return false;
    }

    private static List<String> getEncodedString(Object obj) {
        List<String> list = new ArrayList<>();
        if (Objects.isNull(obj)) {
            return list;
        }
        String str = obj.toString();
        try {
            String aa = URLEncoder.encode(str, "utf-8");
            list.add(aa);
        } catch (UnsupportedEncodingException e) {
        }
        return list;
    }

    @Override
    public int getOrder() {
        return -3;
    }
}
