/**
 * 
 */
package com.demus.crud;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;

import com.demus.entity.EntityStatus;
import com.demus.entity.Refer;
import com.demus.entity.Subscriber;
import com.demus.entity.WalletTransaction;

/**
 * Crud repository for Refer functions
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface ReferCrud extends JpaRepository<Refer, Long> {
	
	@Modifying
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Query ("delete from Refer obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update Refer obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	List<Refer> findBySubscriber(Subscriber subscriber);
	
	Refer findByTransaction (WalletTransaction walletTransaction);
	
	List<Refer> findByPhoneNumberAndEntityStatus (String phoneNumber, EntityStatus entityStatus);
}
