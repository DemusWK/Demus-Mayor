/**
 * 
 */
package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Fine grained permission object
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "PERMISSION")
public class Permission extends Persistent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column (name = "NAME", nullable = false)
	private String name;
	
	@NotNull
	@Column (name = "DESCRIPTION", nullable = false)
	private String description;
	
	public Permission () {}

	public Permission(String name) {
		setName(name);
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
	
	@Override
	public boolean equals (Object o) {
		if (o == null || !(o instanceof Permission))
			return false;
		Permission current = (Permission) o;
		if (current.name.equals(name))
			return true;
		return false;
	}
	
	
}
