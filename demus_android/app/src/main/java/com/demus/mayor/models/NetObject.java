package com.demus.mayor.models;

public class NetObject {

	int id;
	String text;
	int imgResource;
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getImgResource() {
		return imgResource;
	}
	
	public void setImgResource(int imgResource) {
		this.imgResource = imgResource;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
