/**
 * 
 */
package com.demus.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An assignable role in the system
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "ROLE")
public class Role extends Persistent {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Column (name = "NAME", nullable = false, unique = true)
	private String name;
	
	@NotNull
	@Column (name = "ROLE_CODE", unique = true, nullable = false)
	private String roleCode;
	
	@Size (max = 255)
	@Column (name = "DESCRIPTION", nullable = true)
	private String description;
	
	//Requested permissions from client
	@Transient
	@JsonProperty("permissions")
	public List<Permission> savePermissions;
	
	@ManyToMany (fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable (name = "ROLE_PERMISSION", 
	joinColumns = {@JoinColumn (name = "ROLE_ID")}, inverseJoinColumns = {@JoinColumn (name = "PERMISSION_ID")})
	@JsonProperty("savedPermissions")
	private List<Permission> permissions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

}
