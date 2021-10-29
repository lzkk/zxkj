package com.zxkj.gateway.filter;

import com.zxkj.gateway.hot.HotQueue;
import com.zxkj.gateway.permission.AuthorizationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Configuration
@Slf4j
public class ProcessRequestFilter extends BaseFilter implements GlobalFilter, Ordered {
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
        String uri = request.getURI().getPath();
        log.info("请求uri:{}", uri);
        //过滤uri是否有效
        if (!authorizationInterceptor.isValid(uri)) {
            return error(exchange, 404, "url bad");
        }

        //是否需要拦截
        if (!authorizationInterceptor.isIntercept(exchange)) {
            return chain.filter(exchange);
        }

        //令牌校验
        Map<String, Object> resultMap = authorizationInterceptor.tokenIntercept(exchange);
        if (resultMap == null || !authorizationInterceptor.rolePermission(exchange, resultMap)) {
            //令牌校验失败 或者没有权限
            return error(exchange, 401, "Access denied");
        }

        //秒杀过滤
        if (uri.equals("/seckill/order")) {
            boolean isHot = seckillFilter(exchange, request, resultMap.get("username").toString());
            if (isHot) {
                return error(exchange, 0, "success");
            }
        }
        //NOT_HOT 直接由后端服务处理
        return chain.filter(exchange);
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


    @Override
    public int getOrder() {
        return -3;
    }
}
