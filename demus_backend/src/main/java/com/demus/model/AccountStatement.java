package com.demus.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountStatement {
	private String startDate;
	private String endDate;
	private String openingBalance;
	private String closingBalance;
	private String commissionSum;
	private String debitSum;
	private String creditSum;
	
	private Logger logger = LoggerFactory.getLogger(AccountStatement.class);
	
	public boolean log (String string) {
		logger.debug(string);
		return true;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(String openingBalance) {
		this.openingBalance = openingBalance;
	}

	public String getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(String closingBalance) {
		this.closingBalance = closingBalance;
	}

	public String getCommissionSum() {
		return commissionSum;
	}

	public void setCommissionSum(String commissionSum) {
		this.commissionSum = commissionSum;
	}

	public String getDebitSum() {
		return debitSum;
	}

	public void setDebitSum(String debitSum) {
		this.debitSum = debitSum;
	}

	public String getCreditSum() {
		return creditSum;
	}

	public void setCreditSum(String creditSum) {
		this.creditSum = creditSum;
	}
}
