package com.example.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class GreetingController {
    private final WebClient.Builder loadBalancedWebClietnBuilder;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    public GreetingController(ReactorLoadBalancerExchangeFilterFunction lbFunction, Builder loadBalancedWebClietnBuilder) {
        this.lbFunction = lbFunction;
        this.loadBalancedWebClietnBuilder = loadBalancedWebClietnBuilder;
    }

    @RequestMapping("/hi")
    public Mono<String> hi(@RequestParam(value = "name", defaultValue = "Mary") String name){
        return loadBalancedWebClietnBuilder.build().get()
            .uri("http://say-hello/greeting")
            .retrieve()
            .bodyToMono(String.class)
            .map(greeting -> String.format("%s, %s!", greeting, name));
    }

    @RequestMapping("/hello")
    public Mono<String> hello(@RequestParam(value = "name", defaultValue = "John") String name){
        return WebClient.builder()
            .filter(lbFunction)
            .build()
            .get()
            .uri("http://say-hello/greeting")
            .retrieve()
            .bodyToMono(String.class)
            .map(greeting -> String.format("%s, %s!", greeting, name));
    }
}
