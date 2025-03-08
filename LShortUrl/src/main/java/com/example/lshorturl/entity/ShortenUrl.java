package com.example.lshorturl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shorten_url", indexes = {@Index(name = "shorten_url_idx1", columnList = "long_url"),
    @Index(name = "shorten_url_idx2", columnList = "short_url")})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShortenUrl {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "long_url", unique = true)
    private String longUrl;
}
