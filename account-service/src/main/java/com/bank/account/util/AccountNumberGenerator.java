package com.bank.account.util;

import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

	public String generator(String branchCode,String typeCode, long sequence) {
		String seq = String.format("%010d", sequence);
		String base = branchCode.concat(typeCode).concat(seq);
		int checkSum = calculateCheckDigit(base);
		return base+checkSum;
	}
	
	private int calculateCheckDigit(String number) {
		int sum =0;
		for(int i=0; i<number.length();i++) {
			int n = Character.getNumericValue(number.charAt(i));
			if (i%2 == 0) {
				n*=2;
				if (n>9) {
					n-=9;
				}
				
			}
			sum+=n;
			
		}
		return   (10-(sum%10))%10;
	}
}
