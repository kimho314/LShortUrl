package com.example.lshorturl.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.lshorturl.dto.SaveShortenUrlRequestDto;
import com.example.lshorturl.dto.SaveShortenUrlResponseDto;
import com.example.lshorturl.entity.ShortenUrl;
import com.example.lshorturl.repository.ShortUrlRepository;
import com.example.lshorturl.utils.DataSourceUtil;
import com.example.lshorturl.utils.ShortenUrlGenerator;
import com.zaxxer.hikari.HikariDataSource;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ShortUrlServiceV1 {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortenUrlGenerator shortenUrlGenerator;
    private final DataSourceUtil dataSourceUtil;
    private final RedisTemplate redisTemplate;

    public ShortUrlServiceV1(
        ShortenUrlGenerator shortenUrlGenerator,
        ShortUrlRepository shortUrlRepository,
        DataSourceUtil dataSourceUtil,
        RedisTemplate redisTemplate
    ) {
        this.shortenUrlGenerator = shortenUrlGenerator;
        this.shortUrlRepository = shortUrlRepository;
        this.dataSourceUtil = dataSourceUtil;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public SaveShortenUrlResponseDto shortenUrl(SaveShortenUrlRequestDto request) {
        dataSourceUtil.log();
        Optional<ShortenUrl> maybeShortenUrl = shortUrlRepository.findByLongUrl(request.longUrl());
        if(maybeShortenUrl.isPresent()) {
            return new SaveShortenUrlResponseDto(maybeShortenUrl.get().getShortUrl());
        }
        // 1. 단축 url 생성
        String uniqueId = NanoIdUtils.randomNanoId();
        String shortenedUrl = shortenUrlGenerator.generate(uniqueId, request.longUrl());

        // 2. long_url, short_url db에 저장
        ShortenUrl shortenUrl = new ShortenUrl(uniqueId, shortenedUrl, request.longUrl());
        shortUrlRepository.save(shortenUrl);

        return new SaveShortenUrlResponseDto(shortenedUrl);
    }

    @Transactional(readOnly = true)
    public String getLongUrl(String shortUrl) {
        dataSourceUtil.log();
        ShortenUrl shortenUrl = shortUrlRepository.findByShortUrl(shortUrl)
            .orElseThrow(() -> new NoSuchElementException("ShortUrl not found"));

        return shortenUrl.getLongUrl();
    }
}
