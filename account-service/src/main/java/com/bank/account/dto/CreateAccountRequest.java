package com.bank.account.dto;

import java.math.BigDecimal;

import com.bank.account.enumarate.AccountType;

import lombok.Data;

@Data
public class CreateAccountRequest {

	private AccountType accountType;
	private BigDecimal intitalDeposit;
}
