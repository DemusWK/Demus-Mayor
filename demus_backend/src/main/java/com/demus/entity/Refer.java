/**
 * 
 */
package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * This entity represents a none registered user referred on the eClever Mobile Platform
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 11 Mar 2016
 */
@Entity
@Table (name = "REFER")
public class Refer extends Persistent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "REFEREE_ID", referencedColumnName = "ID", nullable = false, updatable = false)
	private Subscriber subscriber; 
	
	@NotNull
	@Column (name = "NAME", nullable = false, length = 11, unique = true)
	private String phoneNumber;
	
	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "TRANSACTION_ID", unique = true, nullable = false, updatable = false)
	private WalletTransaction transaction;
	
	public Refer () {}

	public Refer(Subscriber subscriber, String phoneNumber2, WalletTransaction transaction2, EntityStatus entityStatus) {
		setSubscriber(subscriber);
		setPhoneNumber(phoneNumber2);
		setTransaction(transaction2);
		setEntityStatus(entityStatus);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public WalletTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(WalletTransaction transaction) {
		this.transaction = transaction;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
	
}
