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

import com.demus.entity.Device;
import com.demus.entity.DeviceOs;
import com.demus.entity.EntityStatus;
import com.demus.entity.Subscriber;

/**
 * Crud Operation for devices
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface DeviceCrud extends JpaRepository<Device, Long> {
	
	List<Device> findBySubscriberAndEntityStatus (Subscriber subscriber, EntityStatus entityStatus);
	
	Device findByClientIdAndEntityStatus (String clientId, EntityStatus entityStatus);
	
	List<Device> findByDeviceOsAndEntityStatus (DeviceOs deviceOs, EntityStatus entityStatus);
	
	Long countByDeviceOsAndEntityStatus (DeviceOs deviceOs, EntityStatus entityStatus);
	
	@Modifying
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@Query ("delete from Device obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update Device obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	Device findByClientId(String clientId);

	Device findByClientIdAndDeviceOs(String clientId, DeviceOs clientOs);
	
	Device findByClientIdAndDeviceOsAndSubscriberId(String clientId, DeviceOs clientOs, Long subscriberId);
}
