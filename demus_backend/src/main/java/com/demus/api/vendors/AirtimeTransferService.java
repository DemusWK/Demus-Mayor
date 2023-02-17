package com.demus.api.vendors;

import com.demus.api.io.ApiResponse;

public interface AirtimeTransferService {
	
	ApiResponse buyAirtime (Integer amount, String phoneNumber);
	
	Double accountBalance ();
	
}
