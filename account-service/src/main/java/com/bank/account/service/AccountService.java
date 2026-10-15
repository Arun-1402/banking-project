package com.bank.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bank.account.dto.AccountResponse;
import com.bank.account.dto.BalanceUpdateRequest;
import com.bank.account.dto.CreateAccountRequest;
import com.bank.account.dto.CustomerDto;
import com.bank.account.entity.Account;
import com.bank.account.enumarate.AccountStatus;
import com.bank.account.feign.CustomerClient;
import com.bank.account.repository.AccountRepository;
import com.bank.account.security.SecurityUtil;
import com.bank.account.util.AccountNumberGenerator;
import com.bank.account.validator.AccountValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
	private final AccountRepository accountRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final CustomerClient feignClient;
	private final AccountNumberGenerator numberGenerator;
	private final AccountSequenceService sequenceService;
	private final DistributedLockService lockService;
	
	public AccountResponse createAccount(CreateAccountRequest request, String ip, String deviceId) {
		Long userId = SecurityUtil.getUserId();
		CustomerDto customerDto  = feignClient.getCustomerByUserId(userId);
		long sequence = sequenceService.getNext();
		String accountNumber = numberGenerator.generator("4201", "10", sequence);
		AccountValidator.validateInitialDeposit(request.getIntitalDeposit());
		AccountValidator.validateCustomer(customerDto);
		log.info("Create Account customerId={} type={}",customerDto.getId(),request.getAccountType());
		Account account = Account.builder().accountNumber(accountNumber)
							.customerId(customerDto.getId()).accountType(request.getAccountType())
							.balance(request.getIntitalDeposit()).status(AccountStatus.ACTIVE)
							.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
		Account persistedAcc = accountRepository.save(account);
		kafkaTemplate.send("account-created", persistedAcc);
		return mapAccountTOResponse(persistedAcc);
	}
	
	@Transactional
	public void updateBalance(BalanceUpdateRequest request) {
		lockService.executeWithLock("account-lock:"+request.getAccountNumber(), ()-> {
		Account account = accountRepository.findByAccountNumberForUpdate(request.getAccountNumber())
							.orElseThrow(()-> new RuntimeException("Account not found"));
		if (account.getStatus() == AccountStatus.BLOCKED) {
			throw new RuntimeException("Account is Blocked");
		}
		log.info("Update Account accountNum={} type={}",request.getAccountNumber(),request.getTransactionType());
		BigDecimal currentBal = account.getBalance();
		BigDecimal updatedBal;
		if ("DEBIT".equals(request.getTransactionType())) {
			if (currentBal.compareTo(request.getNewBalance()) < 0) {
				throw new RuntimeException("Insufficient Funds");
			}
			updatedBal = currentBal.subtract(request.getNewBalance());
		}else {
			updatedBal = currentBal.add(request.getNewBalance());
		}
		account.setBalance(updatedBal);
		accountRepository.save(account);
		return null;
		});
	}
	
	private AccountResponse mapAccountTOResponse(Account acc) {
		return AccountResponse.builder().accountNumber(acc.getAccountNumber())
							.balance(acc.getBalance()).type(acc.getAccountType())
							.status(acc.getStatus()).build();
	}
}
