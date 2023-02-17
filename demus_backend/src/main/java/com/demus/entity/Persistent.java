package com.demus.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Parent class for all id persistable objects.
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class Persistent extends AbstractAuditable<Subscriber, Long> implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1079444957779273224L;
	
	public Date getCreateDate () {
		return getCreatedDate().toDate();
	}
	
	@Column (name = "STATUS")
	@NotNull
	@Enumerated (EnumType.STRING)
	private EntityStatus entityStatus = EntityStatus.ACTIVE;
	
	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}
	
	@JsonInclude
	public String getTime () {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		return format.format(getCreateDate());
	}
	
}
