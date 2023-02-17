/**
 * 
 */
package com.demus.crud;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demus.entity.PaystackAuthorizationToken;
import com.demus.entity.Subscriber;

/**
 * Crud repository for Paystack Authorization Token
 * @author Lekan Baruwa
 */
@Transactional
public interface PaystackAuthorizationTokenCrud extends JpaRepository<PaystackAuthorizationToken, Long> {
	
	List<PaystackAuthorizationToken> findBySubscriber (Subscriber subscriber);
	
	PaystackAuthorizationToken findById(Long id);
	
	PaystackAuthorizationToken findTop1BySubscriberOrderByIdDesc(Subscriber subscriber);
	
	PaystackAuthorizationToken findByAuthorizationCode(String authorizationCode);
}
