/**
 * 
 */
package com.demus.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * Set of roles assigned to a subscriber	
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity
@Table(name = "SUBSCRIBER_ROLE", uniqueConstraints=
	        @UniqueConstraint(columnNames={"subscriber_ID", "role_ID"}))
public class SubscriberRole extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "SUBSCRIBER_ID", nullable = false, referencedColumnName = "ID")
	private Subscriber subscriber;
	
	@NotNull
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "ROLE_ID", nullable = false, referencedColumnName = "ID")
	private Role role;

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}
