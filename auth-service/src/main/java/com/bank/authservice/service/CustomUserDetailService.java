package com.bank.authservice.service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bank.authservice.domain.User;
import com.bank.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username)
					.orElseThrow(()-> new UsernameNotFoundException("User not found"));
		UserDetails ud = new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), 
				user.getRoles().stream().flatMap(r-> Stream.concat(Stream.of(new SimpleGrantedAuthority(r.getName())),
						r.getPermissions().stream().map(p-> new SimpleGrantedAuthority(p.getName())))).collect(Collectors.toList()));
		return ud;
	}
}
