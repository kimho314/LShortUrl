package com.example.lshorturl.utils;

import io.seruco.encoding.base62.Base62;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class ShortenUrlGeneratorImpl implements ShortenUrlGenerator {

    public static final String TINY_URL_HOST = "http://tiny.com/";

    @Override
    public String generate(String uniqueId, String longUrl) {
        // 1. 생성한 unique id를 base62 변환
        Base62 standardEncoder = Base62.createInstance();
        byte[] encode = standardEncoder.encode(uniqueId.getBytes(StandardCharsets.UTF_8));

        // 2. base62 변환 값으로 단축 url 생성

        return new String(encode, StandardCharsets.UTF_8);
    }

    @Override
    public String getShortUrlWithHost(String shortUrl) {
        return TINY_URL_HOST + shortUrl;
    }
}
