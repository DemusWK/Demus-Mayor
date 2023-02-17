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

import com.demus.entity.Vendor;

/**
 * Crud repository for vendor functions
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
public interface VendorCrud extends JpaRepository<Vendor, Long> {
	
	@Modifying
	@Query ("delete from Vendor obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@Modifying(clearAutomatically = true)
	@Query ("update Vendor obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	@PreAuthorize("hasAnyRole('SUBSCRIBER')")
	List<Vendor> findByNameLike(String match);
	
}
