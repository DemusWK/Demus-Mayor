/**
 * 
 */
package com.demus.crud;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demus.entity.PushMessage;

/**
 * @author Lekan Baruwa
 * @for EcleverMobile
 */
public interface PushMessageCrud extends JpaRepository<PushMessage, Long> {
	
	List<PushMessage> findByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Date startDate, Date endDate);
	
	List<PushMessage> findByCreatedDateGreaterThanEqual(Date startDate);
	
	PushMessage findById(int id);
}
