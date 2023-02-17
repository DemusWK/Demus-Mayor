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
import org.springframework.data.repository.query.Param;

import com.demus.entity.Country;
import com.demus.entity.EntityStatus;
import com.demus.entity.Product;
import com.demus.entity.ProductCategory;
import com.demus.entity.Vendor;

/**
 * Crud repository for product
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
//@PreAuthorize("hasAnyRole('SUBSCRIBER')")
public interface ProductCrud extends JpaRepository<Product, Long> {
	
	List<Product> findByCategory (ProductCategory category, EntityStatus status);
	
	@Query ("SELECT p FROM Product p WHERE p.productCode = :code AND p.category.categoryCode = :categoryCode AND p.entityStatus = :status")
	Product findByProductCodeAndCategoryCategoryCode (@Param("code")String code,  @Param("categoryCode")String categoryCode, @Param("status")EntityStatus status);
	
	List<Product> findByCountry (Country country);
	
	List<Product> findByPriceBetween (Double start, Double end);
	
	List<Product> findByStockCountBetween (Integer start, Integer end);
	
	Product findByProductCodeAndEntityStatus (String productCode, EntityStatus status);
	
	List<Product> findByNameContainsAndEntityStatus (String Name, EntityStatus entityStatus, Pageable request);
	
	List<Product> findByVendorAndEntityStatus (Vendor vendor, Pageable request);
	
	@Modifying
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PROCUREMENT')")
	@Query ("delete from Product obj where obj.id in (?1)")
	void deleteInBatch(List<Long> ids);
	
	//@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PROCUREMENT')")
	@Modifying(clearAutomatically = true)
	@Query ("update Product obj set obj.entityStatus = 'DELETED' where obj.id in (?1)")
	void recycleBin (List<Long> id);

	List<Product> findByProductCodeLikeOrNameLikeIgnoreCase(String match, String match2, Pageable filter);

	List<Product> findByProductCodeContainsOrNameContainsIgnoreCase(String match, String match2, Pageable filter);

	Product findByProductCode(String productCode);
	
	@SuppressWarnings("unchecked")
	@Override
	//@PreAuthorize("hasAnyRole('SUBSCRIBER', 'ADMINISTRATOR', 'PROCUREMENT')")
	Product save (Product product);
}
