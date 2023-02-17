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

import com.demus.entity.Permission;

/**
 * Crud repository for Permissions
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface PermissionCrud extends JpaRepository<Permission, Long> {
	

	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying
	@Query ("delete from Permission obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	

	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update Permission obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	List<Permission> findByNameLikeIgnoreCase(String match, Pageable filter);
	
}
