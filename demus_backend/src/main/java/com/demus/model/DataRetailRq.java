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
public class DataRetailRq extends AuthenticatedRq {
	
	public MobileNetworkOperator mobileNetworkOperator;
	
	public String productCode;
	
	public String receiverNumber;
	
	public Integer amount;
	
}
