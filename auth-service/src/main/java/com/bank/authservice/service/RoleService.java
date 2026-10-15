package com.bank.authservice.service;

import org.springframework.stereotype.Service;

import com.bank.authservice.domain.Permission;
import com.bank.authservice.domain.Role;
import com.bank.authservice.repository.PermissionRepository;
import com.bank.authservice.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

	private final PermissionRepository permissionRepository;
	private final RoleRepository roleRepository;
	
	public void assignPermissionToRole(Long roleId, Long permissionId) {
	Role role =	roleRepository.findById(roleId).orElseThrow(()-> new RuntimeException("Role not found"));
	Permission perm = permissionRepository.findById(permissionId).orElseThrow(()-> new RuntimeException("Permission not found"));
	role.getPermissions().add(perm);
	roleRepository.save(role);
	}
	
	public void removePermissionFromRole(Long roleId, Long permissionId) {
		Role role =	roleRepository.findById(roleId).orElseThrow(()-> new RuntimeException("Role not found"));
		Permission perm = permissionRepository.findById(permissionId).orElseThrow(()-> new RuntimeException("Permission not found"));
		role.getPermissions().removeIf(p -> p.getId().equals(permissionId));
		roleRepository.save(role);
	}
}
