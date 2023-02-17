/**
 * 
 */
package com.demus.crud;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import com.demus.entity.EntityStatus;
import com.demus.entity.PaymentGateway;
import com.demus.entity.Wallet;
import com.demus.entity.WalletTransaction;
import com.demus.entity.WalletTransactionStatus;
import com.demus.entity.WalletTransactionType;

/**
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface WalletTransactionCrud extends JpaRepository<WalletTransaction, Long> {
	
	WalletTransaction findByReference (String reference);
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PAYMENT_GATEWAY')")
	List<WalletTransaction> findByPaymentGatewayAndEntityStatus (PaymentGateway paymentGateway, EntityStatus entityStatus);
	
	WalletTransaction findByTransactionIdAndEntityStatus (String transactionId, EntityStatus entityStatus);
	
	List<WalletTransaction> findByTypeAndEntityStatus (WalletTransactionType type, EntityStatus entityStatus);
	
	List<WalletTransaction> findByWalletAndTypeAndEntityStatus (Wallet wallet, WalletTransactionType type, EntityStatus entityStatus);
	
	List<WalletTransaction> findByWalletAndEntityStatus (Wallet wallet, EntityStatus entityStatus);
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ACCOUNTANT')")
	@Query ("SELECT SUM(t.amount) from WalletTransaction t where t.type = :type AND t.transactionStatus = :status AND t.createdDate BETWEEN :startDate AND :endDate")
	Double sum (@Param ("type") WalletTransactionType type, @Param ("status") WalletTransactionStatus status, @Param ("startDate") Date startDate, @Param ("endDate") Date endDate);
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ACCOUNTANT')")
	@Query ("SELECT AVG(t.amount) from WalletTransaction t where t.type = :type AND t.transactionStatus = :status AND t.createdDate BETWEEN :startDate AND :endDate")
	Double avg (@Param ("type") WalletTransactionType type, @Param ("status") WalletTransactionStatus status, @Param ("startDate") Date startDate, @Param ("endDate") Date endDate);
	
	@Modifying
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Query ("delete from WalletTransaction obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update WalletTransaction obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	List<WalletTransaction> findByTransactionId(String match, Pageable filter);

	List<WalletTransaction> findByWalletAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualOrderByIdDesc(Wallet wallet, Date startDate,
			Date endDate, Pageable pageRequest);
	
	List<WalletTransaction> findByWalletAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Wallet wallet, Date startDate,
			Date endDate);
	
	List<WalletTransaction> findByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Date startDate,
			Date endDate, Pageable pageRequest);
	
	@Query ("select t from WalletTransaction t where t.createdDate >= :startDate AND t.createdDate <= :endDate")
	List<WalletTransaction> findByDate (@Param ("startDate") Date startDate, @Param ("endDate") Date endDate);
	
	@Query ("select SUM(t) from WalletTransaction t where t.type = :type AND t.createdDate >= :startDate AND t.createdDate <= :endDate")
	Double totalByTypeDate (@Param ("type") WalletTransactionType type, @Param ("startDate") Date startDate, @Param ("endDate") Date endDate);
	
	@Query ("select SUM(t) from WalletTransaction t where t.type = :type")
	Double totalByType (@Param ("type") WalletTransactionType type);
	
	@Query ("select COUNT(t) from WalletTransaction t where t.type = :type AND t.createdDate >= :startDate AND t.createdDate <= :endDate")
	Long countByTypeDate (@Param ("type") WalletTransactionType type, @Param ("startDate") Date startDate, @Param ("endDate") Date endDate);
	
	@Query ("select COUNT(t) from WalletTransaction t where t.type = :type")
	Long countByType (@Param ("type") WalletTransactionType type);
	
	@Query ("select COUNT(t) from WalletTransaction t where t.createdDate >= :startDate AND t.createdDate <= :endDate")
	Long countByDate (@Param ("startDate") Date startDate, @Param ("endDate") Date endDate);
	
	@Query ("select COUNT(t) from WalletTransaction t where t.type = :type")
	Long count (@Param ("type") WalletTransactionType type);
		
	@Query ("select t from WalletTransaction t where t.createdDate >= :startDate AND t.createdDate <= :endDate AND t.wallet = :wallet")
	List<WalletTransaction> accountStatement (@Param ("startDate") Date startDate, @Param ("endDate") Date endDate, @Param ("wallet") Wallet wallet);

	WalletTransaction findByTransactionId(String transactionId);
	
	@Query ("select t from WalletTransaction t where t.wallet = :wallet ORDER BY t.id DESC")
	List<WalletTransaction> findByWallet (@Param ("wallet") Wallet wallet, Pageable pageable);

	List<WalletTransaction> findByWalletOrderByIdDesc (Wallet wallet, Pageable pageable);
	
	WalletTransaction findTop1ByWalletAndPaymentGatewayOrderByIdDesc(Wallet wallet, PaymentGateway paymentGateway);
	
	WalletTransaction findByPaystackTransactionRef(String transactionRef);
}
