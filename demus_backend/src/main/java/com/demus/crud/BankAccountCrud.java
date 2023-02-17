/**
 * 
 */
package com.demus.crud;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;

import com.demus.entity.BankAccount;
import com.demus.entity.BankOperator;
import com.demus.entity.EntityStatus;
import com.demus.entity.Subscriber;

/**
 * Repository for bank account crud operations
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface BankAccountCrud extends JpaRepository<BankAccount, Long> {
	
	List<BankAccount> findBySubscriberAndEntityStatus (Subscriber subscriber, EntityStatus status);
	
	List<BankAccount> findByBankOperatorAndEntityStatus (BankOperator operator, EntityStatus status, Pageable request);
	
	BankAccount findBySubscriberAndAccountNumberAndBankOperatorAndEntityStatus (Subscriber subscriber, String accNo, BankOperator operator, EntityStatus status);
	
	@Modifying
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@Query ("delete from BankAccount obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update BankAccount obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	List<BankAccount> findBySubscriber(Subscriber subscriber);
	
}
