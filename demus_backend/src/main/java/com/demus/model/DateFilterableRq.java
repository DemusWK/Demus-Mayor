/**
 * 
 */
package com.demus.model;

import java.util.Date;

import com.demus.api.io.ApiRequest;

/**
 * This is for requests with a Date range
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 3 Mar 2016
 */
public class DateFilterableRq extends ApiRequest {
	
	public Date startDate;
	
	public Date endDate;
	
	public Integer page;
	
	public Integer size;
	
}
