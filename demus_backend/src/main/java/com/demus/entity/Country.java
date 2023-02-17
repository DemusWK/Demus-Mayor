package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity 
@Table (name = "COUNTRY", indexes = {@Index(columnList = "name")})
/**
 * A country in the world
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public class Country extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6017390932022124071L;

	@Column (name = "NAME", nullable = false)
	@NotNull
	@Size (max = 255)
	private String name;
	
	@Column (name = "CURRENCY", nullable = false)
	@NotNull
	private String currency;
	
	@Column (name = "COUNTRY_CODE", nullable = false)
	@NotNull
	private String countryCode;
	
	@Column (name = "INT_CALLING_CODE", nullable = false)
	@NotNull
	private String intCallingCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getIntCallingCode() {
		return intCallingCode;
	}

	public void setIntCallingCode(String intCallingCode) {
		this.intCallingCode = intCallingCode;
	}

}
