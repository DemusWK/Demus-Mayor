/**
 * 
 */
package com.demus.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demus.crud.SubscriberCrud;
import com.demus.entity.Subscriber;

/**
 * This is responsible for loading user session principal
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 13 Mar 2016
 */
@Service
@Configuration
public class AuthService implements UserDetailsService {

	@Autowired
	private SubscriberCrud api;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		assert (username != null);
		Subscriber subscriber = api.findByPhoneNumberOrEmail (username);
		if (subscriber == null)
			throw new UsernameNotFoundException ("Email or Phone Number is incorrect");
		return new User (subscriber);
		
	}
	
	@Bean
	public AuditorAware<Subscriber> auditorProvider() {
		return new AuditorAware<Subscriber>() {

			@Override
			public Subscriber getCurrentAuditor() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null || !authentication.isAuthenticated())
					return null;
				Subscriber subscriber = null;
				try {
					subscriber = ((User) authentication.getPrincipal()).getSubscriber();
				} catch (Exception e) {
					
				}
				
				return subscriber;
			}
			
		};
	}
}
