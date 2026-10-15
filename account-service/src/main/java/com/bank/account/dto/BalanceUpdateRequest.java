package com.bank.account.dto;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateRequest {

	private String accountNumber;
	private BigDecimal newBalance;
	private String transactionType;
}
