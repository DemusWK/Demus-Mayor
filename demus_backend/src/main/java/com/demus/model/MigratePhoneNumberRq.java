/**
 * 
 */
package com.demus.model;

import com.demus.entity.MobileNetworkOperator;

/**
 * This class helps users migrate their phone numbers and wallet account
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 10 Mar 2016
 */
public class MigratePhoneNumberRq extends AuthenticatedRq {
	public String newPhoneNumber;
	public MobileNetworkOperator mobileNetworkOperator;
}
