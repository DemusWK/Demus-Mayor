package com.demus.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "PLATFORM_CONFIGURATION")
public class PlatformConfiguration extends Persistent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double cashBackFixedCharge;
	
	private Double cashBackPercentageCharge;
	
	private Double minimumAmountForCashbackPercentageCharge;
	
	private Double maximumAmountForCashbackPercentageCharge;
	
	private Double minimumAmountForCashBackFixedCharge;
	
	private Double maximumAmountForCashBackFixedCharge;
	
	private Double pinLoaderFixedCharge;
	
	private Double pinLoaderPercentageCharge;
	
	private Double minimumAmountForPinLoaderFixedCharge;
	
	private Double maximumAmountForPinLoaderFixedCharge;
	
	private Double minimumAmountForPinLoaderPercentageCharge;
	
	private Double maximumAmountForPinLoaderPercentageCharge;
	
	private Double accStatementSixMonthCharge = 0.00;
	
	private Double accStatementChargeOneMonth = 0.00;
	
	private Double accStatementChargeThreeMonth = 0.00;

	public Double getCashBackFixedCharge() {
		return cashBackFixedCharge;
	}

	public void setCashBackFixedCharge(Double cashBackFixedCharge) {
		this.cashBackFixedCharge = cashBackFixedCharge;
	}

	public Double getCashBackPercentageCharge() {
		return cashBackPercentageCharge;
	}

	public void setCashBackPercentageCharge(Double cashBackPercentageCharge) {
		this.cashBackPercentageCharge = cashBackPercentageCharge;
	}

	public Double getPinLoaderFixedCharge() {
		return pinLoaderFixedCharge;
	}

	public void setPinLoaderFixedCharge(Double pinLoaderFixedCharge) {
		this.pinLoaderFixedCharge = pinLoaderFixedCharge;
	}

	public Double getMinimumAmountForCashbackPercentageCharge() {
		return minimumAmountForCashbackPercentageCharge;
	}

	public void setMinimumAmountForCashbackPercentageCharge(Double minimumAmountForCashbackPercentageCharge) {
		this.minimumAmountForCashbackPercentageCharge = minimumAmountForCashbackPercentageCharge;
	}

	public Double getPinLoaderPercentageCharge() {
		return pinLoaderPercentageCharge;
	}

	public void setPinLoaderPercentageCharge(Double pinLoaderPercentageCharge) {
		this.pinLoaderPercentageCharge = pinLoaderPercentageCharge;
	}

	public Double getMinimumAmountForPinLoaderPercentageCharge() {
		return minimumAmountForPinLoaderPercentageCharge;
	}

	public void setMinimumAmountForPinLoaderPercentageCharge(Double minimumAmountForPinLoaderPercentageCharge) {
		this.minimumAmountForPinLoaderPercentageCharge = minimumAmountForPinLoaderPercentageCharge;
	}

	public Double getMaximumAmountForCashbackPercentageCharge() {
		return maximumAmountForCashbackPercentageCharge;
	}

	public void setMaximumAmountForCashbackPercentageCharge(Double maximumAmountForCashbackPercentageCharge) {
		this.maximumAmountForCashbackPercentageCharge = maximumAmountForCashbackPercentageCharge;
	}

	public Double getMinimumAmountForCashBackFixedCharge() {
		return minimumAmountForCashBackFixedCharge;
	}

	public void setMinimumAmountForCashBackFixedCharge(Double minimumAmountForCashBackFixedCharge) {
		this.minimumAmountForCashBackFixedCharge = minimumAmountForCashBackFixedCharge;
	}

	public Double getMinimumAmountForPinLoaderFixedCharge() {
		return minimumAmountForPinLoaderFixedCharge;
	}

	public void setMinimumAmountForPinLoaderFixedCharge(Double minimumAmountForPinLoaderFixedCharge) {
		this.minimumAmountForPinLoaderFixedCharge = minimumAmountForPinLoaderFixedCharge;
	}

	public Double getMaximumAmountForPinLoaderFixedCharge() {
		return maximumAmountForPinLoaderFixedCharge;
	}

	public void setMaximumAmountForPinLoaderFixedCharge(Double maximumAmountForPinLoaderFixedCharge) {
		this.maximumAmountForPinLoaderFixedCharge = maximumAmountForPinLoaderFixedCharge;
	}

	public Double getMaximumAmountForPinLoaderPercentageCharge() {
		return maximumAmountForPinLoaderPercentageCharge;
	}

	public void setMaximumAmountForPinLoaderPercentageCharge(Double maximumAmountForPinLoaderPercentageCharge) {
		this.maximumAmountForPinLoaderPercentageCharge = maximumAmountForPinLoaderPercentageCharge;
	}

	public Double getMaximumAmountForCashBackFixedCharge() {
		return maximumAmountForCashBackFixedCharge;
	}

	public void setMaximumAmountForCashBackFixedCharge(Double maximumAmountForCashBackFixedCharge) {
		this.maximumAmountForCashBackFixedCharge = maximumAmountForCashBackFixedCharge;
	}

	public Double getAccStatementSixMonthCharge() {
		return accStatementSixMonthCharge;
	}

	public void setAccStatementSixMonthCharge(Double accStatementSixMonthCharge) {
		this.accStatementSixMonthCharge = accStatementSixMonthCharge;
	}

	public Double getAccStatementChargeOneMonth() {
		return accStatementChargeOneMonth;
	}

	public void setAccStatementChargeOneMonth(Double accStatementChargeOneMonth) {
		this.accStatementChargeOneMonth = accStatementChargeOneMonth;
	}

	public Double getAccStatementChargeThreeMonth() {
		return accStatementChargeThreeMonth;
	}

	public void setAccStatementChargeThreeMonth(Double accStatementChargeThreeMonth) {
		this.accStatementChargeThreeMonth = accStatementChargeThreeMonth;
	}

}
