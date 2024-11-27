package com.example.lshorturl.repository;

import com.example.lshorturl.entity.ShortenUrl;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortenUrl, String> {
    Optional<ShortenUrl> findByLongUrl(String longUrl);
}
