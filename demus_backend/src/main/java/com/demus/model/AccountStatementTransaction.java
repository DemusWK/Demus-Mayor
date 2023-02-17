package com.demus.model;

import com.demus.entity.WalletTransaction;

public class AccountStatementTransaction {	
	public AccountStatementTransaction () {}
	public AccountStatementTransaction(WalletTransaction transaction) {
		// TODO Auto-generated constructor stub
	}
	
	public String getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(String debitAmount) {
		this.debitAmount = debitAmount;
	}

	public String getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getBalanceAfterTransaction() {
		return balanceAfterTransaction;
	}
	public void setBalanceAfterTransaction(String balanceAfterTransaction) {
		this.balanceAfterTransaction = balanceAfterTransaction;
	}

	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommissionAmount() {
		return commissionAmount;
	}
	public void setCommissionAmount(String commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	private String debitAmount;
	private String creditAmount;
	private String commissionAmount;
	private String transactionId;
	private String balanceAfterTransaction;
	private String createdDate;
	private String description;
}