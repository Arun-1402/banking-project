package com.bank.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.customer.entity.KycDetail;

public interface KycRepository extends JpaRepository<KycDetail, Long>{
	Optional<KycDetail>  findByCustomerId(Long customerId);
}
