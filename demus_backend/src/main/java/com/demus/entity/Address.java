package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.demus.api.system.Config;

@Entity
@Table (name = "ADDRESS")
public class Address extends Persistent {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne 
	@JoinColumn (name = "SUBSCRIBER_ID", referencedColumnName = "ID")
	@NotNull
	private Subscriber subscriber;
	
	@Column (name = "IS_PRIMARY_ADDRESS")
	@NotNull
	private Boolean primary;
	
	@Column (name = "ADDRESS", length = Config.MAX_ADDRESS, nullable = false)
	@NotNull
	@Size (max = Config.MAX_ADDRESS)
	private String address;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "STATE_ID", referencedColumnName = "ID")
	@NotNull
	private State state;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getPrimary() {
		return primary;
	}

	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

}
