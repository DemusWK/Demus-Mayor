/**
 * 
 */
package com.demus.model;

import com.demus.entity.BankAccountType;

/**
 * This is for withdrawing cash from the wallet
 * @author Lekan Baruwa
 * @since 4 Mar 2018
 */
public class BankAccountRq extends AuthenticatedRq {
	
	public String accountNumber;
	
	public String bankOperatorCode;
	
	public BankAccountType bankAccountType;
	
	public Boolean isPrimary;

}
