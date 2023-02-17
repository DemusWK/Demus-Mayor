/**
 * 
 */
package com.demus.crud;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;

import com.demus.entity.BankOperator;
import com.demus.entity.Country;
import com.demus.entity.EntityStatus;

/**
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface BankOperatorCrud extends JpaRepository<BankOperator, Long> {

	// Get all bank operators

	@Override
	Page<BankOperator> findAll (Pageable request);
	
	// Get all operator by country
	List<BankOperator> findByCountryAndEntityStatus (Country country, EntityStatus status);

	
	@Modifying
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@Query ("delete from BankOperator obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update BankOperator obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	List<BankOperator> findByBankOperatorCodeLikeIgnoreCaseOrNameLikeIgnoreCase(String match, String match2, PageRequest filter);

	BankOperator findByBankOperatorCode(String bankOperatorCode);
	
}
