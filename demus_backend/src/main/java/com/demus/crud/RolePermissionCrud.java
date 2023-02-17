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

import com.demus.entity.Permission;
import com.demus.entity.Role;
import com.demus.entity.RolePermission;

/**
 * Crud repository for RolePermissions
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
public interface RolePermissionCrud extends JpaRepository<RolePermission, Long> {
	
	@Modifying
	@Query ("delete from RolePermission obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@Modifying(clearAutomatically = true)
	@Query ("update RolePermission obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	List<RolePermission> findByRole(Role role);
	
	List<RolePermission> findByPermission(Permission permission);
	
	RolePermission findByRoleAndPermission (Role role, Permission permission);
	
	
}
