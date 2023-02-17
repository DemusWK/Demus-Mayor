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

import com.demus.entity.VendorApiLog;

/**
 * Crud repository for VendorApiLog functions
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
//@PreAuthorize("hasRole('SUBSCRIBER')")
public interface VendorApiLogCrud extends JpaRepository<VendorApiLog, Long> {
	
	@Modifying
	@Query ("delete from VendorApiLog obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@Modifying(clearAutomatically = true)
	@Query ("update VendorApiLog obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	//@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'VENDOR')")
	@Override
	Page<VendorApiLog> findAll(Pageable pageRequest);
	
	List<VendorApiLog> findByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual (Date startDate, Date endDate, Pageable pageable);
	
}
