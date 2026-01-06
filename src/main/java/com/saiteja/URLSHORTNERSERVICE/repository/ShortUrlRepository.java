package com.saiteja.URLSHORTNERSERVICE.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saiteja.URLSHORTNERSERVICE.entity.ShortUrl;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
	
	Optional<ShortUrl> findByShortCode(String shortCode);
	
	Optional<ShortUrl> findByOriginalUrl(String originalUrl);

}
