package com.bank.account.validator;

import java.math.BigDecimal;

import com.bank.account.dto.CustomerDto;

public class AccountValidator {

	public static void validateInitialDeposit(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.valueOf(1000))<0) {
			throw new RuntimeException("Minimun balance must be 1000/-");
		}
	}
	public static void validateCustomer(CustomerDto customerDto) {
		if (!"ACTIVE".equals(customerDto.getStatus())) {
			throw new RuntimeException("customer is not active");
		}
		if (!"VERIFIED".equals(customerDto.getKycStatus())) {
			throw new RuntimeException("Kyc not verified");
		}
	}
}
