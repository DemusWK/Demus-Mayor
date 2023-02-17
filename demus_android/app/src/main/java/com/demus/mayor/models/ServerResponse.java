package com.demus.mayor.models;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ServerResponse implements Parcelable {

	String responseCode,
		   responseMessage,
		   extraStringValue;
	
	ArrayList<String> tags;
	
	int extraIntValue;
	
	double extraDoubleValue, extraDoubleValue2;
	
	public ServerResponse() {
		
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
	
	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	public String getExtraStringValue() {
		return extraStringValue;
	}

	public void setExtraStringValue(String extraStringValue) {
		this.extraStringValue = extraStringValue;
	}

	public int getExtraIntValue() {
		return extraIntValue;
	}

	public void setExtraIntValue(int extraIntValue) {
		this.extraIntValue = extraIntValue;
	}

	public double getExtraDoubleValue() {
		return extraDoubleValue;
	}

	public void setExtraDoubleValue(double extraDoubleValue) {
		this.extraDoubleValue = extraDoubleValue;
	}
	
	public double getExtraDoubleValue2() {
		return extraDoubleValue2;
	}

	public void setExtraDoubleValue2(double extraDoubleValue2) {
		this.extraDoubleValue2 = extraDoubleValue2;
	}

	/**
	 * Overidden method for serializing the class attributes
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(responseCode);
		out.writeString(responseMessage);
		out.writeSerializable(tags);
		out.writeInt(extraIntValue);
		out.writeString(extraStringValue);
		out.writeDouble(extraDoubleValue);
		out.writeDouble(extraDoubleValue2);
	}

	/**
	 * This is a constructor for de-serializing the object
	 */
	@SuppressWarnings("unchecked")
	private ServerResponse(Parcel in) {
		this.setResponseCode(in.readString());
		this.setResponseMessage(in.readString());
		this.setTags((ArrayList<String>) in.readSerializable());
		this.setExtraIntValue(in.readInt());
		this.setExtraStringValue(in.readString());
		this.setExtraDoubleValue(in.readDouble());
		this.setExtraDoubleValue2(in.readDouble());
	}

	public static final Creator<ServerResponse> CREATOR = new Creator<ServerResponse>() {
		
		@Override
		public ServerResponse createFromParcel(Parcel in) {
			return new ServerResponse(in);
		}
		
		@Override
		public ServerResponse[] newArray(int size) {
			return new ServerResponse[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
