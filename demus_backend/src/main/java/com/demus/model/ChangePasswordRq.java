/**
 * 
 */
package com.demus.model;

import com.demus.api.io.ApiRequest;

/**
 * Api request to change password
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public class ChangePasswordRq extends ApiRequest {
	
	public String oldPassword;
	
	public String newPassword;
	
	public String confirmPassword;
	
}
