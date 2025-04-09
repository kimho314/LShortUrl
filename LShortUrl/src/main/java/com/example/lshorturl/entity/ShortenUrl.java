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
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "shorten_url", indexes = {@Index(name = "shorten_url_idx1", columnList = "long_url"),
    @Index(name = "shorten_url_idx2", columnList = "short_url")})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShortenUrl implements Persistable<String> {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "long_url", unique = true)
    private String longUrl;

    // 직접 ID를 할당하는 경우 해당 엔터티에 대해 isNew()가 false가 반환되어 merge() 메소드가 호출 된다.
    // merge() 메소드가 호출 되므로 insert 쿼리가 실행되기 전에 select 쿼리가 한번 더 실행된다.
    // 이렇게 실행되는 select 쿼리는 비횰적이므로 Persistable를 상속받아 isNew() 메소드를 재정의해서 select 쿼리가 한 번 더 실행되는 것을 방지한다.
    @Override
    public boolean isNew() {
        return true;
    }

}
