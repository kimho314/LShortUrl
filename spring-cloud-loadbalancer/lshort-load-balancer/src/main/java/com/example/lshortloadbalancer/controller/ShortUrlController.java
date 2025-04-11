package com.example.lshortloadbalancer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class ShortUrlController {

    private final WebClient.Builder webClientBuilder;

    public ShortUrlController(Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @RequestMapping("/api/v1/{shortUrl}")
    public Mono<Void> getLongUrl(@PathVariable(value = "shortUrl") String shortUrl) {
        return webClientBuilder.build()
            .get()
            .uri("http://LShortUrl/api/v1/" + shortUrl)
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(Void.class);
    }
}
