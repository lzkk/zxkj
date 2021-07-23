package com.zxkj.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxkj.common.exception.BusinessException;
import com.zxkj.gateway.hot.HotQueue;
import com.zxkj.gateway.permission.AuthorizationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class ProcessRequestFilter implements GlobalFilter, Ordered {
    private static final String START_TIME = "startTime";

    @Autowired
    private HotQueue hotQueue;

    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    /***
     * 执行拦截处理
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        //uri
        String uri = request.getURI().getPath();

        //过滤uri是否有效
        if (!authorizationInterceptor.isValid(uri)) {
            return errorInfo(exchange, 404, "url bad");
        }

        //是否需要拦截
        if (!authorizationInterceptor.isIntercept(exchange)) {
            return chain.filter(exchange);
        }

        //令牌校验
        Map<String, Object> resultMap = authorizationInterceptor.tokenIntercept(exchange);
        if (resultMap == null || !authorizationInterceptor.rolePermission(exchange, resultMap)) {
            //令牌校验失败 或者没有权限
            return errorInfo(exchange, 401, "Access denied");
        }

        //秒杀过滤
        if (uri.equals("/seckill/order")) {
            boolean isHot = seckillFilter(exchange, request, resultMap.get("username").toString());
            if (isHot) {
                return errorInfo(exchange, 0, "success");
            }
        }
        //NOT_HOT 直接由后端服务处理
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                Long executeTime = (System.currentTimeMillis() - startTime);
                log.info(exchange.getRequest().getURI().getRawPath() + " : " + executeTime + "ms");
            }
        }));
    }

    /***
     * 秒杀过滤
     * @param exchange
     * @param request
     * @param username
     * @return
     */
    private boolean seckillFilter(ServerWebExchange exchange, ServerHttpRequest request, String username) {
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

    /**
     * 返回response
     *
     * @param exchange
     * @param status   data中的status
     * @param message  异常信息
     * @return
     */
    public static Mono<Void> errorInfo(ServerWebExchange exchange, Integer status, String message) {
        // 自定义返回格式
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("returnCode", status);
        resultMap.put("message", message);
        resultMap.put("result", null);
        return Mono.defer(() -> {
            byte[] bytes;
            try {
                bytes = new ObjectMapper().writeValueAsBytes(resultMap);
            } catch (JsonProcessingException e) {
                throw new BusinessException("信息序列化异常");
            } catch (Exception e) {
                throw new BusinessException("写入响应异常");
            }
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString());
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        });
    }

    @Override
    public int getOrder() {
        return -3;
    }
}
