package com.example.lshorturl.controller;

import com.example.lshorturl.dto.SaveShortenUrlRequestDto;
import com.example.lshorturl.dto.SaveShortenUrlResponseDto;
import com.example.lshorturl.service.ShortUrlServiceV1;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class ShortUrlControllerV1 {
    private final ShortUrlServiceV1 shortUrlServiceV1;

    @PostMapping("/shortens")
    public ResponseEntity<SaveShortenUrlResponseDto> saveShortenUrl(@RequestBody SaveShortenUrlRequestDto request){
        SaveShortenUrlResponseDto shortenUrl = shortUrlServiceV1.shortenUrl(request);
        return ResponseEntity.created(URI.create("")).body(shortenUrl);
    }

    @GetMapping("/shortens/{shortUrl}")
    public ResponseEntity<Void> getLongUrl(@PathVariable String shortUrl){
        String longUrl = shortUrlServiceV1.getLongUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
            .header("Location", longUrl)
            .build();
    }
}
