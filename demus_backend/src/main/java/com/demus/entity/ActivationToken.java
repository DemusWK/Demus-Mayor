/**
 * 
 */
package com.demus.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Activation token to activate a subscriber
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity
@Table (name = "ACTIVATION_TOKEN", uniqueConstraints = @UniqueConstraint (columnNames = {"SUBSCRIBER_ID", "TOKEN_VALUE", "ACTIVATION_TOKEN_TYPE"}))
public class ActivationToken extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "SUBSCRIBER_ID", nullable = false, referencedColumnName = "ID")
	private Subscriber subscriber;
	
	@Size (max = 255)
	@NotNull
	@Column (name = "TOKEN_VALUE", nullable = false)
	private String value;
	
	@Enumerated (EnumType.STRING)
	@NotNull
	@Column (name = "ACTIVATION_TOKEN_TYPE", nullable = false)
	private ActivateTokenType type;
	
	@NotNull
	@Column (name = "EXPIRY_DATE", nullable = false)
	@Temporal (TemporalType.TIMESTAMP)
	private Date expiryDate;
	
	public ActivationToken () {}

	public ActivationToken(Subscriber subscriber2, String generateEmailToken, ActivateTokenType type) {
		setSubscriber(subscriber2);
		setValue(generateEmailToken);
		setType(type);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 30);
		setExpiryDate(calendar.getTime());
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ActivateTokenType getType() {
		return type;
	}

	public void setType(ActivateTokenType type) {
		this.type = type;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

}
