package com.bank.transaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.transaction.dto.TransactionRequest;
import com.bank.transaction.dto.TransactionResponse;
import com.bank.transaction.entity.Transaction;
import com.bank.transaction.enumerator.TransactionStatus;
import com.bank.transaction.enumerator.TransactionType;
import com.bank.transaction.repository.TransactionRepository;
import com.bank.transaction.service.TransactionService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;
	private final TransactionRepository repository;
	
	@PostMapping("/transfer")
	public ResponseEntity<TransactionResponse> transact(@RequestBody TransactionRequest request, HttpServletRequest http){
		TransactionResponse response = transactionService.processed(request, http.getRemoteAddr(), http.getHeader("X-Device-Id"));
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/reverse/{txnId}")
	public ResponseEntity<?> reverse(@PathVariable String txnId){
		Transaction txn = repository.findByTxnId(txnId).orElseThrow(()-> new RuntimeException("Txn not found"));
		if (txn.getStatus() != TransactionStatus.SUCCESS) {
			throw new RuntimeException("Only successful txn's can be reversed");
		}
		TransactionRequest reverseRequest = new TransactionRequest();
		reverseRequest.setAccountNumber(txn.getAccountNumber());
		reverseRequest.setAmount(txn.getAmount());
		reverseRequest.setType(txn.getType() == TransactionType.DEBIT ? TransactionType.CREDIT : TransactionType.DEBIT);
		return ResponseEntity.ok(transactionService.processed(reverseRequest, "System", "reversal"));
	}
}
