/**
 * 
 */
package com.demus.model;

import com.demus.api.io.ApiRequest;

/**
 * This class can be used to confirm an access code
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 12 Mar 2016
 */
public class ConfirmDataRq extends ApiRequest {
	public String accessCode;
	
	public void setAccessCode (String accessCode) {
		this.accessCode = accessCode;
	}
}
