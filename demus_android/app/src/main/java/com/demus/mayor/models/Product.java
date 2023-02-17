package com.demus.mayor.models;

import java.math.BigDecimal;

public class Product {

	String mobileNetworkOperator, receiverNumber, promoCode, pin;
	
	BigDecimal amount;
	
	int type;
	
	public static final int AIRTIME_PRODUCT_TYPE = 1;
	public static final int REFERENT_PRODUCT_TYPE = 2;
	public static final int DATA_PRODUCT_TYPE = 3;

	public String getMobileNetworkOperator() {
		return mobileNetworkOperator;
	}

	public void setMobileNetworkOperator(String mobileNetworkOperator) {
		this.mobileNetworkOperator = mobileNetworkOperator;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getReceiverNumber() {
		return receiverNumber;
	}

	public void setReceiverNumber(String receiverNumber) {
		this.receiverNumber = receiverNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
