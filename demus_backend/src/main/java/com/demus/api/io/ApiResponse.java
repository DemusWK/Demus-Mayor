package com.demus.api.io;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude (Include.NON_NULL)
public class ApiResponse {
	
	public ApiResponse(String message2, Object object, Integer size, ApiResponseStatus status) {
		message = message2;
		results = object;
		this.size = size;
		this.status = status;
	}
	
	public Long timestamp = new Date().getTime();
	public ApiResponseStatus status;
	public int size;
	public Object results;
	public String message;
}


