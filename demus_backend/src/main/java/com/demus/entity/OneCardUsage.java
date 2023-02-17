package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity 
@Table(name = "ONE_CARD_USAGE", uniqueConstraints=
	        @UniqueConstraint(columnNames={"pin"}))
public class OneCardUsage extends Persistent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column (name = "pin", nullable = false)
	private String pin;
	
	private Double amount;
	
	private String transactionId;
	
	private WalletTransaction transcation;
	
	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public WalletTransaction getTranscation() {
		return transcation;
	}

	public void setTranscation(WalletTransaction transcation) {
		this.transcation = transcation;
	}
	
	

}
