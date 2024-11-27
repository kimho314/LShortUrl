package com.example.lshorturl.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.lshorturl.dto.SaveShortenUrlRequestDto;
import com.example.lshorturl.dto.SaveShortenUrlResponseDto;
import com.example.lshorturl.entity.ShortenUrl;
import com.example.lshorturl.repository.ShortUrlRepository;
import com.example.lshorturl.utils.ShortenUrlGeneratorImpl;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShortUrlServiceV1Test {

    @Mock
    ShortUrlRepository shortUrlRepository;
    @Mock
    ShortenUrlGeneratorImpl shortenUrlGenerator;
    @InjectMocks
    ShortUrlServiceV1 shortUrlServiceV1;

    @Test
    @DisplayName("단축 url 생성 - 새로운 단축 url")
    void shortenUrlTest_NewShortenedUrl() {
        //given
        String longUrl = "https://io.cjconnect.unban.ai/cjconnect/service/api/driver/faq/v1/category-meta-list";
        String uniqueId = "nanoId: 1BVhLZF6pQEIqPAmsHMo3";
        String shortenUrl = "http://tiny.com/T1IE1ozloHPO4eflBZs7s4wjOjFD";

        Mockito.when(shortUrlRepository.findByLongUrl(anyString()))
            .thenReturn(Optional.empty());
        Mockito.when(shortenUrlGenerator.generate(anyString(), anyString()))
            .thenReturn(shortenUrl);

        //when
        SaveShortenUrlResponseDto saveShortenUrlResponseDto = shortUrlServiceV1.shortenUrl(new SaveShortenUrlRequestDto(longUrl));

        //then
        Mockito.verify(shortUrlRepository).save(any());
        Assertions.assertEquals(shortenUrl, saveShortenUrlResponseDto.shortenUrl());
    }

    @Test
    @DisplayName("단축 url 생성 - 기존 단축 url 존재")
    void shortenUrlTest_ExistedShortenedUrl() {
        //given
        String longUrl = "https://io.cjconnect.unban.ai/cjconnect/service/api/driver/faq/v1/category-meta-list";
        String uniqueId = "nanoId: 1BVhLZF6pQEIqPAmsHMo3";
        String shortenUrl = "http://tiny.com/T1IE1ozloHPO4eflBZs7s4wjOjFD";

        Mockito.when(shortUrlRepository.findByLongUrl(anyString()))
            .thenReturn(Optional.of(new ShortenUrl(uniqueId, shortenUrl, longUrl)));

        //when
        SaveShortenUrlResponseDto saveShortenUrlResponseDto = shortUrlServiceV1.shortenUrl(new SaveShortenUrlRequestDto(longUrl));

        //then
        Assertions.assertEquals(shortenUrl, saveShortenUrlResponseDto.shortenUrl());
    }
}
