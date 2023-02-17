package com.demus.model;

import java.util.Date;

public class CreditMeConnectionResponse {
	public long status;
	
	Date date;
	
	public String message, state, responseCode;
	
	public CreditMeConnectionResponse() {
		message = "Low Wallet Fund";
		status = -1;
		date = new Date();
		state = "PURCHASE";
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
}
