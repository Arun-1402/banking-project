package com.bank.customer.service;

import org.springframework.stereotype.Service;

import com.bank.customer.dto.KycDto;
import com.bank.customer.entity.Customer;
import com.bank.customer.entity.KycDetail;
import com.bank.customer.enumerator.CustomerStatus;
import com.bank.customer.enumerator.KycStatus;
import com.bank.customer.exception.ResourceNotFoundException;
import com.bank.customer.repository.CustomerRepository;
import com.bank.customer.repository.KycRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KycService {

	private final KycRepository kycRepository;
	private final CustomerRepository customerRepository;
	
	public KycDto submitKyc(Long customerId, KycDetail detail) {
	Customer customer =	customerRepository.findById(customerId)
			.orElseThrow(()->new ResourceNotFoundException("Customer not found"));
	detail.setCustomer(customer);
	detail.setStatus(KycStatus.PENDING);
	return mapKycDetailsToResponse(kycRepository.save(detail));
	}
	
	public KycDto getKyc(Long customerId) {
		KycDetail detail= kycRepository.findByCustomerId(customerId).orElseThrow(()->new RuntimeException("KYC data not found"));
		return mapKycDetailsToResponse(detail);
	}
	
	public KycDto updateKyc(Long customerId, KycDetail request) {
		KycDetail detail= kycRepository.findByCustomerId(customerId).orElseThrow(()->new RuntimeException("KYC data not found"));
		detail.setAdharNumber(request.getAdharNumber());
		detail.setPanNum(request.getPanNum());
		return mapKycDetailsToResponse(kycRepository.save(detail));
	}
	
	public void approveKyc(Long kycId) {
	KycDetail kyc =	kycRepository.findById(kycId).orElseThrow(()-> new RuntimeException("Kyc not found"));
	kyc.setStatus(KycStatus.VERIFIED);
	Customer customer = kyc.getCustomer();
	customer.setStatus(CustomerStatus.ACTIVE);
	customerRepository.save(customer);
	kycRepository.save(kyc);
	}
	
	public void rejectKyc(Long kycId) {
		KycDetail kyc =	kycRepository.findById(kycId).orElseThrow(()-> new RuntimeException("Kyc not found"));
		kyc.setStatus(KycStatus.REJECTED);
		kycRepository.save(kyc);
	}
	
	private KycDto mapKycDetailsToResponse(KycDetail detail) {
		return KycDto.builder().id(detail.getId()).adharNumber(detail.getAdharNumber())
				.panNum(detail.getPanNum()).status(detail.getStatus()).customer(detail.getCustomer()).build();
	}
}
