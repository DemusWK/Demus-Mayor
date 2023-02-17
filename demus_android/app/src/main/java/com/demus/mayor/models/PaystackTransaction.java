package com.demus.mayor.models;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.demus.mayor.utils.Utility;
import com.demus.mayor.utils.DB2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class PaystackTransaction implements Parcelable {

	int ID, subscriptionValue;
	
	long rawDate;
	
	String dateCreated, subscriptionRef, userID;

	public PaystackTransaction() {

    }

	public PaystackTransaction(String ref, int value, String userID) {
		setPaystackTransactionRef(ref);
		setPaystackTransactionValue(value);
		setUserID(userID);
		setRawDate(System.currentTimeMillis());
	}
	
	/**
    * Method for inserting the user preferences
    * 
    * @param context
    */
	public synchronized void insert(Context context) {
		SQLiteDatabase db = null;
		
	   try {
		   db = DB2.getDatabase(context);
		   
		   // Create a new row of values to insert.
	       String sql = "INSERT INTO " + DB2.TABLE_PAYSTACK + " (" + DB2.KEY_SUBSCRIPTION_REF + ", "
																		 + DB2.KEY_SUBSCRIPTION_VALUE + ", "
                                                                         + DB2.KEY_SUBSCRIPTION_USER_ID + ", "
				   														 + DB2.KEY_SUBSCRIPTION_EXPIRY_DATE
																		 + ") VALUES('"
																			 + this.getPaystackTransactionRef() + "', "
																			 + this.getPaystackTransactionValue() + ", '"
                                                                             + this.getUserID() + "', '"
																			 + this.getRawDate() + "');";
	       db.execSQL(sql);
	   } catch (Exception e) {
		   //e.printStackTrace();
	   }
	   finally {
		   if(db != null)
			   db.close();
	   }
    }
	
		/**
	    * Method for getting the user's preference 
	    * 
	    * @param subscriptionRef
        * @param context
	    * @return
	    * @throws SQLException
	    */
	   public synchronized PaystackTransaction get(String subscriptionRef, Context context) throws SQLException {
		   SQLiteDatabase db = null;
		   Cursor item = null;
		   
		   try {
			   //operate
			   db = DB2.getDatabase(context);
			   
			   item = db.query(true, DB2.TABLE_PAYSTACK,  null, DB2.KEY_SUBSCRIPTION_REF + " = '" + subscriptionRef + "'", null, null, null, null, null);
			   
			   if ((item.getCount() == 0) || !item.moveToFirst()) {
				   return null;
			   }
			   
			   PaystackTransaction newItem = new PaystackTransaction();
			   newItem.setID(item.getInt(0));
			   newItem.setPaystackTransactionRef(item.getString(1));
			   newItem.setPaystackTransactionValue(item.getInt(2));
               newItem.setUserID(item.getString(3));
			   newItem.setRawDate(Long.parseLong(item.getString(4)));
			   newItem.setDateCreated(Utility.getPrettyDate(newItem.getRawDate()));
			   
			   item.close();
			   db.close();
			   
			   return newItem;
			} catch (Exception e) {
				return null;
			}
		   finally {
				if (item != null)
					item.close();
				if (db != null)
					db.close();
			}
	   }
	   
	   /**
	    * Method for getting the user's preference 
	    * 
	    * @param subscriptionRef
        * @param db
	    * @return
	    * @throws SQLException
	    */
	   public synchronized PaystackTransaction get(String subscriptionRef, SQLiteDatabase db) throws SQLException {
		   Cursor item = null;
		   
		   try {
			   //operate
			   item = db.query(true, DB2.TABLE_PAYSTACK,  null, DB2.KEY_SUBSCRIPTION_REF + " = '" + subscriptionRef + "'", null, null, null, null, null);
			   
			   if ((item.getCount() == 0) || !item.moveToFirst()) {
				   return null;
			   }
			   
			   PaystackTransaction newItem = new PaystackTransaction();
               newItem.setID(item.getInt(0));
               newItem.setPaystackTransactionRef(item.getString(1));
               newItem.setPaystackTransactionValue(item.getInt(2));
               newItem.setUserID(item.getString(3));
               newItem.setRawDate(Long.parseLong(item.getString(4)));
               newItem.setDateCreated(Utility.getPrettyDate(newItem.getRawDate()));
			   
			   item.close();
			   
			   return newItem;
			} catch (Exception e) {
				return null;
			}
		   finally {
				if (item != null)
					item.close();
			}
	   }
	   
	   /**
		 * Method for getting all the stored usage history
		 * 
		 * @param context
		 * @return - An ArrayList of UserInputValues class, an empty ArrayList is
		 *         returned if there are no usage history.
		 */
		public synchronized ArrayList<PaystackTransaction> getAll(String userID, Context context) {
			ArrayList<PaystackTransaction> result = new ArrayList<PaystackTransaction>();

			SQLiteDatabase db = null;
			Cursor item = null;

			try {
                db = DB2.getDatabase(context);

				item = db.query(DB2.TABLE_PAYSTACK, null, DB2.KEY_SUBSCRIPTION_USER_ID + " = '" + userID + "'", null, null, null, DB2.KEY_ID + " ASC", null);

				if (item.moveToFirst()) {
					do {
						PaystackTransaction newItem = new PaystackTransaction();
                        newItem.setID(item.getInt(0));
                        newItem.setPaystackTransactionRef(item.getString(1));
                        newItem.setPaystackTransactionValue(item.getInt(2));
                        newItem.setUserID(item.getString(3));
                        newItem.setRawDate(Long.parseLong(item.getString(4)));
                        newItem.setDateCreated(Utility.getPrettyDate(newItem.getRawDate()));
		
						result.add(newItem);
					} while (item.moveToNext());
				}
				
				// sort list of items by date and time
				Collections.sort(result, new Comparator<PaystackTransaction>() {

					@Override
					public int compare(PaystackTransaction item1, PaystackTransaction item2) {
						return new Date(item2.getRawDate()).compareTo(new Date(item1.getRawDate()));
					}
				});
			} 
			catch (Exception e) {
				//e.printStackTrace();
			}
			finally {
				if (item != null)
					item.close();
			}
			
			return result;
		}
	   
	   public synchronized void update(Context context) {
			SQLiteDatabase db = null;
			
			try {
				db = DB2.getDatabase(context);
				
				StringBuilder sql = new StringBuilder();
		        sql.append("Update " + DB2.TABLE_PAYSTACK); 
		        sql.append(" set " + DB2.KEY_SUBSCRIPTION_VALUE + " = " + this.getRawDate());
		        sql.append(" where " + DB2.KEY_SUBSCRIPTION_REF + " = '" + this.getPaystackTransactionRef() + "'");
		        
		        db.execSQL(sql.toString());
			} catch (Exception e) {
			}
			finally {
				if (db != null)
					db.close();
			}
	    }
	   
	   /**
	     * Method for deleting all stored clubs
	     * 
	     * @param context
	     */
	    public synchronized void deleteAll(Context context) {
	    	SQLiteDatabase db = null;
	    	
	    	try {
	    		db = DB2.getDatabase(context);
	    		
	    		// Delete the row.
		        String sql = "Delete from " + DB2.TABLE_PAYSTACK;
		        db.execSQL(sql);
			} catch (Exception e) {
				//e.printStackTrace();
			}
	    	finally {
	 		   if(db != null)
	 			   db.close();
	 	   }
	    }

    /**
     * Method for deleting all stored countries
     *
     * @param context
     */
    public synchronized void delete(Context context) {
        SQLiteDatabase db = null;

        try {
            // Update the row.
            db = DB2.getDatabase(context);

            String sql = "Delete from " + DB2.TABLE_PAYSTACK + " where " +
                                          DB2.KEY_SUBSCRIPTION_REF + " = '" + this.getPaystackTransactionRef() + "'";
            db.execSQL(sql);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        finally {
            if(db != null)
                db.close();
        }
    }

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getPaystackTransactionValue() {
		return subscriptionValue;
	}

	public void setPaystackTransactionValue(int subscriptionValue) {
		this.subscriptionValue = subscriptionValue;
	}

	public long getRawDate() {
		return rawDate;
	}

	public void setRawDate(long expiryDate) {
		this.rawDate = expiryDate;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateOfExpiry) {
		this.dateCreated = dateOfExpiry;
	}

    public String getPaystackTransactionRef() {
        return subscriptionRef;
    }

    public void setPaystackTransactionRef(String subscriptionRef) {
        this.subscriptionRef = subscriptionRef;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Overidden method for serializing the class attributes
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(ID);
        out.writeString(subscriptionRef);
        out.writeString(userID);
        out.writeString(dateCreated);
        out.writeLong(rawDate);
        out.writeInt(subscriptionValue);
    }

    /**
     * This is a constructor for de-serializing the object
     */
    private PaystackTransaction(Parcel in) {
        this.setID(in.readInt());
        this.setPaystackTransactionRef(in.readString());
        this.setUserID(in.readString());
        this.setDateCreated(in.readString());
        this.setPaystackTransactionValue(in.readInt());
    }

    public static final Parcelable.Creator<PaystackTransaction> CREATOR = new Parcelable.Creator<PaystackTransaction>() {

        @Override
        public PaystackTransaction createFromParcel(Parcel in) {
            return new PaystackTransaction(in);
        }

        @Override
        public PaystackTransaction[] newArray(int size) {
            return new PaystackTransaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
