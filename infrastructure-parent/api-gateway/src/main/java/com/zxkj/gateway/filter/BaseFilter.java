package com.zxkj.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxkj.common.exception.BusinessException;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuhui
 */
public abstract class BaseFilter {
    private static final Logger log = LoggerFactory.getLogger(BaseFilter.class);

    /**
     * 返回response
     *
     * @param exchange
     * @param status   data中的status
     * @param message  异常信息
     * @return
     */
    public static Mono<Void> error(ServerWebExchange exchange, Integer status, String message) {
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

    public ServerHttpResponseDecorator responseDecorator(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffer -> {
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffer);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        //释放掉内存
                        DataBufferUtils.release(join);
                        String responseResult = new String(content, StandardCharsets.UTF_8);
                        long startTimeObj = (long) exchange.getAttributes().get("startTime");
                        log.info("response plain:{},spend:{}", responseResult, System.currentTimeMillis() - startTimeObj);
                        content = new String(responseResult.getBytes(), StandardCharsets.UTF_8).getBytes();
                        return bufferFactory.wrap(content);
                    }));
                }
                return super.writeWith(body);
            }
        };
        return decoratedResponse;
    }

}
