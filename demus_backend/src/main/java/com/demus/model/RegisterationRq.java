/**
 * 
 */
package com.demus.model;

import com.demus.api.io.ApiRequest;
import com.demus.entity.BankAccountType;
import com.demus.entity.Gender;
import com.demus.entity.MobileNetworkOperator;

/**
 * Registeration request object to the API
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public class RegisterationRq extends ApiRequest {

	public String mobileNumber;
	
	public MobileNetworkOperator mobileNetworkOperator;
	
	public String firstName;
	
	public String lastName;
	
	public String email;

	public String password;
	
	public String pin;
	
	public String countryCode;
	
	public String facebookId;
	
	public String facebookApiSecret;
	
	public String googleId;
	
	public String googleApiSecret;
	
	public String bankAccountNumber;
	
	public String bankOperatorCode;
	
	public BankAccountType bankAccountType;
	
	public Gender gender;
	
	public String dateOfBirth;
	
}
