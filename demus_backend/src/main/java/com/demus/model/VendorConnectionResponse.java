package com.demus.model;

import java.util.Date;

public class VendorConnectionResponse {
	public int amount,
			    sequence,
			    tariffTypeID,
			    serviceProviderID;
	public String status;
	
	public boolean fulfilled;
	
	public Date created_at;
	
	public String origMsisdn,
				    destMsisdn,
				    txRefID,
				    origBalance,
				    destBalance,
				    responseCode,
				    responseMessage,
				    seqTxRefID,
				    seqStatus,
				    seqID,
				    telcoTag;
	
	public VendorConnectionResponse() {
		responseCode = "400";
		status = "";
		txRefID = "000000000000000000000000";
		responseMessage = "Recharge request could not be fulfilled.";
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getTariffTypeID() {
		return tariffTypeID;
	}

	public void setTariffTypeID(int tariffTypeID) {
		this.tariffTypeID = tariffTypeID;
	}

	public int getServiceProviderID() {
		return serviceProviderID;
	}

	public void setServiceProviderID(int serviceProviderID) {
		this.serviceProviderID = serviceProviderID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isFulfilled() {
		return fulfilled;
	}

	public void setFulfilled(boolean fulfilled) {
		this.fulfilled = fulfilled;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getOrigMsisdn() {
		return origMsisdn;
	}

	public void setOrigMsisdn(String origMsisdn) {
		this.origMsisdn = origMsisdn;
	}

	public String getDestMsisdn() {
		return destMsisdn;
	}

	public void setDestMsisdn(String destMsisdn) {
		this.destMsisdn = destMsisdn;
	}

	public String getTxRefID() {
		return txRefID;
	}

	public void setTxRefID(String txRefID) {
		this.txRefID = txRefID;
	}

	public String getOrigBalance() {
		return origBalance;
	}

	public void setOrigBalance(String origBalance) {
		this.origBalance = origBalance;
	}

	public String getDestBalance() {
		return destBalance;
	}

	public void setDestBalance(String destBalance) {
		this.destBalance = destBalance;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getSeqTxRefID() {
		return seqTxRefID;
	}

	public void setSeqTxRefID(String seqTxRefID) {
		this.seqTxRefID = seqTxRefID;
	}

	public String getSeqStatus() {
		return seqStatus;
	}

	public void setSeqStatus(String seqStatus) {
		this.seqStatus = seqStatus;
	}

	public String getSeqID() {
		return seqID;
	}

	public void setSeqID(String seqID) {
		this.seqID = seqID;
	}

	public String getTelcoTag() {
		return telcoTag;
	}

	public void setTelcoTag(String telcoTag) {
		this.telcoTag = telcoTag;
	}
}
