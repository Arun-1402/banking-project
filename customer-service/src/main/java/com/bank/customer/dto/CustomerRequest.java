package com.bank.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRequest {

	@NotBlank(message = "Name cannot be empty")
	private String name;
	@NotBlank(message = "Phone Number cannot be empty")
	private String phoneNumber;
	@Email(message = "Invalid email")
	private String email;
}
