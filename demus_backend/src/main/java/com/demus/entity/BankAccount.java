/**
 * 
 */
package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.demus.api.system.Config;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A persons bank account
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile Limited
 */
@Entity 
@Table(name = "BANK_ACCOUNT", uniqueConstraints=
	        @UniqueConstraint(columnNames={"ACCOUNT_NUMBER", "SUBSCRIBER_ID", "BANK_OPERATOR_ID"}))
public class BankAccount extends Persistent {
	
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size (max = Config.MAX_ACCOUNT_NUMBER)
	@Column (name = "ACCOUNT_NUMBER", nullable = false, length = Config.MAX_ACCOUNT_NUMBER)
	private String accountNumber;
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "SUBSCRIBER_ID", referencedColumnName = "ID", nullable = false)
	@NotNull
	@JsonIgnore
	private Subscriber subscriber;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "BANK_OPERATOR_ID", referencedColumnName = "ID", nullable = false)
	@NotNull
	private BankOperator bankOperator;
	
	@Enumerated (EnumType.STRING)
	@NotNull
	@Column (name = "BANK_ACCOUNT_TYPE", nullable = false,updatable = true)
	private BankAccountType type;
	
	@NotNull
	@Column (name = "CASH_BACK_VALUE", nullable = true)
	private Double cashBackValue = 0.00;
	
	// The first account to receive a cashback
	@NotNull
	@Column (name = "IS_PRIMARY_ACCOUNT", nullable = true)
	private Boolean isPrimary;
	
	public BankAccount () {}

	public BankAccount(String bankAccountNumber, BankOperator operator, Subscriber subscriber2, BankAccountType bankAccountType, boolean isPrimary) {
		setAccountNumber(bankAccountNumber);
		setBankOperator(operator);
		setSubscriber(subscriber2);
		setType(bankAccountType);
		setIsPrimary(isPrimary);
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber (Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public BankOperator getBankOperator() {
		return bankOperator;
	}

	public void setBankOperator(BankOperator bankOperator) {
		this.bankOperator = bankOperator;
	}

	public Double getCashBackValue() {
		return cashBackValue;
	}

	public void setCashBackValue(Double cashBackValue) {
		this.cashBackValue = cashBackValue;
	}

	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public BankAccountType getType() {
		return type;
	}

	public void setType(BankAccountType type) {
		this.type = type;
	} 
		
}
