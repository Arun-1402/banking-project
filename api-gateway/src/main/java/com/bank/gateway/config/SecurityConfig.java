package com.bank.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	 SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.authorizeExchange(auth -> auth.pathMatchers("/public/**").permitAll()
						.pathMatchers("/api/admin/**").hasRole("ADMIN").anyExchange()
						.authenticated()).oauth2ResourceServer(oauth -> oauth.jwt(jwt ->
			               jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))).build();
	}
	
//	@Bean
//    public ReactiveJwtDecoder jwtDecoder() {
//        // Replace with the issuer URI of your Auth Service
//        String issuerUri = "http://localhost:9000"; 
//        return JwtDecoders.fromIssuerLocation(issuerUri);
//    }
	
	@Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // ROLE_ already hai token me
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities"); // ye important

        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
            new ReactiveJwtGrantedAuthoritiesConverterAdapter(grantedAuthoritiesConverter));
        return jwtAuthenticationConverter;
    }
}
