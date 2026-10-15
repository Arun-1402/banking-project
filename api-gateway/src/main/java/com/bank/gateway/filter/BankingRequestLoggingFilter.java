package com.bank.gateway.filter;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BankingRequestLoggingFilter implements GlobalFilter, Ordered {
	
	private static final Logger log = LoggerFactory.getLogger(BankingRequestLoggingFilter.class);

	@Override
	public int getOrder() {
		
		return -1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		String RequestId = UUID.randomUUID().toString();
		exchange.getResponse().getHeaders().add("X-Request-id", RequestId);
		String path = exchange.getRequest().getURI().getPath();
		String method = exchange.getRequest().getMethod().name();
		String user = exchange.getRequest().getHeaders().getFirst("X-User-Id");
		log.info("[Request] id = {} user = {} {} {}", RequestId,user,method,path);
		return chain.filter(exchange).doOnSuccess(v -> {
			long timeTaken = System.currentTimeMillis() - startTime;
			log.info("[Response] id ={} status = {} time = {}ms", RequestId,exchange.getResponse().getStatusCode(),timeTaken);
		}).doOnError(e->{
			long timeTaken = System.currentTimeMillis() - startTime;
			log.error("[Error] id ={} {} {} time = {}ms error={}", RequestId,method,path,timeTaken,e);
		});
	}

}
