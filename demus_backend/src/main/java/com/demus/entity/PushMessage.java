package com.demus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A push message
 * @author Lekan Baruwa
 * @for EcleverMobile
 */
@Entity
@Table (name = "PUSH_MESSAGE")
public class PushMessage extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size (max = 200)
	@Column (name = "TITLE", nullable = false, updatable = true)
	private String title;
	
	@NotNull
	@Size (max = 2000)
	@Column (name = "MESSAGE", nullable = false, updatable = true)
	private String message;
	
	@NotNull
	@Column (name = "PICTURE_URL", nullable = false, updatable = false)
	private String picture_url;
	
	@NotNull
	@Column (name = "PICTURE_THUMB_URL", nullable = false, updatable = false)
	private String picture_thumb_url;
	
	@NotNull
	@Size (max = 500)
	@Column (name = "LINK", nullable = false, updatable = true)
	private String link;
	
	public PushMessage () {}
	
	/**
	 * Constructor for push message
	 * @param transactionId
	 */
	public PushMessage (String message) {
		setMessage(message);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPictureUrl() {
		return picture_url;
	}

	public void setPictureUrl(String picture_url) {
		this.picture_url = picture_url;
	}

	public String getPictureThumbUrl() {
		return picture_thumb_url;
	}

	public void setPictureThumbUrl(String picture_thumb_url) {
		this.picture_thumb_url = picture_thumb_url;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
