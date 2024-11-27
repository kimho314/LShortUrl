package com.example.lshorturl.controller;

import com.example.lshorturl.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class ShortUrlControllerV1 {
    private final ShortUrlService shortUrlService;

    @PostMapping("/shortens")
    public ResponseEntity<Void> saveShortenUrl(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shortens/{shortUrl}")
    public ResponseEntity<String> getShortenUrl(@PathVariable String shortUrl){
        String longUrl = "";
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
            .header("Location", longUrl)
            .build();
    }
}
