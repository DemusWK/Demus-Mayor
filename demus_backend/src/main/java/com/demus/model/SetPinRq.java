/**
 * 
 */
package com.demus.model;

import com.demus.api.io.ApiRequest;

/**
 * This class represents a new password request
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 10 Mar 2016
 */
public class SetPinRq extends ApiRequest {
	public String oldPin;
	public String pin;
}
