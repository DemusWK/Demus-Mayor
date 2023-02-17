/**
 * 
 */
package com.demus.api.vendors;

import com.demus.entity.VendorApiLog;

/**
 * Api for vendor view of the platform
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public interface VendorApi {
	
	VendorApiLog deliverOrderRequest (DeliverOrderRq request, String mno, boolean isData, boolean isTest) throws VendorRelationshipException;
}
