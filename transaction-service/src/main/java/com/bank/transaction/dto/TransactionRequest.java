package com.bank.transaction.dto;

import java.math.BigDecimal;

import com.bank.transaction.enumerator.TransactionType;

import lombok.Data;

@Data
public class TransactionRequest {

	private String accountNumber;
	private BigDecimal amount;
	private TransactionType type;
	private String referenceId;
}
