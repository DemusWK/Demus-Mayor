/**
 * 
 */
package com.demus.crud;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;

import com.demus.entity.EntityStatus;
import com.demus.entity.Gender;
import com.demus.entity.Subscriber;

/**
 * Repository for subscriber crud operations
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface SubscriberCrud extends JpaRepository<Subscriber, Long> {
	
	Subscriber findByPhoneNumberAndEntityStatus (String phoneNumber, EntityStatus status);
	
	List<Subscriber> findByNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCaseOrEmailContainsIgnoreCase (String name, String phoneNumber,String email,  Pageable request);
	
	@Override
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	Page<Subscriber> findAll (Pageable request);
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	List<Subscriber> findByEntityStatus (EntityStatus status, Pageable request);
	
	@Modifying
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Query ("delete from Subscriber obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update Subscriber obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);
	
	List<Subscriber> findByGenderAndEntityStatus (Gender gender, EntityStatus status, Pageable request);
	
	Subscriber findByEmailAndEntityStatus (String email, EntityStatus status);

	Subscriber findByPhoneNumber(String phoneNumber);
	
	Subscriber findByEmail (String email);
	
	Subscriber findByFacebookId (String facebookId);
	
	Subscriber findByGoogleId (String googleId);
	
	Subscriber findByFacebookIdAndEntityStatus (String facebookId, EntityStatus status);
	
	Subscriber findByGoogleIdAndEntityStatus (String googleId, EntityStatus status);

	@Query ("select user from Subscriber user where user.email = ?1 or user.phoneNumber = ?1")
	Subscriber findByPhoneNumberOrEmail(String username);
	
	List<Subscriber> findByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Date startDate, Date endDate);
	
	Long countByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Date startDate, Date endDate);
	
}
