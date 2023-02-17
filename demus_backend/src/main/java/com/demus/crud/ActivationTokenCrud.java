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

import com.demus.entity.ActivateTokenType;
import com.demus.entity.ActivationToken;
import com.demus.entity.Subscriber;

/**
 * Activation Token Crud Controller
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface ActivationTokenCrud extends JpaRepository<ActivationToken, Long> {
	
	@Modifying
	@Query ("delete from ActivationToken obj where obj.id in (?1)")
	void deleteInBatch (List<Long> ids);
	
	@Modifying
	@Query ("delete from ActivationToken obj where obj.subscriber = (?1) AND obj.type = (?2)")
	void deleteInBatch (Subscriber subscriber, ActivateTokenType type);
	
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update ActivationToken obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	ActivationToken findByValueAndTypeAndExpiryDateGreaterThanEqual(String token, ActivateTokenType type, Date date);

	ActivationToken findBySubscriberAndValueAndTypeAndExpiryDateGreaterThanEqual(Subscriber subscriber, String token, ActivateTokenType type, Date date);
}	
