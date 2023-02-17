/**
 * 
 */
package com.demus.entity;

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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Promo code entity allows subscribers discount their order
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "PROMO_CODE")
public class PromoCode extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	@Size (max = 255)
	@Column (name = "CODE", nullable = false, unique = true, updatable = false)
	private String code;
	
	@NotNull
	@Min (value = 0)
	@Column (name = "VALUE", nullable = false) 
	private Double value;
	
	@NotNull
	@Column (name = "TYPE", nullable = false)
	@Enumerated (EnumType.STRING)
	private PromoCodeType type;
	
	@NotNull
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "PRODUCT_ID", nullable = true, referencedColumnName = "ID")
	private Product product;
	
	@NotNull
	@Column (name = "EXPIRY_DATE", nullable = false)
	@Temporal (TemporalType.TIMESTAMP)
	private Date expiryDate;
	
	@NotNull
	@Column (name = "MIN_QUANTITY", nullable = false) 
	private Integer minQuantity;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PromoCodeType getType() {
		return type;
	}

	public void setType(PromoCodeType type) {
		this.type = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getMinQuantity() {
		return minQuantity;
	}

	public void setMinQuantity(Integer minQuantity) {
		this.minQuantity = minQuantity;
	}

}
