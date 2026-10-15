package com.bank.authservice.security;

import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

public class OtpAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken{
	private final String userName;
	private final String otp;
	private final String ip;
	private final String deviceId;
	
	public OtpAuthenticationToken(Authentication clientPrinciple, Set<String> scopes, Map<String, Object> additionalParams) {
		super(new AuthorizationGrantType("otp"), clientPrinciple, additionalParams);
		this.userName = (String)additionalParams.get("userName");
		this.otp = (String) additionalParams.get("otp");
		this.ip = (String) additionalParams.get("ip");
		this.deviceId = (String) additionalParams.get("deviceId");
	}

	public String getUserName() {
		return userName;
	}
	public String getOtp() {
		return otp;
	}
	public String getIp() {
		return ip;
	}
	public String getDeviceId() {
		return deviceId;
	}
}
