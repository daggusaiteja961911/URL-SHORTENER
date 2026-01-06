package com.saiteja.URLSHORTNERSERVICE.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class ShortCodeGenerator {
	
	private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final int CODE_LENGTH = 6;
	private final SecureRandom secureRandom;
	
	public ShortCodeGenerator() {
		this.secureRandom = new SecureRandom();
	}

	/**
	 * Generates a random 6-Character Base62 short code.
	 *
	 * @return A randomly 6-character string using Base62 encoding (0-9, a-z, A-Z).
	 */
	
	public String generate() {
		StringBuilder code = new StringBuilder(CODE_LENGTH);
		
		for (int i = 0; i < CODE_LENGTH; i++) {
			int randomIndex = secureRandom.nextInt(BASE62_CHARS.length());
			code.append(BASE62_CHARS.charAt(randomIndex));
		}
		
		return code.toString();
	}

}
