package com.bank.customer.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SetuService {

private final WebClient webClient;
@Value("${setu.client-id}")
private String clientId;
@Value("${setu.client-secret}")
private String clientSecret;
@Value("${setu.pan-product-id}")
private String panProductId;
@Value("${setu.base-url}")
private String baseUrl;

public String verifyPan(String pan) {
	Map<String, Object> map = Map.of("pan",pan,
									"consent","Y",
									"reason","Kyc Verification");
	return webClient.post().uri(baseUrl).header("x-client-id", clientId)
			.header("x-client-secret", clientSecret)
			.header("x-product-instance-id", panProductId)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(map)
			.retrieve().bodyToMono(String.class).block();
}
}
