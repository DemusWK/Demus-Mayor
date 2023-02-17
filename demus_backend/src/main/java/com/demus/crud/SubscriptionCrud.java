/**
 * 
 */
package com.demus.crud;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demus.entity.EntityStatus;
import com.demus.entity.Order;
import com.demus.entity.Product;
import com.demus.entity.Subscription;

/**
 * Crud repository for subscriptions
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
//@PreAuthorize("hasAnyRole('SUBSCRIBER')")
public interface SubscriptionCrud extends JpaRepository<Subscription, Long> {
	
	List<Subscription> findByOrderAndEntityStatus (Order order, EntityStatus entityStatus);
	
	List<Subscription> findByProductAndEntityStatus (Product product, EntityStatus entityStatus);
	
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SALES', 'ACCOUNTANT')")
	Long countByProductAndEntityStatusAndCreatedDateGreaterThanAndCreatedDateLessThan (Product product, EntityStatus entityStatus, Date startDate, Date endDate);
	
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SALES', 'ACCOUNTANT')")
	Long countByOrderAndEntityStatus (Order order, EntityStatus entityStatus);
	
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SALES', 'ACCOUNTANT')")
	@Query ("SELECT AVG(s.quantity) FROM Subscription s WHERE s.entityStatus = :entityStatus AND s.product = :product AND s.createdDate BETWEEN :startDate AND :endDate")
	Double averageOrderSize (@Param ("product") Product product,@Param ("entityStatus") EntityStatus status, @Param ("startDate") Date startDate, @Param ("endDate") Date endDate);
	
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SALES', 'ACCOUNTANT')")
	@Query ("SELECT SUM(s.quantity) FROM Subscription s WHERE s.entityStatus = :entityStatus AND s.product = :product AND s.createdDate BETWEEN :startDate AND :endDate")
	Double sumQuantitySize (@Param ("product") Product product,@Param ("entityStatus") EntityStatus status, @Param ("startDate") Date startDate, @Param ("endDate") Date endDate);
	
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SALES', 'ACCOUNTANT')")
	@Query ("SELECT SUM(s.cost) FROM Subscription s WHERE s.entityStatus = :entityStatus AND s.product = :product AND s.createdDate BETWEEN :startDate AND :endDate")
	Double sumRevenue (@Param ("product") Product product,@Param ("entityStatus") EntityStatus status, @Param ("startDate") Date startDate, @Param ("endDate") Date endDate);

	@Modifying
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Query ("delete from Subscription obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update Subscription obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);
}
