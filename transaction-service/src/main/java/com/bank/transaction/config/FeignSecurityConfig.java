package com.bank.transaction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import feign.RequestInterceptor;

@Configuration
public class FeignSecurityConfig {

	@Bean
	public RequestInterceptor requestInterceptor() {
		return req -> {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth!=null && auth.getCredentials() !=null) {
				String token = auth.getCredentials().toString();
				req.header("Authorization", "Bearer "+token);
			}
		};
	}
}
