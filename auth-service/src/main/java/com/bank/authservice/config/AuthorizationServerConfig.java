package com.bank.authservice.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;

import com.bank.authservice.domain.User;
import com.bank.authservice.repository.UserRepository;
import com.bank.authservice.security.OtpAuthenticationConvertor;
import com.bank.authservice.security.OtpAuthenticationProvider;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthorizationServerConfig {

	private final UserRepository userRepository;
	@Bean
	@Order(-1)
	SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http, 
			OtpAuthenticationConvertor convertor, OtpAuthenticationProvider provider) throws Exception{
		
		OAuth2AuthorizationServerConfigurer serverConfigurer = new OAuth2AuthorizationServerConfigurer();		
		http.securityMatcher(serverConfigurer.getEndpointsMatcher()).with(serverConfigurer,
				auth -> auth.tokenEndpoint(tkp -> tkp.accessTokenRequestConverter(convertor).
						authenticationProvider(provider))).csrf(csrf-> csrf.disable())
		.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
		return http.build();
	}
	
	 @Bean
	public OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator(JWKSource<SecurityContext> jwkSource,OAuth2TokenCustomizer<JwtEncodingContext> customizer){
		 JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
		 jwtGenerator.setJwtCustomizer(customizer);
		 return new DelegatingOAuth2TokenGenerator(jwtGenerator, new OAuth2AccessTokenGenerator());
	 }
	 
	 @Bean
	 public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(){
		 return ctx -> {
			 if(OAuth2TokenType.ACCESS_TOKEN.equals(ctx.getTokenType())) {
				Authentication principal = ctx.getPrincipal();
				if (principal.getPrincipal() instanceof UserDetails user) {
				User dbuser=	userRepository.findByUserName(user.getUsername()).orElseThrow(()-> new RuntimeException("User not found"));
					ctx.getClaims().claim("userId", dbuser.getId());
					List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
					ctx.getClaims().claim("authorities", authorities);
				}
//				Set<String> scopes = principal.getAuthorities().stream()
//	                    .map(GrantedAuthority::getAuthority)
//	                    .collect(Collectors.toSet());
//	            ctx.getClaims().claim("scope", scopes);
			 }
		 };
	 }
	

}
