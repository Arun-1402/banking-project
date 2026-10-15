package com.bank.account.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {

	private Long id;
	private String status;
	private String kycStatus;
}
