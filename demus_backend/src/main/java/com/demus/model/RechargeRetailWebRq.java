/**
 * 
 */
package com.demus.model;

import com.demus.entity.MobileNetworkOperator;

/**
 * This class is a request object for purchasing a new recharge card from the website
 * @author Lekan Baruwa
 *
 */
public class RechargeRetailWebRq extends AuthenticatedRq {
	
	public MobileNetworkOperator mobileNetworkOperator;
	
	public String receiverNumber;
	
	public Integer amount;
	
	public Integer type;
	
	public String payStackTransactionRef;
	
}
