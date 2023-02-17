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
import com.demus.entity.Role;
import com.demus.entity.Subscriber;
import com.demus.entity.SubscriberRole;

/**
 * Crud repository for Subscriber crud 
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
public interface SubscriberRoleCrud extends JpaRepository<SubscriberRole, Long> {
	
	List<SubscriberRole> findBySubscriberAndEntityStatus (Subscriber subscriber, EntityStatus entityStatus);
	
	List<SubscriberRole> findByRoleAndEntityStatus (Role role, EntityStatus entityStatus);
	
	@Modifying
	@Query ("delete from SubscriberRole obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@Modifying(clearAutomatically = true)
	@Query ("update SubscriberRole obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);
}
