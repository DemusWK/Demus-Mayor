/**
 * 
 */
package com.demus.crud;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;

import com.demus.entity.EntityStatus;
import com.demus.entity.Product;
import com.demus.entity.PromoCode;

/**
 * Crud repository for all promo code related activities
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
@PreAuthorize("hasAnyRole('SUBSCRIBER')")
public interface PromoCodeCrud extends JpaRepository<PromoCode, Long> {
	
	PromoCode findByCodeAndEntityStatusAndExpiryDateLessThanEqual(String promoCode, EntityStatus status, Date date);
	
	PromoCode findByCodeAndEntityStatus (String code, EntityStatus entityStatus);
	
	List<PromoCode> findByProduct (Product product, Pageable request);
	
	@Modifying
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Query ("delete from PromoCode obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update PromoCode obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);
	
	@SuppressWarnings("unchecked")
	@Override
	@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SALES')")
	PromoCode save (PromoCode promoCode);
}
