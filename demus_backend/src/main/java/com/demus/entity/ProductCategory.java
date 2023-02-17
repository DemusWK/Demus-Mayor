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
 * A category for products 
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity
@Table (name = "PRODUCT_CATEGORY")
public class ProductCategory extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "NAME", nullable = false)
	private String name;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "CODE", nullable = false, unique = true)
	private String categoryCode;
	
	@Column (name = "ORDER_INDEX", nullable = false)
	@NotNull
	private String orderIndex;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "PARENT_CATEGORY_ID", referencedColumnName = "ID", nullable = true)
	private ProductCategory parentCategory;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(String orderIndex) {
		this.orderIndex = orderIndex;
	}

	public ProductCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(ProductCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

}
