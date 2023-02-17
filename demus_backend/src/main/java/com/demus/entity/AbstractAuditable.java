/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demus.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.demus.Exclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Abstract base class for auditable entities. Stores the audition values in persistent fields.
 * 
 * @author Oliver Gierke
 * @param <U> the auditing type. Typically some kind of user.
 * @param <PK> the type of the auditing type's idenifier
 */
@MappedSuperclass
public abstract class AbstractAuditable<U extends AbstractPersistable<PK>, PK extends Serializable> extends AbstractPersistable<PK> implements
		Auditable<U, PK> {

	private static final long serialVersionUID = 141481953116476081L;

	@Exclude @JsonIgnore
	@ManyToOne
	private U createdBy;
	
	@Transient
	private PK createById;

	@JsonInclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@ManyToOne
	@Exclude @JsonIgnore
	private U lastModifiedBy;
	
	@Transient
	private PK lastModifiedById;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Auditable#getCreatedBy()
	 */
	@Override
	public U getCreatedBy() {

		return createdBy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.domain.Auditable#setCreatedBy(java.lang.Object)
	 */
	@Override
	public void setCreatedBy(final U createdBy) {
		this.createdBy = createdBy;
		if (createdBy != null)
			createById = getCreatedBy().getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Auditable#getCreatedDate()
	 */
	@Override
	public DateTime getCreatedDate() {

		return null == createdDate ? null : new DateTime(createdDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.domain.Auditable#setCreatedDate(org.joda.time
	 * .DateTime)
	 */
	@Override
	public void setCreatedDate(final DateTime createdDate) {

		this.createdDate = null == createdDate ? null : createdDate.toDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Auditable#getLastModifiedBy()
	 */
	@Override
	public U getLastModifiedBy() {

		return lastModifiedBy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.domain.Auditable#setLastModifiedBy(java.lang
	 * .Object)
	 */
	@Override
	public void setLastModifiedBy(final U lastModifiedBy) {
		
		this.lastModifiedBy = lastModifiedBy;
		if (lastModifiedBy != null)
			this.lastModifiedById = lastModifiedBy.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Auditable#getLastModifiedDate()
	 */
	@Override
	public DateTime getLastModifiedDate() {

		return null == lastModifiedDate ? null : new DateTime(lastModifiedDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.domain.Auditable#setLastModifiedDate(org.joda
	 * .time.DateTime)
	 */
	@Override
	public void setLastModifiedDate(final DateTime lastModifiedDate) {

		this.lastModifiedDate = null == lastModifiedDate ? null : lastModifiedDate.toDate();
	}
}