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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

/**
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "ROLE_PERMISSION", uniqueConstraints = @UniqueConstraint (name = "role_permission_idx", columnNames = {"ROLE_ID", "PERMISSION_ID"}))
public class RolePermission extends Persistent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "ROLE_ID", referencedColumnName = "ID", nullable = false, updatable = false)
	@JsonIgnore // Prevent deserialization cycles
	@Expose (deserialize = true, serialize = false)
	private Role role;
	
	@NotNull
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "PERMISSION_ID", referencedColumnName = "ID", nullable = false, updatable = false, unique = false)
	private Permission permission;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}
}
