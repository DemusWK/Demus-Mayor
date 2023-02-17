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

import com.demus.entity.Role;

/**
 * Crud repository for roles
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface RoleCrud extends JpaRepository<Role, Long> {
	

	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying
	@Query ("delete from Role obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	

	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update Role obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	List<Role> findByNameContainsIgnoreCase(String match);

	Role findByRoleCode(String code);
	
}
