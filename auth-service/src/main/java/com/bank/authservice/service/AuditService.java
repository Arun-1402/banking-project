package com.bank.authservice.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.bank.authservice.audit.LoginAuditEntity;
import com.bank.authservice.repository.LoginAuditRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

	private final LoginAuditRepository auditRepository;
	
	public void logSuccess(String userName, String ipAddress) {
		auditRepository.save(LoginAuditEntity.builder()
				.userName(userName)
				.success(true)
				.ipAddress(ipAddress)
				.loginTime(LocalDateTime.now())
				.build());
	}
	public void logFailure(String userName, String ipAddress, String reason) {
		auditRepository.save(LoginAuditEntity.builder()
				.userName(userName)
				.success(false)
				.ipAddress(ipAddress)
				.reason(reason)
				.loginTime(LocalDateTime.now())
				.build());
	}
}
