/**
 * 
 */
package com.demus.api.vendors;

import com.demus.api.io.ApiRequest;
import com.demus.entity.Order;
import com.demus.entity.Subscription;
import com.demus.entity.WalletTransaction;

/**
 * Api interface to deliver orders
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public class DeliverOrderRq extends ApiRequest {
	
	public WalletTransaction walletTransaction;
	
	public Subscription subscription;
	
	public  Order order;
	
	public String phoneNumber;
	
	public Boolean genericBool;
}
