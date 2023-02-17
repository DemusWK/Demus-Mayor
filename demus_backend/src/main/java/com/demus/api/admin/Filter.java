package com.demus.api.admin;

import java.util.Date;

import com.demus.entity.EntityStatus;

public class Filter {
	
	public int page = 0;
	
	public int size = 20;
	
	public String sort = "createdDate:desc";
	
	private String match;
	
	private String filterExpression;
	
	private EntityStatus status;
	
	public Date startDate;
	
	public Date endDate;
	
	public void setStartDate (Date startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate (Date endDate) {
		this.endDate = endDate;
	}
	
	public Filter () {
		
	}
	
	public Filter (String match) {
		this.match = match;
	}
	
	public Filter (String match, EntityStatus status, Integer page, Integer size) {
		setMatch(match);
		setStatus(status);
		setPage(page);
		setSize(size);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getFilterExpression() {
		return filterExpression;
	}

	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(EntityStatus status) {
		this.status = status;
	}
	
}
