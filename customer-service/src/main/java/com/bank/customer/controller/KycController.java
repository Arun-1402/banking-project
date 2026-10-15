package com.bank.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.customer.dto.KycDto;
import com.bank.customer.entity.KycDetail;
import com.bank.customer.service.KycService;
import com.bank.customer.service.SetuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/kyc")
@RequiredArgsConstructor
public class KycController {

	private final KycService kycService;
	private final SetuService setuService;
	
	@PostMapping("/submit/{customerId}")
	public ResponseEntity<KycDto> submitKyc(@PathVariable Long customerId, @RequestBody KycDetail detail) {
	
		return ResponseEntity.ok(kycService.submitKyc(customerId, detail));
	}
	
	@PostMapping("/verify_pan")
	public ResponseEntity<?> verifyPan(@RequestParam String pan){
		String res = setuService.verifyPan(pan);
		return ResponseEntity.ok(res);
	}
	

}
