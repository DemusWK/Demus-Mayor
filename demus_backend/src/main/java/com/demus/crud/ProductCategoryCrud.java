/**
 * 
 */
package com.demus.crud;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;

import com.demus.entity.EntityStatus;
import com.demus.entity.ProductCategory;

/**
 * Crud repository for product categories
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
@PreAuthorize("hasAnyRole('SUBSCRIBER')")
public interface ProductCategoryCrud extends JpaRepository<ProductCategory, Long> {
	
	@Override
	Page<ProductCategory> findAll (Pageable request);
	
	ProductCategory findByCategoryCode (String catCode, EntityStatus status);
	
	List<ProductCategory> findByEntityStatus (EntityStatus entityStatus, Pageable request);
	
	List<ProductCategory> findByParentCategoryAndEntityStatus (EntityStatus entityStatus, Pageable request); 
	
	@Modifying
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Query ("delete from ProductCategory obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update ProductCategory obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	List<ProductCategory> findByCategoryCodeLikeIgnoreCaseOrNameLikeIgnoreCase(String match, String match2, Pageable filter);
	
	@SuppressWarnings("unchecked")
	@Override
	@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PROCUREMENT')")
	ProductCategory save (ProductCategory object);
	
	
}
