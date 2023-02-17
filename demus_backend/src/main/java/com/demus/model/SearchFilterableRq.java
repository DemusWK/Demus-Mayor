/**
 * 
 */
package com.demus.model;

import com.demus.api.io.ApiRequest;

/**
 * This represents any request that can be filtered
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 3 Mar 2016
 */
public class SearchFilterableRq extends ApiRequest {
	
	public String filter;
	
	public Integer page;
	
	public Integer size;
	
}
