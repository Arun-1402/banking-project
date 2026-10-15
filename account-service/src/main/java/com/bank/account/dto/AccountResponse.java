package com.bank.account.dto;

import java.math.BigDecimal;

import com.bank.account.enumarate.AccountStatus;
import com.bank.account.enumarate.AccountType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponse {

	private String accountNumber;
	private AccountType type;
	private AccountStatus status;
	private BigDecimal balance;
}
