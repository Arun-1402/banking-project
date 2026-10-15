package com.bank.authservice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;
	
	public void sendOtp(String email, String otp) {
		System.out.println("email=> "+email+" otp==> "+otp);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setSubject("OTP");
		mailMessage.setText("Your OTP is : "+otp);
		mailSender.send(mailMessage);
	}
}
