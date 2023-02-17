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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.demus.Exclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An Eclever Wallet
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity
@Table (name = "WALLET")
public class Wallet extends Persistent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "WALLET_ID", nullable=false, unique = true)
	private String walletId;
	
	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "SUBSCRIBER_ID", nullable=false, unique = true)
	@Exclude 
	@JsonIgnore (value=true)
	private Subscriber subscriber;
	
	@Column (name = "BONUS_BALANCE")
	private Double bonusBalance = 0.00;
	
	@NotNull
	@Column (name = "WALLET_BALANCE", nullable = false)
	private Double balance = 0.00;
	
	@NotNull
	@Column (name = "MOBILE_NETWORK_OPERATOR", nullable = false)
	@Enumerated (EnumType.STRING)
	private MobileNetworkOperator mobileNetworkOperator;
	
	@Column (name = "MIN_BALANCE_TRIGGER", nullable = false)
	private Double minimumBalanceTrigger = 100.00;
	
	public Wallet () {}
	
	public Wallet (String walletId, Subscriber subscriber, MobileNetworkOperator mobileNetworkOperator) {
		setWalletId(walletId);
		setSubscriber(subscriber);
		setMobileNetworkOperator(mobileNetworkOperator);
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double stockWalletBalance) {
		this.balance = stockWalletBalance;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public MobileNetworkOperator getMobileNetworkOperator() {
		return mobileNetworkOperator;
	}

	public void setMobileNetworkOperator(MobileNetworkOperator mobileNetworkOperator) {
		this.mobileNetworkOperator = mobileNetworkOperator;
	}

	public Double getMinimumBalanceTrigger() {
		return minimumBalanceTrigger;
	}

	public void setMinimumBalanceTrigger(Double minimumBalanceTrigger) {
		this.minimumBalanceTrigger = minimumBalanceTrigger;
	}

	public Double getBonusBalance() {
		return bonusBalance;
	}

	public void setBonusBalance(Double bonusBalance) {
		this.bonusBalance = bonusBalance;
	}
	
}
