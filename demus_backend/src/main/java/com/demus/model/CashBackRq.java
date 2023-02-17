/**
 * 
 */
package com.demus.model;

import com.demus.entity.BankAccountType;

/**
 * This is for withdrawing cash from the wallet
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 3 Mar 2016
 */
public class CashBackRq extends AuthenticatedRq {
	
	public String accountNumber;
	
	public String bankOperatorCode;
	
	public BankAccountType bankAccountType;
	
	public Double amount;

}
