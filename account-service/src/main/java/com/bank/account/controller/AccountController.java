package com.bank.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.dto.AccountResponse;
import com.bank.account.dto.CreateAccountRequest;
import com.bank.account.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;
	
	@PreAuthorize("hasAuthority('CREATE_ACCOUNT')")
	@PostMapping("/create")
	public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request, HttpServletRequest httpRequest){
		
		String ip =httpRequest.getRemoteAddr();
		String deviceId = httpRequest.getHeader("X-Device-Id");
		return ResponseEntity.ok(accountService.createAccount(request, ip, deviceId));
	}
}
