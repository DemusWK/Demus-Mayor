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
import javax.validation.constraints.Size;

/**
 * Any institution with capability to store and forward funds
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "BANK_OPERATOR")
public class BankOperator extends Persistent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column (name = "NAME", nullable = false, length = 255)
	@NotNull
	@Size (max = 255)
	private String name;
	
	@Column (name = "CODE", nullable = false, unique = true)
	@NotNull
	private String bankOperatorCode;
	
	@Column (name = "SECURE_COMMUNICATION_KEY", nullable = true, length = 255)
	@Size (max = 255)
	private String bankCommunicationKey;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "COUNTRY_ID", nullable = false, referencedColumnName = "ID")
	@NotNull
	private Country country;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBankOperatorCode() {
		return bankOperatorCode;
	}

	public void setBankOperatorCode(String bankOperatorCode) {
		this.bankOperatorCode = bankOperatorCode;
	}

	public String getBankCommunicationKey() {
		return bankCommunicationKey;
	}

	public void setBankCommunicationKey(String bankCommunicationKey) {
		this.bankCommunicationKey = bankCommunicationKey;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
}
