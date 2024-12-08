package com.example.lshorturl.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.lshorturl.dto.SaveShortenUrlRequestDto;
import com.example.lshorturl.dto.SaveShortenUrlResponseDto;
import com.example.lshorturl.entity.ShortenUrl;
import com.example.lshorturl.repository.ShortUrlRepository;
import com.example.lshorturl.utils.ShortenUrlGenerator;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShortUrlServiceV1 {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortenUrlGenerator shortenUrlGenerator;

    public ShortUrlServiceV1(ShortenUrlGenerator shortenUrlGenerator, ShortUrlRepository shortUrlRepository) {
        this.shortenUrlGenerator = shortenUrlGenerator;
        this.shortUrlRepository = shortUrlRepository;
    }

    @Transactional
    public SaveShortenUrlResponseDto shortenUrl(SaveShortenUrlRequestDto request) {
        // 1. 해당 long url에 해당하는 단축 url이 존재하는지 체크
        Optional<ShortenUrl> maybeShortenUrl = shortUrlRepository.findByLongUrl(request.longUrl());

        // 2. 존재하면 이미 생성되어 있는 단축 url 반환
        if (maybeShortenUrl.isPresent()) {
            return new SaveShortenUrlResponseDto(maybeShortenUrl.get().getShortUrl());
        }

        // 3. 단축 url 생성
        String uniqueId = NanoIdUtils.randomNanoId();
        String shortenedUrl = shortenUrlGenerator.generate(uniqueId, request.longUrl());

        // 4. long_url, short_url db에 저장
        ShortenUrl shortenUrl = new ShortenUrl(uniqueId, shortenedUrl, request.longUrl());
        shortUrlRepository.save(shortenUrl);

        return new SaveShortenUrlResponseDto(shortenUrlGenerator.getShortUrlWithHost(shortenedUrl));
    }

    @Transactional(readOnly = true)
    public String getLongUrl(String shortUrl) {
        ShortenUrl shortenUrl = shortUrlRepository.findByShortUrl(shortUrl)
            .orElseThrow(() -> new NoSuchElementException("ShortUrl not found"));

        return shortenUrl.getLongUrl();
    }
}
