package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A registered subscriber device on the platform
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity
@Table (name = "DEVICE", uniqueConstraints = { 
		@UniqueConstraint (columnNames = {"DEVICE_OS", "CLIENT_ID"})})
public class Device extends Persistent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Device () {}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getPushNotificationKey() {
		return pushNotificationKey;
	}

	public void setPushNotificationKey(String pushNotificationKey) {
		this.pushNotificationKey = pushNotificationKey;
	}

	public DeviceOs getDeviceOs() {
		return deviceOs;
	}

	public void setDeviceOs(DeviceOs deviceOs) {
		this.deviceOs = deviceOs;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	@NotNull
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "SUBSCRIBER_ID", referencedColumnName = "ID", nullable = false)
	private Subscriber subscriber;

	@NotNull
	@Column (name = "DEVICE_OS", nullable = false, updatable = false)
	@Enumerated (EnumType.STRING)
	private DeviceOs deviceOs;
	
	@Size (max = 255)
	@Column (name = "PUSH_NOTIFICATION_KEY")
	private String pushNotificationKey;
	
	@NotNull
	@Column (name = "CLIENT_ID", nullable = false, updatable = false)
	private String clientId;
	
	@NotNull
	@Column (name = "APPLICATION_VERSION", nullable = false)
	private Integer version;
	
	public Device (String clientId, DeviceOs clientOs, Integer version, String pushNotificationKey, Subscriber subscriber) {
		setClientId(clientId);
		setDeviceOs(clientOs);
		setVersion(version);
		setPushNotificationKey(pushNotificationKey);
		setSubscriber(subscriber);
	}
	
}
