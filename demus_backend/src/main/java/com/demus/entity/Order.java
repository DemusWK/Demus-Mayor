package com.demus.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.demus.Exclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An order for a list of products at a given time
 * 
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "PRODUCT_ORDER")
public class Order extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "ORDER_ID", nullable = false)
	private String orderId;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "SUBSCRIBER_ID", referencedColumnName = "ID", nullable = false)
	private Subscriber subscriber;
	
	@NotNull
	@Column (name = "COST", nullable = false, updatable = true)
	private Double cost;
	
	@NotNull
	@Column (name = "ORDER_STATUS", nullable = false, updatable = true)
	@Enumerated (EnumType.STRING)
	private OrderStatus orderStatus;
	
	@NotNull
	@Column (name = "RETAIL_CHANNEL", nullable = false, updatable = false)
	@Enumerated (EnumType.STRING)
	private DeviceOs retailChannel;
	
	@OneToMany(mappedBy="order", fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@Exclude
	@JsonIgnore
	private List<Subscription> subscriptions;
	
	public Order () {} 
	
	public Order (String orderId, Double cost, OrderStatus orderStatus, DeviceOs retailChannel, EntityStatus status, Subscriber subscriber) {
		setOrderId(orderId);
		setCost(cost);
		setOrderStatus(orderStatus);
		setRetailChannel(retailChannel);
		setEntityStatus(status);
		setSubscriber(subscriber);
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public DeviceOs getRetailChannel() {
		return retailChannel;
	}

	public void setRetailChannel(DeviceOs retailChannel) {
		this.retailChannel = retailChannel;
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}	
	
}
