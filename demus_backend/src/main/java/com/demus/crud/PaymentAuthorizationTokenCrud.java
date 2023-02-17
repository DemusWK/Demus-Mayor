/**
 * 
 */
package com.demus.crud;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;

import com.demus.entity.PaymentAuthorizationToken;

/**
 * Crud repository for payment authorization tokens
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
@PreAuthorize("hasAnyRole('SUBSCRIBER', 'PAYMENT_GATEWAY')")
public interface PaymentAuthorizationTokenCrud extends JpaRepository<PaymentAuthorizationToken, Long> {
	
	PaymentAuthorizationToken findByTokenAndExpiryDateLessThanEqual (String token, Date expiryDate);
	
	@Modifying
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Query ("delete from PaymentAuthorizationToken obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update PaymentAuthorizationToken obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);
}
