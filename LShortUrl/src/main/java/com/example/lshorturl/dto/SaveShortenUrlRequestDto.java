package com.example.lshorturl.dto;

import jakarta.validation.constraints.NotBlank;

public record SaveShortenUrlRequestDto(@NotBlank(message = "longUrl must not be blank") String longUrl) {

}
