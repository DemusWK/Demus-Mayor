/**
 * 
 */
package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * This represents every request sent to mobifin and responses
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 2 Apr 2016
 */
@Entity
@Table (name = "VENDOR_API_LOG")
public class VendorApiLog extends Persistent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "INITIATOR_ID", referencedColumnName="ID", nullable = false)
	private Subscriber owner;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "SUBSCRIPTION_ID",  referencedColumnName="ID", nullable = false)
	private Subscription subscription;

	@NotNull
	@Column (name = "BENEFIARY_PHONE_NUMBER", nullable = false)
	private String phoneNumber;
	
	@NotNull
	@Column (name = "BATCH_ID", nullable = false)
	private String batchId;
	
	@NotNull
	@Column (name = "AIRTIME_AMOUNT", nullable = false)
	private Double amount;
	
	@Column (name = "RESPONSE_CODE", nullable = true)
	private String responseCode;
	
	@Column (name = "AUDIT_ID", nullable = true)
	private String auditId;
	
	private String confirmationCode;
	
	private boolean isPrepaid;
	
	public VendorApiLog () {};
	
	public VendorApiLog (Subscriber owner, String phoneNumber, String batchId, Double amount, Subscription subscription) {
		setOwner(owner);
		setPhoneNumber(phoneNumber);
		setBatchId (batchId);
		setAmount(amount);
		setSubscription(subscription);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public Subscriber getOwner() {
		return owner;
	}

	public void setOwner(Subscriber owner) {
		this.owner = owner;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public boolean isPrepaid() {
		return isPrepaid;
	}

	public void setPrepaid(boolean isPrepaid) {
		this.isPrepaid = isPrepaid;
	}

}
