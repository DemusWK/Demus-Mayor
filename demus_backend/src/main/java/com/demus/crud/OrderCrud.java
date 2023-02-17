/**
 * 
 */
package com.demus.crud;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demus.entity.EntityStatus;
import com.demus.entity.Order;
import com.demus.entity.OrderStatus;
import com.demus.entity.Subscriber;

/**
 * Crud repository to process orders
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
//@PreAuthorize("hasRole('SUBSCRIBER')")
public interface OrderCrud extends JpaRepository<Order, Long> {
	
	List<Order> findBySubscriber (Subscriber subscriber, Pageable request);
	
	List<Order> findOrderBySubscriberAndEntityStatus (Subscriber subscriber, EntityStatus status, Pageable request);
	
	Order findByOrderId (String orderId);
	
	List<Order> findByOrderStatus (OrderStatus status);
	
	List<Order> findBycreatedDateBetween (Date start, Date end);
	
	@Override
	Page<Order> findAll (Pageable request);
	
	//@PreAuthorize("hasAnyRole('ACCOUNTANT', 'ADMINISTRATOR')")
	@Query ("SELECT SUM(o.cost) FROM Order o WHERE o.orderStatus = :orderStatus")
	Double sumOrdersByStatus (@Param ("orderStatus") OrderStatus status);

	//@PreAuthorize("hasAnyRole('ACCOUNTANT', 'ADMINISTRATOR')")
	@Query ("SELECT SUM(o.cost) FROM Order o WHERE o.orderStatus = :orderStatus AND (o.createdDate BETWEEN :start AND :end)")
	Double sumOrdersByStatusRange (@Param ("orderStatus") OrderStatus status, @Param ("start") Date start, @Param ("end") Date end);
	
	@Modifying
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Query ("delete from Order obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update Order obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	//@PreAuthorize("hasAnyRole('SUBSCRIBER', 'ACCOUNTANT', 'ADMINISTRATOR')")
	List<Order> findByOrderId(String match, Pageable filter);
}
