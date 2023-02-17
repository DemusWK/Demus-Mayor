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

import com.demus.entity.Country;

/**
 * Crud interface for country
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
@PreAuthorize("hasRole('ADMINISTRATOR')")
public interface CountryCrud extends JpaRepository<Country, Long> {
	@Modifying
	@Query ("delete from Country obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@Modifying(clearAutomatically = true)
	@Query ("update Country obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);
	
	List<Country> findByNameLikeIgnoreCase (String match, Pageable request);

	List<Country> findByNameContainsIgnoreCase(String match, Pageable filter);
	
}
