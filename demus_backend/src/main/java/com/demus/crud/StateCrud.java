package com.demus.crud;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;

import com.demus.entity.Country;
import com.demus.entity.State;

/**
 * Crud repository for states information
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
public interface StateCrud extends JpaRepository<State, Long> {
	
	List<State> findByCountry (Country country);
	
	List<State> findByNameContainsAndCountry (String name, Country country);
	
	State findByStateCode (String stateCode);
	
	@Modifying
	@Query ("delete from State obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@Modifying(clearAutomatically = true)
	@Query ("update State obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);
}
