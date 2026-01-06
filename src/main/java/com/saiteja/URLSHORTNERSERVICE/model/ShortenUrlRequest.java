package com.saiteja.URLSHORTNERSERVICE.model;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShortenUrlRequest {
	
	@NotBlank(message = "URL is required")
	@URL (message = "Invalid URL format")
	private String url;

}
