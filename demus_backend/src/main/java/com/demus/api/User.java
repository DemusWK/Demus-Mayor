package com.demus.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.demus.entity.Permission;
import com.demus.entity.Subscriber;

public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	
	private Subscriber subscriber;
	
	public User (Subscriber subscriber) {
		assert(subscriber != null);
		List<Permission> permissions = subscriber.getRole().getPermissions();
		if (permissions != null) {
			for (Permission permission : permissions) {
				authorities.add(new AuthRole(permission));
			}
		}
		this.subscriber = subscriber;
	}
	
	public Subscriber getSubscriber () {
		return subscriber;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return subscriber.getPassword();
	}

	@Override
	public String getUsername() {
		return subscriber.getPhoneNumber();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	private class AuthRole implements GrantedAuthority {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String role;
		
		public AuthRole(Permission permission) {
			role = permission.getName();
		}
		
		@Override
		public String getAuthority() {
			return role;
		}
		
	}
	
}
