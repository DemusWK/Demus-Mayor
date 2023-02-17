package com.demus.model;

public class PreConfirmTransaction {

	public String initiatingCost;
	
	public String transactionCost;
	
	public PreConfirmTransaction (String cost, String transactionCost) {
		this.initiatingCost = cost;
		this.transactionCost = transactionCost;
	}
	
}
