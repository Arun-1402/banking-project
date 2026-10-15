package com.bank.transaction.resilience;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.bank.transaction.dto.AccountDto;
import com.bank.transaction.feign.AccountClient;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountProtectionService {

	private final AccountClient accountClient;
	
	@Retry(name = "accountService",fallbackMethod = "accountFallBack")
	@Bulkhead(name = "accountService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "accountFallBack")
	@CircuitBreaker(name = "accountService", fallbackMethod = "accountFallBack")
	@TimeLimiter(name = "accountService")
	public CompletableFuture<AccountDto> getAccountSecured(String accountNumber) {
		return CompletableFuture.supplyAsync(()-> accountClient.getAccount(accountNumber));
	}
	
	public CompletableFuture<AccountDto> accountFallBack(String accountNumber, Exception e) {
		throw new RuntimeException("Account service is unavailable at moment, please try after sometime");
	}
}
