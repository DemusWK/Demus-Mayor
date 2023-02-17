package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Any product that can be sold on eclever mobile
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "PRODUCT", uniqueConstraints = {@UniqueConstraint(columnNames = {"code", "category_id"})})
public class Product extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size (max = 255)
	@Column (name  = "CODE", nullable = false)
	private String productCode;
	
	@NotNull
	@Column (name  = "MIN_PURCHASE_QUANTITY", nullable = false)
	private Integer minPurchaseQuantity;
	
	@Size (max = 255)
	@Column (name  = "ICON_URL", nullable = true)
	private String iconUrl;
	
	@NotNull
	@Size (max = 255)
	@Column (name  = "NAME", nullable = false)
	private String name;
	
	@Size (max = 255)
	@Column (name  = "DESCRIPTION", nullable = true)
	private String description;
	
	@Column (name  = "cost_price", nullable = false)
	private Double costPrice;
	
	@NotNull
	@Column (name  = "PRICE", nullable = false)
	private Double price;
	
	@NotNull
	@Column (name = "STOCK_COUNT", nullable = false)
	private Long stockCount;
	
	@NotNull
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name  = "CATEGORY_ID", referencedColumnName = "ID", nullable = false)
	private ProductCategory category;
	
	@NotNull
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name  = "COUNTRY_ID", referencedColumnName = "ID", nullable = false)
	private Country country;
	
	@NotNull
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name  = "VENDOR_ID", referencedColumnName = "ID", nullable = false)
	private Vendor vendor;
	
	@NotNull
	@Column (name = "COMMISSION", nullable = false)
	private Double commission;
	
	public Product () {}

	public Product(Long productId) {
		setId(productId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Long getStockCount() {
		return stockCount;
	}

	public void setStockCount(Long stockCount) {
		this.stockCount = stockCount;
	}

//	public Vendor getVendor() {
//		return vendor;
//	}
//
//	public void setVendor(Vendor vendor) {
//		this.vendor = vendor;
//	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getMinPurchaseQuantity() {
		return minPurchaseQuantity;
	}

	public void setMinPurchaseQuantity(Integer minPurchaseQuantity) {
		this.minPurchaseQuantity = minPurchaseQuantity;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	
}
