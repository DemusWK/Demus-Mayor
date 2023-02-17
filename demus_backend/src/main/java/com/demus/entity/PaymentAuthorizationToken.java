/**
 * 
 */
package com.demus.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.demus.Exclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Temporary authorization token used to make payments
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "PAYMENT_AUTHORIZATION_TOKEN")
public class PaymentAuthorizationToken extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "TOKEN", nullable = false, unique = true)
	@Exclude
	private String token;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "PAYMENT_REFERENCE", nullable = false, unique = true)
	private String paymentReference;
	
	@ManyToOne
	@JoinColumn (name = "WALLET_TRANSACTION_ID", nullable = false, referencedColumnName = "ID")
	@Exclude
	@JsonIgnore
	private WalletTransaction transaction;
	
	@NotNull
	@Column (name = "EXPIRY_DATE", nullable = false, unique = true)
	@Temporal (TemporalType.TIMESTAMP)
	private Date expiryDate;
	
	@Column (name = "AUTHORIZATION_DATE", nullable = true, unique = true)
	@Temporal (TemporalType.TIMESTAMP)
	@Exclude
	private Date authorizationDate;
	
	public PaymentAuthorizationToken () {}
	
	public PaymentAuthorizationToken (String secureToken, String paymentReference, WalletTransaction transaction, Date expiryDate, EntityStatus status) {
		setToken(secureToken);
		setPaymentReference(paymentReference);
		setTransaction(transaction);
		setExpiryDate(expiryDate);
		setEntityStatus(status);
	}


	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public WalletTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(WalletTransaction transaction) {
		this.transaction = transaction;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getAuthorizationDate() {
		return authorizationDate;
	}

	public void setAuthorizationDate(Date authorizationDate) {
		this.authorizationDate = authorizationDate;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

}
