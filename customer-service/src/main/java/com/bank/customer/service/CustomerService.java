package com.bank.customer.service;

import java.time.LocalDateTime;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bank.customer.dto.CustomerDto;
import com.bank.customer.dto.CustomerRequest;
import com.bank.customer.dto.CustomerResponse;
import com.bank.customer.entity.Customer;
import com.bank.customer.entity.KycDetail;
import com.bank.customer.enumerator.CustomerStatus;
import com.bank.customer.repository.CustomerRepository;
import com.bank.customer.repository.KycRepository;
import com.bank.customer.security.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;
	private final KycRepository kycRepository;
	
	public CustomerResponse create(CustomerRequest request) {
		Long userId = SecurityUtil.getUserId();
		customerRepository.findByUserIdAndIsDeletedFalse(userId)
							.ifPresent(c->{ throw new RuntimeException("customer already exists");});
		
		Customer customer =  Customer.builder().name(request.getName()).email(request.getEmail())
					.phoneNumber(request.getPhoneNumber()).status(CustomerStatus.PENDING)
					.userId(userId).createdBy(SecurityUtil.getUserName()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
					.build();
		return mapToCustomerResponseFromEntity(customerRepository.save(customer));
	}
	
	public CustomerResponse get(Long id) {
		Customer customer = customerRepository.findById(id).orElseThrow(()-> new RuntimeException("Customer not Found"));
		return mapToCustomerResponseFromEntity(customer);
	}
	
	@Cacheable(value = "customer-user-cache", key = "#userId")
	public CustomerDto getByUserId(Long userId) {
		Customer cust = customerRepository.findByUserIdAndIsDeletedFalse(userId)
						.orElseThrow(()-> new RuntimeException("customer not found"));
			
			KycDetail kycDetail=	kycRepository.findByCustomerId(cust.getId()).orElse(null);
		
			return mapToCustomerDto(cust, kycDetail);
	}
	
	private CustomerResponse mapToCustomerResponseFromEntity(Customer c) {
		return CustomerResponse.builder().id(c.getId()).name(c.getName()).email(c.getEmail())
				.phoneNumber(c.getPhoneNumber()).status(c.getStatus()).build();
	}
	
	private CustomerDto mapToCustomerDto(Customer cust, KycDetail kyc) {
		CustomerDto dto = new CustomerDto();
		dto.setId(cust.getId());
		dto.setStatus(cust.getStatus().name());
		dto.setKycStatus(kyc.getStatus().name());
		return dto;
	}
}
