package com.saiteja.URLSHORTNERSERVICE.controller;


import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import com.saiteja.URLSHORTNERSERVICE.entity.ShortUrl;
import com.saiteja.URLSHORTNERSERVICE.model.ShortenUrlRequest;
import com.saiteja.URLSHORTNERSERVICE.model.ShortenUrlResponse;
import com.saiteja.URLSHORTNERSERVICE.service.UrlShortnerService;

import jakarta.validation.Valid;




@Controller
public class HomeController {
	
	private final UrlShortnerService urlShortenerService;

	public HomeController(UrlShortnerService urlShortenerService) {
		this.urlShortenerService = urlShortenerService;
	}

	
	/**
	 * Displays the form page for URL shortening.
	 */
	@GetMapping("/")
	public String showForm(Model model) {
		model.addAttribute("request", new ShortenUrlRequest());
		return "index";
	}
	
	/**
	 * Processes the form submission and creates a shortened URL
	 */
	
	@PostMapping("/shorten")
	public String shortenUrl(@Valid ShortenUrlRequest request,
			                 BindingResult bindingResult,
			                 Model model,
			                 HttpServletRequest httpRequest) {
		
		if (bindingResult.hasErrors()) {
			return "index";
		}
		
		ShortUrl shortUrl = urlShortenerService.createShortUrl(request.getUrl());
		
		// Build the short URL
		String baseUrl = getBaseUrl(httpRequest);
		String shortUrlString = baseUrl + "/" + shortUrl.getShortCode();
		
		// Create response object
		ShortenUrlResponse response = new ShortenUrlResponse();
		response.setShortCode(shortUrl.getShortCode());
		response.setShortUrl(shortUrlString);
		response.setOriginalUrl(shortUrl.getOriginalUrl());
		response.setCreatedAt(shortUrl.getCreatedAt());
		response.setExpiryAt(shortUrl.getExpiresAt());
		
		model.addAttribute("response", response);
		return "result";
	}
	
	/**
	 * Redirects to the original URL based on the provided short code.
	 */
	
	@GetMapping("/{shortCode}")
	public String redirectToOriginalUrl(@PathVariable String shortCode) {
		Optional<String> originalUrl = urlShortenerService.getOriginalUrl(shortCode);
		
		if(originalUrl.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found");
		}
		
		return "redirect:" + originalUrl.get();
	}
	
	/**
	 * Helper method to get the base URL from the request.
	 */
	private String getBaseUrl(HttpServletRequest request) {
		String scheme = request.getScheme();             // http or https
		String serverName = request.getServerName();     // domain name
		int serverPort = request.getServerPort();        // port number
		String contextPath = request.getContextPath();   // application context path
		
		// Construct base URL
		StringBuilder baseUrl = new StringBuilder();
		baseUrl.append(scheme).append("://").append(serverName);
		
		// Append port if not default
		if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
			baseUrl.append(":").append(serverPort);
		}
		
		baseUrl.append(contextPath);
		return baseUrl.toString();
	}

}
