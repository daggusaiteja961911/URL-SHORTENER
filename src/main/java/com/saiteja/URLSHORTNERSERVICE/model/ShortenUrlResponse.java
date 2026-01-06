package com.saiteja.URLSHORTNERSERVICE.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShortenUrlResponse {
	
	private String shortCode;
	private String shortUrl;
	private String originalUrl;
	private LocalDateTime createdAt;
	private LocalDateTime expiryAt;

}
