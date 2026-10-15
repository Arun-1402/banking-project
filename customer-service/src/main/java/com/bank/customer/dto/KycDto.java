package com.bank.customer.dto;

import com.bank.customer.entity.Customer;
import com.bank.customer.enumerator.KycStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KycDto {
	private Long id;
	private String panNum;
	private String adharNumber;
	private KycStatus status;
	private Customer customer;
}
