/**
 * 
 */
package com.demus.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.demus.Exclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Temporary authorization token used to make payments
 * @author Lekan Baruwa
 */
@Entity 
@Table (name = "PAYSTACK_AUTHORIZATION_TOKEN")
public class PaystackAuthorizationToken extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "AUTHORIZATION_CODE", nullable = false)
	@Exclude
	@JsonIgnore
	private String authorizationCode;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "BANK", nullable = false)
	private String bank;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "CARD_TYPE", nullable = false)
	private String cardType;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "LAST_FOUR", nullable = false)
	private String lastFour;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "BIN", nullable = false)
	private String bin;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "BRAND", nullable = true)
	private String brand;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "CHANNEL", nullable = false)
	@Exclude
	@JsonIgnore
	private String channel;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "SIGNATURE", nullable = false)
	@Exclude
	@JsonIgnore
	private String signature;
	
	@NotNull
	@Column (name = "REUSABLE", nullable = false)
	@Exclude
	@JsonIgnore
	private boolean reusable;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "COUNTRY_CODE", nullable = false)
	private String countryCode;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "EXP_MONTH", nullable = false)
	@Exclude
	@JsonIgnore
	private String expMonth;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "EXP_YEAR", nullable = false)
	@Exclude
	@JsonIgnore
	private String expYear;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "SUBSCRIBER_ID", referencedColumnName = "ID", nullable = false)
	@JsonIgnore
	private Subscriber subscriber;
	
	@NotNull
	@Column (name = "DATE_CREATED", nullable = false)
	private Date dateCreated;
	
	public PaystackAuthorizationToken () {}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getLastFour() {
		return lastFour;
	}

	public void setLastFour(String lastFour) {
		this.lastFour = lastFour;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public boolean isReusable() {
		return reusable;
	}

	public void setReusable(boolean reusable) {
		this.reusable = reusable;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
}
