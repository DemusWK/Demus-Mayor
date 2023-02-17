package com.demus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Company {
	
	@Value ("${com.demus.company.name}")
	private String companyName;

	@Value ("${com.demus.company.phoneNumber}")
	private String phoneNumber;
	
	@Value ("${com.demus.company.email}")
	private String email;
	
	@Value ("${com.demus.company.address}")
	private String address;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
