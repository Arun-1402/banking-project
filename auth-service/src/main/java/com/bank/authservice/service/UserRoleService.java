package com.bank.authservice.service;

import org.springframework.stereotype.Service;

import com.bank.authservice.domain.Role;
import com.bank.authservice.domain.User;
import com.bank.authservice.repository.RoleRepository;
import com.bank.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRoleService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	public void assignRoleToUser(Long userId, Long roleId) {
	User user =	userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
	Role role = roleRepository.findById(roleId).orElseThrow(()-> new RuntimeException("Role not found"));
	user.getRoles().add(role);
	userRepository.save(user);
	}
	
	public void removeRoleFromUser(Long userId, Long roleId) {
		User user =	userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
		Role role = roleRepository.findById(roleId).orElseThrow(()-> new RuntimeException("Role not found"));
		user.getRoles().removeIf(r -> r.getId().equals(roleId));
		userRepository.save(user);
	}
}
