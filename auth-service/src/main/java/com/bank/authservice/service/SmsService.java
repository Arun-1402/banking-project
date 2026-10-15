package com.bank.authservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;


@Service
public class SmsService {

	@Value("${twilio.sid}")
	private String accountId;
	@Value("${twilio.token}")
	private String authToken;
	@Value("${twilio.number}")
	private String fromNumber;
	@PostConstruct
	public void init() {
		Twilio.init( accountId,authToken);
	}
	
	public void sendOtp(String phoneNumber, String otp) {
		Message msg =Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(fromNumber),"Internal Alerts / Workforce Notifications"+otp).create();
		
		System.out.println("Message sid====>> "+msg.getSid());
		System.out.println("Message body====>> "+msg.getBody());
		System.out.println("Message error====>> "+msg.getErrorMessage());
	}
}
