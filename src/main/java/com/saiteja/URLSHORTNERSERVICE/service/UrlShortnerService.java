package com.saiteja.URLSHORTNERSERVICE.service;

import com.saiteja.URLSHORTNERSERVICE.entity.ShortUrl;
import com.saiteja.URLSHORTNERSERVICE.repository.ShortUrlRepository;
import com.saiteja.URLSHORTNERSERVICE.util.ShortCodeGenerator;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UrlShortnerService {

	private final ShortUrlRepository shortUrlRepository;
	private final ShortCodeGenerator shortCodeGenerator;
	private static final int MAX_COLLISION_RETRIES = 10;
	
	public UrlShortnerService(ShortUrlRepository shortUrlRepository, ShortCodeGenerator shortCodeGenerator) {
		this.shortUrlRepository = shortUrlRepository;
		this.shortCodeGenerator = shortCodeGenerator;
	}
	
	/**
	 * Creates a short URL for the given original URL.
	 * If the URL already exists, it returns the existing shortUrl
	 * Otherwise, generates a unique short code and saves it to the database.
	 * 
	 * @param originalUrl The original long URL to be shortened.
	 * @return The ShortUrl entity (existing or newly created).
	 */
	
	public ShortUrl createShortUrl(String originalUrl) {
		// Check if the original URL already exists
		Optional<ShortUrl> existingEntry = shortUrlRepository.findByOriginalUrl(originalUrl);
		if (existingEntry.isPresent()) {
			return existingEntry.get();
		}
		
		// Generate a unique short code, handling potential collisions
		String shortCode = generateUniqueShortCode();
		
		// Create and save new ShortUrl
		ShortUrl shortUrl = new ShortUrl();
		shortUrl.setOriginalUrl(originalUrl);
		shortUrl.setShortCode(shortCode);
		
		return shortUrlRepository.save(shortUrl);
	}
	
	/**
	 * Generates a unique short code by handling potential collisions.
	 * 
	 * @return A unique short code that does not exist in the database.
	 */
	
	private String generateUniqueShortCode() {
		int attempts = 0;
		String shortCode;
		
		do {
			shortCode = shortCodeGenerator.generate();
			attempts++;
			
			if(attempts > MAX_COLLISION_RETRIES) {
				throw new RuntimeException("Failed to generate a unique short code after " + 
			              MAX_COLLISION_RETRIES + " attempts");
			}
		} while(shortUrlRepository.findByShortCode(shortCode).isPresent());
		
		return shortCode;
	}
	
	/**
	 * Retrieves the original URL for the given short code.
	 * 
	 * @param shortCode The short code to look up.
	 * @return The original URL if found, otherwise null.
	 */
	
	@Transactional(readOnly = true)
	public Optional<String> getOriginalUrl(String shortCode) {
		return shortUrlRepository.findByShortCode(shortCode)
				.map(ShortUrl::getOriginalUrl);
	}
	
	
}
