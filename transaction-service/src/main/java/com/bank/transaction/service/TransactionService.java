package com.bank.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bank.transaction.audit.TransactionAuditService;
import com.bank.transaction.dto.AccountDto;
import com.bank.transaction.dto.BalanceUpdateRequest;
import com.bank.transaction.dto.TransactionRequest;
import com.bank.transaction.dto.TransactionResponse;
import com.bank.transaction.entity.Transaction;
import com.bank.transaction.enumerator.TransactionStatus;
import com.bank.transaction.enumerator.TransactionType;
import com.bank.transaction.feign.AccountClient;
import com.bank.transaction.repository.TransactionRepository;
import com.bank.transaction.resilience.AccountProtectionService;
import com.bank.transaction.util.SecurityUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountClient accountClient;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final TransactionAuditService auditService;
	private final AccountProtectionService accountProtectionService;
	
	@Transactional
	public TransactionResponse processed(TransactionRequest request, String ip, String deviceId) {
		Long userId = SecurityUtil.getUserId();
		Optional<Transaction> existinTxn = transactionRepository.findByReferenceId(request.getReferenceId());
		if (existinTxn.isPresent()) {
			Transaction exist = existinTxn.get();
			return mapToResponse(exist);
		}
		String txnId = UUID.randomUUID().toString();
		Transaction txn= Transaction.builder().txnId(txnId)
							.referenceId(request.getReferenceId())
							.accountNumber(request.getAccountNumber()).type(request.getType())
							.amount(request.getAmount()).status(TransactionStatus.PENDING).createdAt(LocalDateTime.now())
							.updatedAt(LocalDateTime.now()).build();
		transactionRepository.save(txn);
		try {
			//AccountDto account = getAccountSecured(request.getAccountNumber());
		AccountDto account =	accountProtectionService.getAccountSecured(request.getAccountNumber()).join();
			if ("BLOCKED".equals(account.getStatus())) {
				throw new RuntimeException("Account is blocked");
			}
			if (request.getAmount().compareTo(BigDecimal.valueOf(50000)) > 0) {
				throw new RuntimeException("Limit exceeded");
			}
			if (request.getType().equals(TransactionType.DEBIT) && account.getBalance().compareTo(request.getAmount()) < 0) {
				throw new RuntimeException("Insufficient Funds");
			}
			BigDecimal newBalance = request.getType() == TransactionType.DEBIT ?
										account.getBalance().subtract(request.getAmount()) : account.getBalance().add(request.getAmount());
			accountClient.updateBalance(new BalanceUpdateRequest(account.getAccountNumber(), newBalance, request.getType().name() ));
			txn.setStatus(TransactionStatus.SUCCESS);
			txn.setUpdatedAt(LocalDateTime.now());
			transactionRepository.save(txn);
			auditService.log(txnId, userId, request.getType().name(), ip, deviceId);
			kafkaTemplate.send("transaction-event", txn);
			return mapToResponse(txn);
		} catch (Exception e) {
			// TODO: handle exception
			return TransactionResponse.builder().build();
		}
	}
	

	
	private TransactionResponse mapToResponse(Transaction t) {
	return	TransactionResponse.builder().amount(t.getAmount()).status(t.getStatus()).txnId(t.getTxnId()).build();
	}
}
