package com.example.lshorturl.controller;

import com.example.lshorturl.dto.SaveShortenUrlRequestDto;
import com.example.lshorturl.dto.SaveShortenUrlResponseDto;
import com.example.lshorturl.service.ShortUrlServiceV1;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ShortUrlControllerV1 {

    private final ShortUrlServiceV1 shortUrlServiceV1;

    /**
     * @title 단축 url 생성
     */
    @PostMapping("/data/shorten")
    public ResponseEntity<SaveShortenUrlResponseDto> saveShortenUrl(@RequestBody SaveShortenUrlRequestDto request) {
        SaveShortenUrlResponseDto shortenUrl = shortUrlServiceV1.shortenUrl(request);
        return ResponseEntity.ok(shortenUrl);
    }

    /**
     * @title url redirect
     */
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> getLongUrl(@PathVariable(value = "shortUrl") String shortUrl) {
        String longUrl = shortUrlServiceV1.getLongUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
            .location(URI.create(longUrl))
            .build();
    }
}
