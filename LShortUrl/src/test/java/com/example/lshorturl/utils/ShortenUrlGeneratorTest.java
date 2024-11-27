package com.example.lshorturl.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class ShortenUrlGeneratorTest {

    ShortenUrlGenerator shortenUrlGenerator;

    @BeforeEach
    void setUp() {
        shortenUrlGenerator = new ShortenUrlGeneratorImpl();
    }

    @Test
    void shortenUrlGenerateTest() {
        String nanoId = NanoIdUtils.randomNanoId();
        log.info("nanoId: {}", nanoId);
        String longUrl = "https://io.cjconnect.unban.ai/cjconnect/service/api/driver/faq/v1/category-meta-list";

        String shortenUrl = shortenUrlGenerator.generate(nanoId, longUrl);
        log.info("shortenUrl: {}", shortenUrl);

        Assertions.assertNotNull(shortenUrl);
        Assertions.assertTrue(shortenUrl.contains(ShortenUrlGeneratorImpl.TINY_URL_HOST));
    }

}
