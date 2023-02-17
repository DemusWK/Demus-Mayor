package com.demus.model;

public class PaystackAuthCodeChargeResponse {
	
	private boolean isSuccess;
	
	private String reference;
	
	public PaystackAuthCodeChargeResponse() {
		isSuccess = false;
	}

	public boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}
