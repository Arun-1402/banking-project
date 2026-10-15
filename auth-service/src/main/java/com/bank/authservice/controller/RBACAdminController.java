package com.bank.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.authservice.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/rbac")
@RequiredArgsConstructor
public class RBACAdminController {

	private final RoleService roleService;
	
	@PostMapping("/role/{roleId}/permission/{permissionId}")
	public ResponseEntity<?> assignPermission(@PathVariable Long roleId, @PathVariable Long permissionId){
		roleService.assignPermissionToRole(roleId, permissionId);
		return ResponseEntity.ok("Permissions assigned successfully");
	}
	
	@DeleteMapping("/role/{roleId}/permission/{permissionId}")
	public ResponseEntity<?> removePermission(@PathVariable Long roleId, @PathVariable Long permissionId){
		roleService.removePermissionFromRole(roleId, permissionId);
		return ResponseEntity.ok("Permissions removed successfully");
	}
}
