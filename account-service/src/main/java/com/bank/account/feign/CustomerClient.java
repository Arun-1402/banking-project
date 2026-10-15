package com.bank.account.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.account.dto.CustomerDto;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerClient {
	
	@GetMapping("/customers/internal")
	CustomerDto getCustomerByUserId(@RequestParam Long userId);

}
