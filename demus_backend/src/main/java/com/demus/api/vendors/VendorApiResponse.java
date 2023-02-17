/**
 * 
 */
package com.demus.api.vendors;

import com.demus.api.io.ApiResponse;
import com.demus.api.io.ApiResponseStatus;

/**
 * Reponse object for vendor api implementation
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public class VendorApiResponse extends ApiResponse {

	public VendorApiResponse(String message2, Object object, Integer size, ApiResponseStatus status) {
		super(message2, object, size, status);
	}

}
