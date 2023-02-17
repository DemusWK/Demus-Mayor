/**
 * 
 */
package com.demus.model;

import com.demus.api.io.ApiRequest;

/**
 * This is to deposit money into eclever wallet via PayStack
 * @author Lekan Baruwa
 * @since 6 May 2018
 */
public class PayStackDepositRq extends ApiRequest {
	
	public String tranxRef;
	
	public Integer authId;
	
	public Double amount;
	
	public String pin;
}
