package com.demus.mayor.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.demus.mayor.utils.DB;
import com.demus.mayor.utils.DB2;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

public class ListObject implements Parcelable {

	private String title, subTitle, extraString, extraString2, extraString3, imageUrl, thumbImageUrl, mainID, balanceAfter;
	
	private int id, extraValue, isNotif;
	
	public ListObject() {
		extraString3 = "";
		extraString = "";
		imageUrl = "";
		isNotif = 0;
	}
	
	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}
	
	public String getMainID() {
		return mainID;
	}

	public void setMainID(String mainID) {
		this.mainID = mainID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	
	public int getExtraValue() {
		return extraValue;
	}

	public void setExtraValue(int extraValue) {
		this.extraValue = extraValue;
	}

	public String getExtraString() {
		return extraString;
	}

	public void setExtraString(String extraString) {
		this.extraString = extraString;
	}
	
	public String getExtraString2() {
		return extraString2;
	}

	public void setExtraString2(String extraString2) {
		this.extraString2 = extraString2;
	}
	
	public String getExtraString3() {
		return extraString3;
	}

	public void setExtraString3(String extraString3) {
		this.extraString3 = extraString3;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getThumbImageUrl() {
		return thumbImageUrl;
	}

	public void setThumbImageUrl(String imageUrl) {
		this.thumbImageUrl = imageUrl;
	}

	public int getIsNotif() {
		return isNotif;
	}

	public void setIsNotif(int isNotif) {
		this.isNotif = isNotif;
	}

	public String getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(String balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	/**
	 * Method for inserting the user preferences
	 *
	 */
	public synchronized void insertObject(SQLiteDatabase db) {
		 
		try {
			// Create a new row of values to insert.			
			String sql = "INSERT INTO " + DB2.TABLE_TRANSACTIONS + " ("
											+ DB2.KEY_TRANSACTION_ID + ", "
											+ DB2.KEY_TRANSACTION_DESC + ", "
											+ DB2.KEY_TRANSACTION_DATE + ", "
											+ DB2.KEY_TRANSACTION_STATUS + ", "
											+ DB2.KEY_TRANSACTION_IMAGE_URL + ", "
											+ DB2.KEY_TRANSACTION_IS_NOTIF + ", "
											+ DB2.KEY_TRANSACTION_MAIN_ID + ", "
											+ DB2.KEY_TRANSACTION_BALANCE_AFTER + ", "
											+ DB2.KEY_TRANSACTION_THUMB_IMAGE
										+ ") VALUES('"
													 + this.getSubTitle().replaceAll("'", "''") + "', '" 
													 + this.getExtraString2().replaceAll("'", "''") + "', '" 
													 + this.getExtraString3() + "', '"
													 + this.getExtraString() + "', '"
													 + this.getImageUrl() + "', "
													 + this.getIsNotif() + ", '"
													 + this.getMainID() + "', '"
													 + this.getBalanceAfter() + "', '"
													 + this.getThumbImageUrl() + "');";
			db.execSQL(sql);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Method for getting the user's preference
	 *
	 * @return
	 * @throws SQLException
	 */
	public synchronized ListObject getTransaction(SQLiteDatabase db) throws SQLException {
		Cursor item = null;
		
		try {
			// operate
			item = db.query(true, DB2.TABLE_TRANSACTIONS, null, DB2.KEY_TRANSACTION_ID + " = '" + this.getSubTitle() + "'", null, null, null, null, null);

			if ((item.getCount() == 0) || !item.moveToFirst()) {
				if(item != null)
					item.close();
				
				return null;
			}

			ListObject newItem = new ListObject();
			
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
	 * Method for getting the user's preference
	 *
	 * @return
	 * @throws SQLException
	 */
	public synchronized ListObject getNotification(SQLiteDatabase db) throws SQLException {
		Cursor item = null;
		
		try {
			// operate
			item = db.query(true, DB2.TABLE_TRANSACTIONS, null, DB2.KEY_TRANSACTION_MAIN_ID + " = '" + this.getMainID() + "'", null, null, null, null, null);

			if ((item.getCount() == 0) || !item.moveToFirst()) {
				if(item != null)
					item.close();
				
				return null;
			}

			ListObject newItem = new ListObject();
			
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
	public synchronized ArrayList<ListObject> getAllTransactions(Context context) {
		ArrayList<ListObject> result = new ArrayList<ListObject>();
		
		SQLiteDatabase db = null;
		Cursor item = null;

		try {
			db = DB2.getDatabase(context);
			
			item = db.query(true, DB2.TABLE_TRANSACTIONS, null, DB2.KEY_TRANSACTION_IS_NOTIF + " = 0", null, null, null, null, null);

			if (item.moveToFirst()) {
				do {
					ListObject newItem = new ListObject();
					newItem.setID(item.getInt(0));
					newItem.setSubTitle(item.getString(1));
					newItem.setTitle(item.getString(2));
					newItem.setExtraString2(item.getString(2));
					newItem.setExtraString3(item.getString(3));
					newItem.setExtraString(item.getString(4));
					newItem.setImageUrl(item.getString(5));
					newItem.setIsNotif(item.getInt(6));
					newItem.setMainID(item.getString(7));
					newItem.setBalanceAfter(item.getString(8));
					newItem.setThumbImageUrl(item.getString(9));

					result.add(newItem);
				} while (item.moveToNext());
			}
			
			ArrayList<ListObject> resultNoDup = new ArrayList<ListObject>();
			
			if(result.size() > 1) {
				Map<String, ListObject> cleanMap = new LinkedHashMap<String, ListObject>();
				for (int i = 0; i < result.size(); i++) {
				     cleanMap.put(String.valueOf(result.get(i).getSubTitle()), result.get(i));
				}
				
				resultNoDup = new ArrayList<ListObject>(cleanMap.values());
				
				// sort list of items by date and time
				Collections.sort(resultNoDup, new Comparator<ListObject>() {
	
					@Override
					public int compare(ListObject item1, ListObject item2) {
						return new Date(Long.parseLong(item2.getExtraString3())).compareTo(new Date(Long.parseLong(item1.getExtraString3())));
					}
				});
				
				result = null;
			}
			else
				resultNoDup = result;
			
			return resultNoDup;
		} catch (Exception e) {
			//e.printStackTrace();

			return result;
		}
		finally {
			if (item != null)
				item.close();
			if (db != null)
				db.close();
		}
	}
	
	/**
	 * Method for getting all the stored usage history
	 * 
	 * @param context
	 * @return - An ArrayList of UserInputValues class, an empty ArrayList is
	 *         returned if there are no usage history.
	 */
	public synchronized ArrayList<ListObject> getAllNotifications(Context context) {
		ArrayList<ListObject> result = new ArrayList<ListObject>();
		
		SQLiteDatabase db = null;
		Cursor item = null;

		try {
			db = DB2.getDatabase(context);
			
			item = db.query(true, DB2.TABLE_TRANSACTIONS, null, DB2.KEY_TRANSACTION_IS_NOTIF + " = 1", null, null, null, null, null);

			if (item.moveToFirst()) {
				do {
					ListObject newItem = new ListObject();
					newItem.setID(item.getInt(0));
					newItem.setSubTitle(item.getString(1));
					newItem.setTitle(item.getString(2));
					newItem.setExtraString2(item.getString(2));
					newItem.setExtraString3(item.getString(3));
					newItem.setExtraString(item.getString(4));
					newItem.setImageUrl(item.getString(5));
					newItem.setIsNotif(item.getInt(6));
					newItem.setMainID(item.getString(7));					
					newItem.setBalanceAfter(item.getString(8));
					newItem.setThumbImageUrl(item.getString(9));

					result.add(newItem);
				} while (item.moveToNext());
			}
			
			if(result.size() > 1) {
				// sort list of items by date and time
				Collections.sort(result, new Comparator<ListObject>() {
	
					@Override
					public int compare(ListObject item1, ListObject item2) {
						return new Date(Long.parseLong(item2.getExtraString3())).compareTo(new Date(Long.parseLong(item1.getExtraString3())));
					}
				});
			}
			
			return result;
		} catch (Exception e) {
			//e.printStackTrace();

			return result;
		}
		finally {
			if (item != null)
				item.close();
			if (db != null)
				db.close();
		}
	}
	
	public synchronized void updateTransaction(SQLiteDatabase db) {
	     // Update the row.
	     try {
	         StringBuilder sql = new StringBuilder();
	         sql.append("Update " + DB2.TABLE_TRANSACTIONS);
	         sql.append(" set " + DB2.KEY_TRANSACTION_DESC + " = '" + this.getExtraString2().replace("'", "''") + "'");
	         sql.append(", " + DB2.KEY_TRANSACTION_DATE + " = '" + this.getExtraString3() + "'");
	         sql.append(", " + DB2.KEY_TRANSACTION_STATUS + " = '" + this.getExtraString() + "'");
	         sql.append(", " + DB2.KEY_TRANSACTION_IMAGE_URL + " = '" + this.getImageUrl() + "'");
	         sql.append(", " + DB2.KEY_TRANSACTION_THUMB_IMAGE + " = '" + this.getThumbImageUrl() + "'");
	         sql.append(" where " + DB2.KEY_TRANSACTION_ID + " = '" + this.getSubTitle() + "'");
	         
	         db.execSQL(sql.toString());
		} catch (Exception e) {
		}
	}
	
	public synchronized void updateNotification(SQLiteDatabase db) {
	     // Update the row.
	     try {
	         StringBuilder sql = new StringBuilder();
	         sql.append("Update " + DB2.TABLE_TRANSACTIONS);
	         sql.append(" set " + DB2.KEY_TRANSACTION_DESC + " = '" + this.getExtraString2().replace("'", "''") + "'");
	         sql.append(", " + DB2.KEY_TRANSACTION_ID + " = '" + this.getSubTitle().replace("'", "''") + "'");
	         sql.append(", " + DB2.KEY_TRANSACTION_DATE + " = '" + this.getExtraString3() + "'");
	         sql.append(", " + DB2.KEY_TRANSACTION_BALANCE_AFTER + " = '" + this.getBalanceAfter() + "'");
	         sql.append(", " + DB2.KEY_TRANSACTION_IMAGE_URL + " = '" + this.getImageUrl() + "'");
	         sql.append(", " + DB2.KEY_TRANSACTION_THUMB_IMAGE + " = '" + this.getThumbImageUrl() + "'");
	         sql.append(" where " + DB2.KEY_TRANSACTION_MAIN_ID + " = '" + this.getMainID() + "'");
	         
	         db.execSQL(sql.toString());
		} catch (Exception e) {
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

			String sql = "Delete from " + DB2.TABLE_TRANSACTIONS;
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
     * Method for deleting faves
     * 
     * @param context
     */
    public synchronized void deleteAllTransactions(Context context) {
    	SQLiteDatabase db = null;
    	
    	try {
    		// Update the row.
    		db = DB.getDatabase(context);
	 	           
	        String sql = "Delete from " + DB2.TABLE_TRANSACTIONS + " where " + DB2.KEY_TRANSACTION_IS_NOTIF + " = 0";
	        db.execSQL(sql);
		} catch (Exception e) {
			//e.printStackTrace();
		}
    	finally {
			if(db != null)
				db.close();
		}
    }

	public ListObject(Parcel in) {
		this.setTitle(in.readString());
		this.setSubTitle(in.readString());
		this.setExtraValue(in.readInt());
		this.setExtraString(in.readString());
		this.setExtraString2(in.readString());
		this.setExtraString3(in.readString());
		this.setImageUrl(in.readString());
		this.setIsNotif(in.readInt());
		this.setMainID(in.readString());
		this.setBalanceAfter(in.readString());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(title);
		out.writeString(subTitle);
		out.writeInt(extraValue);
		out.writeString(extraString);
		out.writeString(extraString2);
		out.writeString(extraString3);
		out.writeString(imageUrl);
		out.writeInt(isNotif);
		out.writeString(mainID);
		out.writeString(balanceAfter);
	}
	
	public static final Creator<ListObject> CREATOR = new Creator<ListObject>() {

		@Override
		public ListObject createFromParcel(Parcel in) {
			return new ListObject(in);
		}

		@Override
		public ListObject[] newArray(int size) {
			return new ListObject[size];
		}
	};
}
