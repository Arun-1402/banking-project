package com.bank.customer.dto;

import com.bank.customer.enumerator.CustomerStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {
	private Long id;
	private String name;
	private String phoneNumber;
	private String email;
	private CustomerStatus status;
}
