package com.demus.mayor.models;

import com.demus.mayor.utils.DB;
import com.demus.mayor.utils.DB2;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	int localID, gender, emailVerified, phoneVerified, pin;
	
	long dateJoined;
	
	double currentBalance;
	
	String userID, email, firstName, lastName, password, bio, mobileNetwork, countryCode, 
		   bank, bankNumber, phoneNumber, activationCode, profilePictureURL, authCode;
	
	public User() {
		localID = 1;
		activationCode = "";
		profilePictureURL = "";
		bio = "";
		bank = "";
		bankNumber = "";
		authCode = "";
		countryCode = "NGN";
		currentBalance = 0;
		phoneVerified = 0;
		emailVerified = 0;
	}
	
   /**
    * Method for inserting the user preferences
    * 
    * @param _pref
    */
   public synchronized void insert(Context context) {
	   SQLiteDatabase db = null;
	   
	   try {
		   // Create a new row of values to insert.
		   db = DB.getDatabase(context);
		   
		   
	       String sql = "INSERT INTO " + DB.TABLE_USER_APP_INFO + " (" + DB.KEY_USER_ID + ", " + DB.KEY_FIRST_NAME + ", " + DB.KEY_LAST_NAME + ", " + DB.KEY_PIN + ", " +
	    		   														 DB.KEY_PASSWORD + ", " + DB.KEY_PHONE_NUMBER + ", " + DB.KEY_EMAIL + ", " + DB.KEY_MOBILE_NETWORK + ", " +
	                                                                     DB.KEY_COUNTRY_CODE + ", " + DB.KEY_BANK + ", " + DB.KEY_BANK_NUMBER + ", " +
	                                                                     DB.KEY_PROFILE_PICTURE_URL + ", " + DB.KEY_BIO + ", " + DB.KEY_DATE_JOINED + ", " + 
	                                                                     DB.KEY_GENDER + ", " + DB.KEY_EMAIL_VERIFIED + ", " + DB.KEY_PHONE_VERIFIED + ", " + DB.KEY_AUTH_CODE + ", " +
	                                                                     DB.KEY_CURRENT_BALANCE
                                                                       + ") VALUES('"
	                                                                     + this.getUserID() + "', '"
	                                                                     + this.getFirstName().replace("'", "''") + "', '"
	                                                                     + this.getLastName().replace("'", "''") + "', "
	                                                                     + this.getPin() + ", '"
	                                                                     + this.getPassword() + "', '"
	                                                                     + this.getPhoneNumber() + "', '"
	                                                                     + this.getEmail() + "', '"
	                                                                     + this.getMobileNetwork() + "', '"
	                                                                     + this.getCountryCode() + "', '"
	                                                                     + this.getBank() + "', '"
	                                                                     + this.getBankNumber() + "', '"
	                                                                     + this.getProfilePictureURL() + "', '"
	                                                                     + this.getBio().replace("'", "''") + "', '"
	                                                                     + this.getDateJoined() + "', "
	                                                                     + this.getGender() + ", "
	                                                                     + this.getEmailVerified() + ", "
	                                                                     + this.getPhoneVerified() + ", '"
	                                                                     + this.getAuthCode() + "', '"
	                                                                     + this.getCurrentBalance() + "');";
	       db.execSQL(sql);
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	   finally {
			if(db != null)
				db.close();
		}
   }
   
   /**
    * Method for inserting the user preferences
    * 
    * @param _pref
    */
   public synchronized void insert(SQLiteDatabase db) {
	   try {
		   // Create a new row of values to insert.
		   String sql = "INSERT INTO " + DB.TABLE_USER_APP_INFO + " (" + DB.KEY_USER_ID + ", " + DB.KEY_FIRST_NAME + ", " + DB.KEY_LAST_NAME + ", " + DB.KEY_PIN + ", " +
						 											   DB.KEY_PASSWORD + ", " + DB.KEY_PHONE_NUMBER + ", " + DB.KEY_EMAIL + ", " + DB.KEY_MOBILE_NETWORK + ", " +
													                   DB.KEY_COUNTRY_CODE + ", " + DB.KEY_BANK + ", " + DB.KEY_BANK_NUMBER + ", " +
													                   DB.KEY_PROFILE_PICTURE_URL + ", " + DB.KEY_BIO + ", " + DB.KEY_DATE_JOINED + ", " + 
													                   DB.KEY_GENDER + ", " + DB.KEY_EMAIL_VERIFIED + ", " + DB.KEY_PHONE_VERIFIED + ", " + DB.KEY_AUTH_CODE + ", " +
													                   DB.KEY_CURRENT_BALANCE
													                 + ") VALUES('"
													                   + this.getUserID() + "', '"
													                   + this.getFirstName().replace("'", "''") + "', '"
													                   + this.getLastName().replace("'", "''") + "', "
													                   + this.getPin() + ", '"
													                   + this.getPassword() + "', '"
													                   + this.getPhoneNumber() + "', '"
													                   + this.getEmail() + "', '"
													                   + this.getMobileNetwork() + "', '"
													                   + this.getCountryCode() + "', '"
													                   + this.getBank() + "', '"
													                   + this.getBankNumber() + "', '"
													                   + this.getProfilePictureURL() + "', '"
													                   + this.getBio().replace("'", "''") + "', '"
													                   + this.getDateJoined() + "', "
													                   + this.getGender() + ", "
													                   + this.getEmailVerified() + ", "
													                   + this.getPhoneVerified() + ", '"
													                   + this.getAuthCode() + "', '"
													                   + this.getCurrentBalance() + "');";
	       db.execSQL(sql);
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
   }
   
   /**
    * Method for updating the user's preferences
    * 
    * @param _rowIndex
    * @param pref
    */
   public synchronized void update(Context context, String userID) {
	   SQLiteDatabase db = null;
	   
	   try {
		   // Update the row.
		   db = DB.getDatabase(context);
		          
	       String sql = "Update " + DB.TABLE_USER_APP_INFO 
	    		                  + " set " + DB.KEY_FIRST_NAME + " = '" + this.getFirstName().replace("'", "''")
		                          + "', " + DB.KEY_LAST_NAME + " = '" + this.getLastName().replace("'", "''")
		                          + "', " + DB.KEY_PROFILE_PICTURE_URL + " = '" + this.getProfilePictureURL()
		                          + "', " + DB.KEY_BIO + " = '" + this.getBio().replace("'", "''")
		                          + "', " + DB.KEY_EMAIL_VERIFIED + " = " + this.getEmailVerified()
		                          + ", " + DB.KEY_PHONE_VERIFIED + " = " + this.getPhoneVerified()
		                          + ", " + DB.KEY_AUTH_CODE + " = '" + this.getAuthCode()
		                          + "', " + DB.KEY_PIN + " = " + this.getPin()
		                          + ", " + DB.KEY_CURRENT_BALANCE + " = '" + this.getCurrentBalance()
		                          + "' where " + DB.KEY_USER_ID + " = '" + userID + "'";
	       db.execSQL(sql);
	   }
	   catch(Exception ex){
		   //ex.printStackTrace();
	   }
	   finally {
			if(db != null)
				db.close();
		}
   }
   
   /**
    * Method for updating the user's preferences
    * 
    * @param _rowIndex
    * @param pref
    */
   public synchronized void update(String userID, SQLiteDatabase db) {
	   try {
		   // Update the row.
		   String sql = "Update " + DB.TABLE_USER_APP_INFO 
			                  + " set " + DB.KEY_FIRST_NAME + " = '" + this.getFirstName().replace("'", "''")
	                       + "', " + DB.KEY_LAST_NAME + " = '" + this.getLastName().replace("'", "''")
	                       + "', " + DB.KEY_PROFILE_PICTURE_URL + " = '" + this.getProfilePictureURL()
	                       + "', " + DB.KEY_BIO + " = '" + this.getBio().replace("'", "''")
	                       + "', " + DB.KEY_EMAIL_VERIFIED + " = " + this.getEmailVerified()
	                       + ", " + DB.KEY_AUTH_CODE + " = '" + this.getAuthCode()
	                       + "', " + DB.KEY_PIN + " = " + this.getPin()
	                       + ", " + DB.KEY_CURRENT_BALANCE + " = '" + this.getCurrentBalance()
		                   + "' where " + DB.KEY_USER_ID + " = '" + userID + "'";
		   
	       db.execSQL(sql);
	   }
	   catch(Exception ex){
		   //ex.printStackTrace();
	   }
   }
   
   /**
    * Method for getting the user's preference 
    * 
    * @param _rowIndex
    * @return
    * @throws SQLException
    */
   public synchronized User getAppOwner(Context context) {
	   DB db2 = null;
	   DB2 db22 = null;
	   
	   try {
		   db2 = new DB(context);
		   db2.open();
		   db2.close();
		   
		   db22 = new DB2(context);
		   db22.open();
		   db22.close();
	   }
	   catch(Exception ex) {
		   
	   }
	   finally {
		   if(db2 != null)
			   db2.close();
		   if(db22 != null)
			   db22.close();
	   }
	   
	   SQLiteDatabase db = null;
	   Cursor item = null;
	   
	   try {
		   //operate
		   db = DB.getDatabase(context);
		   
		   item = db.query(true, DB.TABLE_USER_APP_INFO,  null, null, null, null, null, DB.KEY_ID + " asc", "1");
		   
		   if ((item.getCount() == 0) || !item.moveToFirst()) {
			   item.close();
			   db.close();
			   return null;
		   }
		   
		   User newItem = new User();
		   newItem.setLocalID(item.getInt(0));
		   newItem.setUserID(item.getString(1));
		   newItem.setFirstName(item.getString(2));
		   newItem.setLastName(item.getString(3));
		   newItem.setPin(item.getInt(4));
		   newItem.setPassword(item.getString(5));
		   newItem.setPhoneNumber(item.getString(6));
		   newItem.setEmail(item.getString(7));
		   newItem.setMobileNetwork(item.getString(8));
		   newItem.setCountryCode(item.getString(9));
		   newItem.setBank(item.getString(10));
		   newItem.setBankNumber(item.getString(11));
		   newItem.setProfilePictureURL(item.getString(12));
		   newItem.setBio(item.getString(13));
		   newItem.setDateJoined(Long.parseLong(item.getString(14)));
		   newItem.setGender(item.getInt(15));
		   newItem.setEmailVerified(item.getInt(16));
		   newItem.setPhoneVerified(item.getInt(17));
		   newItem.setAuthCode(item.getString(18));
		   newItem.setCurrentBalance(Double.parseDouble(item.getString(19)));
		    
		   return newItem;
	   } catch (Exception e) {
		   return null;
	   }
	   finally {
			if(item != null)
				item.close();
			if(db != null)
				db.close();
		}
   }
   
   /**
    * Method for getting the user's preference 
    * 
    * @param _rowIndex
    * @return
    * @throws SQLException
    */
   public synchronized User get(Context context, String userID) throws SQLException {
	   SQLiteDatabase db = null;
	   Cursor item = null;
	   
	   try {
		   //operate
		   db = DB.getDatabase(context);
		   
		   item = db.query(true, DB.TABLE_USER_APP_INFO,  null, DB.KEY_USER_ID + " = '" + userID + "'", null, null, null, null, null);
		   
		   if ((item.getCount() == 0) || !item.moveToFirst()) {
			   item.close();
			   db.close();
			   return null;
		   }
		   
		   User newItem = new User();
		   newItem.setLocalID(item.getInt(0));
		   newItem.setUserID(item.getString(1));
		   newItem.setFirstName(item.getString(2));
		   newItem.setLastName(item.getString(3));
		   newItem.setPin(item.getInt(4));
		   newItem.setPassword(item.getString(5));
		   newItem.setPhoneNumber(item.getString(6));
		   newItem.setEmail(item.getString(7));
		   newItem.setMobileNetwork(item.getString(8));
		   newItem.setCountryCode(item.getString(9));
		   newItem.setBank(item.getString(10));
		   newItem.setBankNumber(item.getString(11));
		   newItem.setProfilePictureURL(item.getString(12));
		   newItem.setBio(item.getString(13));
		   newItem.setDateJoined(Long.parseLong(item.getString(14)));
		   newItem.setGender(item.getInt(15));
		   newItem.setEmailVerified(item.getInt(16));
		   newItem.setPhoneVerified(item.getInt(17));
		   newItem.setAuthCode(item.getString(18));
		   newItem.setCurrentBalance(Double.parseDouble(item.getString(19)));
		   
		   return newItem;
		} catch (Exception e) {
			return null;
		}
	   finally {
			if(item != null)
				item.close();
			if(db != null)
				db.close();
		}
   }
   
   /**
    * Method for getting the user's preference 
    * 
    * @param _rowIndex
    * @return
    * @throws SQLException
    */
   public synchronized User get(String userID, SQLiteDatabase db) throws SQLException {
	   Cursor item = null;
	   
	   try {
		   //operate
		   item = db.query(true, DB.TABLE_USER_APP_INFO,  null, DB.KEY_USER_ID + " = '" + userID + "'", null, null, null, null, null);
		   
		   if ((item.getCount() == 0) || !item.moveToFirst()) {
			   item.close();
			   return null;
		   }
		   
		   User newItem = new User();
		   newItem.setLocalID(item.getInt(0));
		   newItem.setUserID(item.getString(1));
		   newItem.setFirstName(item.getString(2));
		   newItem.setLastName(item.getString(3));
		   newItem.setPin(item.getInt(4));
		   newItem.setPassword(item.getString(5));
		   newItem.setPhoneNumber(item.getString(6));
		   newItem.setEmail(item.getString(7));
		   newItem.setMobileNetwork(item.getString(8));
		   newItem.setCountryCode(item.getString(9));
		   newItem.setBank(item.getString(10));
		   newItem.setBankNumber(item.getString(11));
		   newItem.setProfilePictureURL(item.getString(12));
		   newItem.setBio(item.getString(13));
		   newItem.setDateJoined(Long.parseLong(item.getString(14)));
		   newItem.setGender(item.getInt(15));
		   newItem.setEmailVerified(item.getInt(16));
		   newItem.setPhoneVerified(item.getInt(17));
		   newItem.setAuthCode(item.getString(18));
		   newItem.setCurrentBalance(Double.parseDouble(item.getString(19)));
		   
		   return newItem;
		} catch (Exception e) {
			return null;
		}
	   finally {
			if(item != null)
				item.close();
		}
   }
   
   /**
     * Method for deleting faves
     * 
     * @param context
     */
    public synchronized void deleteAll(Context context) {
    	SQLiteDatabase db = null;
    	
    	try {
    		// Update the row.
    		db = DB.getDatabase(context);
	 	           
	        String sql = "Delete from " + DB.TABLE_USER_APP_INFO;
	        db.execSQL(sql);
		} catch (Exception e) {
			//e.printStackTrace();
		}
    	finally {
			if(db != null)
				db.close();
		}
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public int getLocalID() {
		return localID;
	}

	public void setLocalID(int localID) {
		this.localID = localID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public String getProfilePictureURL() {
		return profilePictureURL;
	}

	public void setProfilePictureURL(String profilePicture) {
		this.profilePictureURL = profilePicture;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(int emailVerified) {
		this.emailVerified = emailVerified;
	}

	public long getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(long dateJoined) {
		this.dateJoined = dateJoined;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public int getPhoneVerified() {
		return phoneVerified;
	}

	public void setPhoneVerified(int phoneVerified) {
		this.phoneVerified = phoneVerified;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public String getMobileNetwork() {
		return mobileNetwork;
	}

	public void setMobileNetwork(String mobileNetwork) {
		this.mobileNetwork = mobileNetwork;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankNumber() {
		return bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(localID);
		out.writeString(userID);
		out.writeString(firstName);
		out.writeString(lastName);
		out.writeString(password);
		out.writeString(email);
		out.writeString(phoneNumber);
		out.writeString(activationCode);
		out.writeString(profilePictureURL);
		out.writeString(bio);
		out.writeLong(dateJoined);
		out.writeInt(gender);
		out.writeInt(emailVerified);
		out.writeInt(phoneVerified);
		out.writeInt(pin);
		out.writeString(mobileNetwork);
		out.writeString(countryCode);
		out.writeString(bank);
		out.writeString(bankNumber);
		out.writeString(authCode);
		out.writeDouble(currentBalance);
	}
	
	/**
	 * This is a constructor for de-serializing the object
	 */
	private User(Parcel in) {
		this.setLocalID(in.readInt());
		this.setUserID(in.readString());
		this.setFirstName(in.readString());
		this.setLastName(in.readString());
		this.setPassword(in.readString());
		this.setEmail(in.readString());
		this.setPhoneNumber(in.readString());
		this.setProfilePictureURL(in.readString());
		this.setBio(in.readString());
		this.setDateJoined(in.readLong());
		this.setGender(in.readInt());
		this.setEmailVerified(in.readInt());
		this.setPhoneVerified(in.readInt());
		this.setPin(in.readInt());
		this.setMobileNetwork(in.readString());
		this.setCountryCode(in.readString());
		this.setBank(in.readString());
		this.setBankNumber(in.readString());
		this.setAuthCode(in.readString());
		this.setCurrentBalance(in.readDouble());
	}
	
	public static final Creator<User> CREATOR = new Creator<User>() {
		
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}
		
		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
}
