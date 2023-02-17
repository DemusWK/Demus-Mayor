/**
 * 
 */
package com.demus.model;

/**
 * This is a request form to transfer money to another Demus Mayor account
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 11 Mar 2016
 */
public class TransferRq extends AuthenticatedRq {
	public String receiverPhoneNumber;
	public Double amount;
}
