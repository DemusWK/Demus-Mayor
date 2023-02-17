package com.demus.api.vendors;

import com.demus.api.ex.ApiException;

public class InvalidPinException extends ApiException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPinException (String message) {
		super (message);
	}
}
