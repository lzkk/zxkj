package com.zxkj.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/requestPost")
public class WebClientController {

    public final static String endpoint = "http://goods-service";
    private final static String PATH = "/sku/1318596430360813570";

    @Autowired
    private WebClient webClient;

    @GetMapping
    public Mono<String> trigger() {
        int count = 1;
        List list = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            list.add(i);
        }
        return Flux.fromStream(list.stream())
                .flatMap(id -> webClient.get().uri(PATH, System.currentTimeMillis()).exchange())
                .collectList()
                .map(results -> "success");
    }
}