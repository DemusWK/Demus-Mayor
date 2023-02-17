/**
 * 
 */
package com.demus.model;

import com.demus.entity.MobileNetworkOperator;

/**
 * This class is a request object for purchasing a new recharge card
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 *
 */
public class RechargeRetailRq extends AuthenticatedRq {
	
	public MobileNetworkOperator mobileNetworkOperator;
	
	public String receiverNumber;
	
	public Integer amount;
	
	public String promoCode;
	
	public Integer type;
}
