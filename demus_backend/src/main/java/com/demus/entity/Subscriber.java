package com.demus.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.demus.Exclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is a registered subscriber that has successfully 
 * activated the created retail account.
 * 
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity 
@Table (name = "SUBSCRIBER")
public class Subscriber extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Size (max = 255)
	@Column (name = "NAME", nullable = true)
	private String name;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "PHONE_NUMBER", nullable = false, unique = true, length = 11)
	private String phoneNumber;
	
	@Size (max = 255)
	@Column (name = "PASSWORD", nullable = true)
	@Exclude
	@JsonIgnore
	private String password;
	
	@Size (max = 255)
	@Column (name = "PIN", nullable = true)
	@Exclude
	@JsonIgnore
	private String pin;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "EMAIL", nullable = false, unique = true)
	@Email
	private String email;
	
	@Column (name = "GENDER", nullable = true)
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Size (max = 255)
	@Column (name = "DATE_OF_BIRTH", nullable = true)
	private String dateOfBirth;
	
	@OneToMany (fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "ADDRESSES", nullable = true)
	private List<Address> addresses;
	
	@Column (name = "FACEBOOK_ID", nullable = true)
	@JsonIgnore
	private String facebookId;
	
	@Column (name = "FACEBOOK_API_KEY", nullable = true)
	@JsonIgnore
	private String facebookApiKey;
	
	@Column (name = "GOOGLE_ID", nullable = true)
	@JsonIgnore
	private String googleId;
	
	@Column (name = "GOOGLE_API_KEY", nullable = true)
	@JsonIgnore
	private String googleApiKey;
	
	@ElementCollection (fetch = FetchType.EAGER)
	@Column (name = "SUBSCRIBER_TAGS", nullable = true)
	@Enumerated (EnumType.STRING)
	private Set<SubscriberTag> tags; 
	
	@OneToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "ROLE_ID", referencedColumnName = "ID", nullable = true)
	private Role role;
	
	@Column (name = "LAST_LOGIN", nullable = true)
	@Temporal (TemporalType.TIMESTAMP)
	private Date lastSeen;
	
	@Column (name = "LAST_IP_ADDRESS", nullable = true)
	private String lastIp;
	
	@OneToOne (fetch = FetchType.EAGER, mappedBy="subscriber")
	private Wallet wallet;
	
	@Transient
	private String authCode;

	public String getName() {
		return name;
	}
	
	//@JsonInclude
	public String getFirstName () {
		return name.split(" ")[0];
	}
	
	//@JsonInclude
	public String getLastName () {
		String[] names = name.split(" ");
		return names.length > 1 ? names [1] : "";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	//@JsonIgnore
	public String getPassword() {
		return password;
	}
	
	@Transient
	private transient BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	//@JsonProperty
	public void setPassword(String password) {
		this.password = encoder.encode(password);
	}
	
	//Used to update already encrypted password
	public void setEncryptedPassword (String password) {
		this.password = password;
	}
	
	public boolean matchPassword (String riskyPassword) {
		return encoder.matches(riskyPassword, getPassword());
	}

	//@JsonProperty
	public void setPin(String pin) {
		if(pin != null)
			this.pin = encoder.encode(pin);
	}
	
	//@JsonIgnore
	public String getPin() {
		return pin;
	}
	
	public void setEncryptedPin (String pin) {
		this.pin = pin;
	}
	
	public boolean matchPin (String riskyPin) {
		return encoder.matches(riskyPin, getPin());
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getFacebookApiKey() {
		return facebookApiKey;
	}

	public void setFacebookApiKey(String facebookApiKey) {
		this.facebookApiKey = facebookApiKey;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getGoogleApiKey() {
		return googleApiKey;
	}

	public void setGoogleApiKey(String googleApiKey) {
		this.googleApiKey = googleApiKey;
	}
	
	public Subscriber () {
		
	}
	
	public Subscriber (String phoneNumber, String firstName, String lastName, String password, String pin, String email, Gender gender, String dob, String facebookId, String facebookApiKey, String googleId, String googleApiKey) {
		setPhoneNumber(phoneNumber);
		String name = firstName + " " + lastName;
		setName(name);
		setPassword(password);
		setPin(pin);
		setEmail(email);
		setGender(gender);
		setDateOfBirth(dob);
		setFacebookId(facebookId);
		setFacebookApiKey(facebookApiKey);
		setGoogleId(googleId);
		setGoogleApiKey(googleApiKey);
	}

	public Set<SubscriberTag> getTags() {
		if (tags == null)
			tags = new HashSet<SubscriberTag>();
		return tags;
	}

	public void setTags(Set<SubscriberTag> tags) {
		this.tags = tags;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}
	
	//@JsonInclude
	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

}
