package com.bank.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.authservice.domain.User;
import com.bank.authservice.dto.RegisterRequest;
import com.bank.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public User register(RegisterRequest request) {
		User user = User.builder().userName(request.getUserName()).password(passwordEncoder.encode(request.getPassword()))
					.branchId(request.getBranchId()).employeeCode(request.getEmployeeCode()).enabled(true)
					.phoneNumber(request.getPhoneNumber()).email(request.getEmail()).accountNonLocked(true)
					.failedAttempts(0).lockTime(null)
					.build();
		return userRepository.save(user);
		
	}
	
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName).orElseThrow(()-> new RuntimeException("user not found : "+userName));
	}
}
