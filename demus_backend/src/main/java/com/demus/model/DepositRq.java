/**
 * 
 */
package com.demus.model;

import com.demus.api.io.ApiRequest;
import com.demus.entity.PaymentGateway;

/**
 * This is to deposit money into eclever wallet
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 3 Mar 2016
 */
public class DepositRq extends ApiRequest {
	
	public String switchSecret;
	
	public Double amount;
	
	public PaymentGateway paymentGateway;
	
	public String walletId;
	
	public Long paymentLogId;
	
	public Long originalPaymentLogId;
	
	public String tranxRef;
}
