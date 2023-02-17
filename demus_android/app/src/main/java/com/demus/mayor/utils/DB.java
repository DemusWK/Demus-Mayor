package com.demus.mayor.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class instantiates the main SQLite database which is used for storing the user data and every other thing.
 * 
 * @author Lekan Baruwa
 * @email 
 * @version v1.0
 * @since July, 2015
 *
 */
public class DB { 
   public static final String DATABASE_NAME = "eclever.db";
   private static final int DATABASE_VERSION = 1;
   
   public static final String TABLE_USER_APP_INFO = "user_registration_info";
   
   public static final String KEY_ID = "_id";
   
   //Columns for User Registration info
   public static final String KEY_USER_ID = "user_id";
   public static final String KEY_FIRST_NAME = "first_name";
   public static final String KEY_LAST_NAME = "last_name";
   public static final String KEY_PIN = "pin";
   public static final String KEY_PASSWORD = "password";
   public static final String KEY_EMAIL = "email";
   public static final String KEY_PHONE_NUMBER = "phone_number";
   public static final String KEY_MOBILE_NETWORK = "mobile_network";
   public static final String KEY_COUNTRY_CODE = "country_code";
   public static final String KEY_BANK = "bank";
   public static final String KEY_BANK_NUMBER = "bank_number";
   public static final String KEY_PROFILE_PICTURE_URL = "profile_picture";
   public static final String KEY_BIO = "bio";
   public static final String KEY_DATE_JOINED = "date_joined";
   public static final String KEY_GENDER = "gender";
   public static final String KEY_EMAIL_VERIFIED = "email_verified";
   public static final String KEY_PHONE_VERIFIED = "phone_verified";
   public static final String KEY_AUTH_CODE = "auth_code";
   public static final String KEY_CURRENT_BALANCE = "current_balance";
      
   //Other Initializations
   public Context context;
   public SQLiteDatabase db;
   private OpenHelper dbHelper;
   
   static SQLiteDatabase db2;
 
   /*
    * This is the main constructor
    */
   public DB(Context context2) {
      context = context2;
      dbHelper = new OpenHelper(context);
   }
   
   /**
    * Method for opening the database for reading or writing
    * 
    * @return
    * @throws SQLiteException
    */
   public DB open() {
	   try {
		   db = dbHelper.getWritableDatabase();
	   } catch (SQLiteException ex) {
		   try {
			   db = dbHelper.getReadableDatabase();
			} catch (Exception e) {
			}
	   }
	   return this;
   }
   
   /**
    * Method for closing the database connection
    */
   public void close() {
	   db.close();
   }
   
   public static SQLiteDatabase getDatabase(Context context) {
	   try {
		   db2 = SQLiteDatabase.openDatabase(context.getDatabasePath(DB.DATABASE_NAME).getAbsolutePath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);		   
	   }
	   catch (Exception e) {
			try {
				   db2 = SQLiteDatabase.openDatabase(context.getDatabasePath(DB.DATABASE_NAME).getAbsolutePath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
			} 
			catch (SQLiteException ex) {
			   try {
				   db2 = new OpenHelper(context).getReadableDatabase();
			   } 
			   catch (Exception exe) {
			   }
			}
		}
	   
	   return db2;
   }
 
   /**
    * This is a helper class that facilitates the creation/upgrade of the database tables.
    * 
    * It extends the SQLiteOpenHelper
    *
    */
   public static class OpenHelper extends SQLiteOpenHelper {
 
      OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }
      
      private static final String DATABASE_CREATE0 = "create table " + TABLE_USER_APP_INFO + " (" + KEY_ID + " integer primary key autoincrement, " + 
    		  KEY_USER_ID + " text not null, " + KEY_FIRST_NAME + " text not null, " + KEY_LAST_NAME + " text not null, " +
    		  KEY_PIN + " integer not null, " + KEY_PASSWORD + " text not null, " + KEY_PHONE_NUMBER + " text not null, " +
    		  KEY_EMAIL + " text not null, " + KEY_MOBILE_NETWORK + " text not null, " + KEY_COUNTRY_CODE + " text not null, " + 
    		  KEY_BANK + " text not null, " + KEY_BANK_NUMBER + " text not null, " + KEY_PROFILE_PICTURE_URL + " text not null, " +
    		  KEY_BIO + " text not null, " + KEY_DATE_JOINED + " text not null, " + KEY_GENDER + " integer not null, " + 
    		  KEY_EMAIL_VERIFIED + " integer not null, " + KEY_PHONE_VERIFIED + " integer not null, " + KEY_AUTH_CODE + " text not null, " +
    		  KEY_CURRENT_BALANCE + " text not null);";
      
      
      @Override
      public void onCreate(SQLiteDatabase db) {
    	 db.execSQL(DATABASE_CREATE0);
      }
 
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {    	  
    	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_APP_INFO);
    	  
          onCreate(db);
      }
      
      @Override
      protected void finalize() throws Throwable {
          this.close();
          super.finalize();
      }
   }
}
