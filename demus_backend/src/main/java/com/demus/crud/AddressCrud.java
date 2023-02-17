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

import com.demus.entity.Address;

/**
 * Activation Token Crud Controller
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
@PreAuthorize("hasRole('SUBSCRIBER')")
public interface AddressCrud extends JpaRepository<Address, Long> {
	
	@Modifying
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@Query ("delete from Address obj where obj.id in (?1)")
	void deleteInBatch (List<Long> ids);
	
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update Address obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> ids);
}	
