package com.demus.mayor.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class instantiates the extra SQLite database which is used for storing the user data and every other thing.
 * 
 * @author Lekan Baruwa
 * @email 
 * @version v1.0
 * @since July, 2015
 *
 */
public class DB2 { 
   public static final String DATABASE_NAME = "eclever_extra.db";
   private static final int DATABASE_VERSION = 4;
   
   public static final String TABLE_BANKS = "banks";
   public static final String TABLE_TRANSACTIONS = "transactions";
   public static final String TABLE_PAYSTACK = "paystack";
   public static final String TABLE_BANK_ACCOUNTS = "bank_accounts";
   
   public static final String KEY_ID = "_id";
   
   //Banks
   public static final String KEY_BANK_CODE = "bank_code";
   public static final String KEY_BANK_NAME = "bank_name";
   
   //Transactions
   public static final String KEY_TRANSACTION_ID = "tranx_id";
   public static final String KEY_TRANSACTION_DESC = "tranx_desc";
   public static final String KEY_TRANSACTION_MAIN_ID = "main_id";
   public static final String KEY_TRANSACTION_DATE = "tranx_date";
   public static final String KEY_TRANSACTION_STATUS = "tranx_status";
   public static final String KEY_TRANSACTION_IMAGE_URL = "image_url";
   public static final String KEY_TRANSACTION_IS_NOTIF = "is_notif";
   public static final String KEY_TRANSACTION_BALANCE_AFTER = "bal_after";
   public static final String KEY_TRANSACTION_THUMB_IMAGE = "thumb_image";

    //Predictor subscriptions
    public static final String KEY_SUBSCRIPTION_REF = "pSub_ref";
    public static final String KEY_SUBSCRIPTION_VALUE = "pSub_value";
    public static final String KEY_SUBSCRIPTION_USER_ID = "pSub_userID";
    public static final String KEY_SUBSCRIPTION_EXPIRY_DATE = "pSub_expDate";

    //Predictor subscriptions
    public static final String KEY_BANK_ACCOUNT_NUMBER = "bank_acc_numbere";
    public static final String KEY_BANK_ACCOUNT_BANK_NAME = "bank_acc_bank_name";
    public static final String KEY_BANK_ACCOUNT_OPERATOR_CODE = "bank_acc_operator_code";
    public static final String KEY_BANK_ACCOUNT_TYPE = "bank_acc_type";
    public static final String KEY_BANK_ACCOUNT_IS_PRIMARY = "bank_acc_is_primary";
      
   //Other Initializations
   public Context context;
   public SQLiteDatabase db;
   private OpenHelper dbHelper;
   
   static SQLiteDatabase db2;
 
   /*
    * This is the main constructor
    */
   public DB2(Context context2) {
      context = context2;
      dbHelper = new OpenHelper(context);
   }
   
   /**
    * Method for opening the database for reading or writing
    * 
    * @return
    * @throws SQLiteException
    */
   public DB2 open() {
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
		   db2 = SQLiteDatabase.openDatabase(context.getDatabasePath(DB2.DATABASE_NAME).getAbsolutePath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);		   
	   }
	   catch (Exception e) {
			try {
				db2 = SQLiteDatabase.openDatabase(context.getDatabasePath(DB2.DATABASE_NAME).getAbsolutePath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
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
      
      private static final String DATABASE_CREATE0 = "create table " + TABLE_BANKS + " (" + KEY_ID + " integer primary key autoincrement, " + 
    		  KEY_BANK_CODE + " text not null, " + KEY_BANK_NAME + " text not null);";
      
      private static final String DATABASE_CREATE1 = "create table " + TABLE_TRANSACTIONS + " (" + KEY_ID + " integer primary key autoincrement, " + 
    		  KEY_TRANSACTION_ID + " text not null, " + KEY_TRANSACTION_DESC + " text not null, " + KEY_TRANSACTION_DATE + " text not null, " +
    		  KEY_TRANSACTION_STATUS + " text not null, " + KEY_TRANSACTION_IMAGE_URL + " text not null, " + KEY_TRANSACTION_IS_NOTIF + " integer not null, " +
    		  KEY_TRANSACTION_MAIN_ID + " text not null, " + KEY_TRANSACTION_BALANCE_AFTER + " text not null, " + KEY_TRANSACTION_THUMB_IMAGE + " text not null);";

      private static final String DATABASE_CREATE2 = "create table " + TABLE_PAYSTACK + " (" + KEY_ID + " integer primary key autoincrement, " +
              KEY_SUBSCRIPTION_REF + " text not null, " + KEY_SUBSCRIPTION_VALUE + " integer not null, " + KEY_SUBSCRIPTION_USER_ID + " text not null, " +
              KEY_SUBSCRIPTION_EXPIRY_DATE + " text not null);";

      private static final String DATABASE_CREATE3 = "create table " + TABLE_BANK_ACCOUNTS + " (" + KEY_ID + " integer primary key autoincrement, " +
              KEY_BANK_ACCOUNT_NUMBER + " text not null, " + KEY_BANK_ACCOUNT_BANK_NAME + " text not null, " + KEY_BANK_ACCOUNT_OPERATOR_CODE + " text not null, " +
              KEY_BANK_ACCOUNT_TYPE + " text not null, " + KEY_BANK_ACCOUNT_IS_PRIMARY + " integer not null);";

      @Override
      public void onCreate(SQLiteDatabase db) {
    	 db.execSQL(DATABASE_CREATE0);
    	 db.execSQL(DATABASE_CREATE1);
    	 db.execSQL(DATABASE_CREATE2);
    	 db.execSQL(DATABASE_CREATE3);
      }
 
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANKS);
    	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYSTACK);
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANK_ACCOUNTS);
    	  
          onCreate(db);
      }
      
      @Override
      protected void finalize() throws Throwable {
          this.close();
          super.finalize();
      }
   }
}