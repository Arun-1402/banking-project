package com.bank.account.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.bank.account.entity.AccountAudit;
import com.bank.account.repository.AccountAuditRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountAuditService {

	private final AccountAuditRepository auditRepository;
	
	public void logAudit(String accountNumber, Long userId, String action, String ip, String deviceId) {
		AccountAudit audit = AccountAudit.builder().accountNumber(accountNumber).userId(userId)
								.action(action).deviceId(deviceId).ip(ip).createdAt(LocalDateTime.now()).build();
		auditRepository.save(audit);
	}
}
