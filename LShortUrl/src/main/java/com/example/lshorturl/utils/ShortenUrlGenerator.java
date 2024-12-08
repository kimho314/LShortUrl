package com.example.lshorturl.utils;

public interface ShortenUrlGenerator {

    String generate(String uniqueId, String longUrl);
}
