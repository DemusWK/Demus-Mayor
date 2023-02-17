/**
 * 
 */
package com.demus.model;

import com.demus.api.io.ApiRequest;

/**
 * This is to deposit money into eclever wallet
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 3 Mar 2016
 */
public class PinRq extends ApiRequest {
	
	public String walletId;
	
	public String pinNumber;
	
}
