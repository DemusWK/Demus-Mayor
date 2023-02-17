package com.demus.mayor.models;

import java.util.ArrayList;

import com.demus.mayor.utils.DB2;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

public class Bank implements Parcelable {
	int ID;
	
	String bankName, 
		   bankCode;
	
	public Bank() {
		
	}
	
	/**
    * Method for inserting the user preferences
    * 
    * @param _pref
    */
   public synchronized void insert(SQLiteDatabase db) {
	   try {
		   // Create a new row of values to insert.
		   String sql = "INSERT INTO " + DB2.TABLE_BANKS + " (" + DB2.KEY_BANK_CODE + ", " + DB2.KEY_BANK_NAME
													                 + ") VALUES('"
													                   + this.getBankCode() + "', '"
													                   + this.getBankName().replace("'", "''") + "');";
	       db.execSQL(sql);
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
   }
   
   /**
    * Method for getting the user's preference 
    * 
    * @param _rowIndex
    * @return
    * @throws SQLException
    */
   public synchronized Bank get(String bankCode, SQLiteDatabase db) throws SQLException {
	   Cursor item = null;
	   
	   try {
		   //operate
		   item = db.query(true, DB2.TABLE_BANKS,  null, DB2.KEY_BANK_CODE + " = '" + bankCode + "'", null, null, null, null, null);
		   
		   if ((item.getCount() == 0) || !item.moveToFirst()) {
			   item.close();
			   return null;
		   }
		   
		   Bank newItem = new Bank();
		   newItem.setID(item.getInt(0));
		   newItem.setBankCode(item.getString(1));
		   newItem.setBankName(item.getString(2));
		   
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
    * Method for getting all the stored usage history
    * 
    * @param context
    * @return - An ArrayList of UserInputValues class, an empty ArrayList is returned if there are no usage history.
    */
   public synchronized ArrayList<Bank> getAll(Context context) {
	   ArrayList<Bank> result = new ArrayList<Bank>();
	   
	   SQLiteDatabase db = null;
	   Cursor item = null;
	   
	   try {
		   db = DB2.getDatabase(context);
		   
		   item = db.query(DB2.TABLE_BANKS, null, null, null, null, null, DB2.KEY_ID + " ASC", null);
		   
		    if (item.moveToFirst()){
			   do {
				   Bank newItem = new Bank();
				   newItem.setID(item.getInt(0));
				   newItem.setBankCode(item.getString(1));
				   newItem.setBankName(item.getString(2));
				   
				   result.add(newItem);
			   } while(item.moveToNext());
		    }
		    
		    item.close();
		    
		   return result;
	   }
	   catch (Exception e) {
		   //e.printStackTrace();
		   
		   return result;
	   }
	   finally {
			if (item != null)
				item.close();
			if(db != null)
				db.close();
		}
	}
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Bank(Parcel in) {
		this.setBankCode(in.readString());
		this.setBankName(in.readString());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(bankCode);
		out.writeString(bankName);
	}
	
	public static final Creator<Bank> CREATOR = new Creator<Bank>() {

		@Override
		public Bank createFromParcel(Parcel in) {
			return new Bank(in);
		}

		@Override
		public Bank[] newArray(int size) {
			return new Bank[size];
		}
	};
}
