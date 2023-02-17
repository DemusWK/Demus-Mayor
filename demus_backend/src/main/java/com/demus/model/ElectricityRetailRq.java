/**
 * 
 */
package com.demus.model;

/**
 * This class is a request object for purchasing electricity
 * @author Lekan Baruwa
 *
 */
public class ElectricityRetailRq extends AuthenticatedRq {
	
	public String meterNumber;
	
	public boolean isPrepaid;
	
	public Integer amount;
	
	public String disco;
}
