package com.bank.authservice.security;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class OtpAuthenticationConvertor implements AuthenticationConverter{

	@Override
	public Authentication convert(HttpServletRequest request) {
		String grantType = request.getParameter("grant_type");
		if (!"otp".equals(grantType)) {
			return null;
		}
		String userName = request.getParameter("userName");
		String otp = request.getParameter("otp");
		String ip = request.getRemoteAddr();
		String deviceId = request.getHeader("X-Device-Id");
		Authentication clientPrinciple = SecurityContextHolder.getContext().getAuthentication();
		return new OtpAuthenticationToken(clientPrinciple, null, Map.of("userName",userName,"otp",otp,
				"ip",ip, "deviceId",deviceId));
	}

}
