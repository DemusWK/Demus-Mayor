/**
 * 
 */
package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A vendor is responsible for fulfilling orders
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "VENDOR")
public class Vendor extends Persistent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "NAME", nullable = false, unique = false)
	private String name;

	@NotNull
	@Column (name = "CODE", nullable = false, unique = true)
	private String vendorCode;
	
	@NotNull
	@Column (name = "VENDOR_API_URL", nullable = true)
	private String apiUrl;

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
}
