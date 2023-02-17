/**
 * 
 */
package com.demus.model;

/**
 * This is create an account for a subscriber or transfer money to an exisitng user
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 3 Mar 2016
 */
public class ReferSubscriberRq extends AuthenticatedRq {
	
	public String receiverPhoneNumber;
	
	public Double amount;
	
}
