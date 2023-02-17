/**
 * 
 */
package com.demus.api.system;

import com.demus.api.ex.ApiException;
import com.demus.api.io.ApiResponse;

/**
 * This service is responsible for sending messages
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 5 Apr 2016
 */
public interface SmsService {
	public ApiResponse send (String to, String message, String from) throws ApiException;
	
}
