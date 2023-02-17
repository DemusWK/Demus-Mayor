/**
 * 
 */
package com.demus.crud;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demus.entity.EntityStatus;
import com.demus.entity.Subscriber;
import com.demus.entity.Wallet;

/**
 * Crud for wallet operations
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface WalletCrud extends JpaRepository<Wallet, Long> {
	
	Wallet findByWalletIdAndEntityStatus (String walletId, EntityStatus entityStatus);
	
	Wallet findBySubscriberAndEntityStatus (Subscriber subscriber, EntityStatus entityStatus);
	

	//@PreAuthorize("hasAnyRole('SUBSCRIBER', 'PAYMENT_GATEWAY')")
	@Modifying (clearAutomatically = true)
	@Query ("update Wallet w SET w.balance = w.balance - :amount WHERE w.walletId = :walletId")
	void debitWallet (@Param ("walletId") String walletId, @Param ("amount")  Double amount);
	

	//@PreAuthorize("hasAnyRole('PAYMENT_GATEWAY', 'SUBSCRIBER')")
	@Modifying (clearAutomatically = true)
	@Query ("update Wallet w SET w.balance = w.balance + :amount WHERE w.walletId = :walletId")
	void updateBalance (@Param ("walletId") String walletId, @Param ("amount")  Double amount);
	

	//@PreAuthorize("hasAnyRole('SUBSCRIBER')")
	@Query ("SELECT w.balance FROM Wallet w WHERE w.walletId = :walletId")
	Double walletBalance (@Param("walletId") String walletId);
	
	@Modifying
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Query ("delete from Wallet obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);

	//@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@Modifying(clearAutomatically = true)
	@Query ("update Wallet obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	Wallet findByWalletId(String id);

	@Modifying (clearAutomatically = true)
	@Query ("update Wallet w SET w.bonusBalance = w.bonusBalance + :amount WHERE w.walletId = :walletId")
	void updateBonusBalance(@Param ("walletId") String walletId, @Param ("amount") Double amount);
}
