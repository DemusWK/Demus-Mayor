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
 * A subscription request for a product by quantity
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "SUBSCRIPTION")
public class Subscription extends Persistent {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "ORDER_ID", referencedColumnName = "ID", nullable = false, updatable = false)
	private Order order;
	
	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "PRODUCT_ID", referencedColumnName = "ID", nullable = false, updatable = false)
	private Product product;
	
	@Column (name = "QUANTITY", updatable = false)
	private Integer quantity;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "PROMO_CODE", nullable = true, referencedColumnName = "ID", updatable = false)
	private PromoCode promoCode;
	
	@NotNull
	@Column (name = "COST", nullable = false, updatable = false)
	private Double cost;
	
	public Subscription () {}
	
	public Subscription (Order order, Product product, Integer quantity, PromoCode promoCode, Double cost, EntityStatus entityStatus) {
		setOrder(order);
		setProduct(product);
		setQuantity(quantity);
		setPromoCode(promoCode);
		setCost(cost);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public PromoCode getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(PromoCode promoCode) {
		this.promoCode = promoCode;
	}

}
