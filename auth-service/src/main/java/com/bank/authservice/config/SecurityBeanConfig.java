package com.bank.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bank.authservice.service.CustomUserDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityBeanConfig {
	
	private final CustomUserDetailService userDetailService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity security, PasswordEncoder encoder) throws Exception{
		
		AuthenticationManagerBuilder builder = security.getSharedObject(AuthenticationManagerBuilder.class);
		
		builder.userDetailsService(userDetailService).passwordEncoder(encoder);
		return builder.build();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{
		security
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(auth-> auth 
				.requestMatchers("/user/register", "/auth/**").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated());
		
		return security.build();
	}
}
