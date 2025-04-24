package com.example.lshortloadbalancer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/api/v1/{shortUrl}")
    public Mono<ResponseEntity<String>> getLongUrl(@PathVariable(value = "shortUrl") String shortUrl) {
        return webClientBuilder.build()
            .get()
            .uri("http://LShortUrl/api/v1/" + shortUrl)
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.toEntity(String.class);
                } else {
                    return response.createError();
                }
            });
    }
}
