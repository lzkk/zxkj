package com.zxkj;

import com.zxkj.common.web.JsonUtil;
import com.zxkj.common.web.RespResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author ：yuhui
 * @date ：Created in 2022/1/18 10:50
 */
public class TestWebClient {

    public final static String endpoint = "http://127.0.0.1:5053";
    private final static String PATH = "/sku/1318596430360813570";

//    @Bean
//    public WebClient sampleWebClient(WebClient.Builder builder) {
//        return builder.uriBuilderFactory(new DefaultUriBuilderFactory(WebClientController.endpoint)).build();
//    }

    private static WebClient webClient;

    static {
        webClient = WebClient.create(endpoint);
    }

    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        Mono<RespResult> resp = webClient.method(HttpMethod.GET).uri(PATH).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve().bodyToMono(RespResult.class);
        RespResult respResult = resp.block();
        System.out.println((System.currentTimeMillis() - now) + ",result:" + JsonUtil.toJsonString(respResult));

        long now2 = System.currentTimeMillis();
        Mono<RespResult> resp2 = webClient.method(HttpMethod.GET).uri(PATH).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve().bodyToMono(RespResult.class);
        RespResult respResult2 = resp2.block();
        System.out.println((System.currentTimeMillis() - now2) + ",result2:" + JsonUtil.toJsonString(respResult2));

        long now3 = System.currentTimeMillis();
        Mono<RespResult> resp3 = webClient.method(HttpMethod.GET).uri(PATH).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve().bodyToMono(RespResult.class);
        RespResult respResult3 = resp3.block();
        System.out.println((System.currentTimeMillis() - now3) + ",result3:" + JsonUtil.toJsonString(respResult3));
    }

}
