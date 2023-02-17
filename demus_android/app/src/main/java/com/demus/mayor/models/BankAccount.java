package com.demus.mayor.models;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.demus.mayor.utils.DB;
import com.demus.mayor.utils.DB2;

import java.util.ArrayList;

public class BankAccount implements Parcelable {

	private String accountNumber, bankName, accountType, bankOperatorCode;

	private boolean isPrimary;

	private int id;

	public BankAccount() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(boolean primary) {
		isPrimary = primary;
	}

	public String getBankOperatorCode() {
		return bankOperatorCode;
	}

	public void setBankOperatorCode(String bankOperatorCode) {
		this.bankOperatorCode = bankOperatorCode;
	}

	/**
	 * Method for inserting the user preferences
	 *
	 * @param db
	 */
	public synchronized void insert(SQLiteDatabase db) {

		try {
			// Create a new row of values to insert.
			String sql = "INSERT INTO " + DB2.TABLE_BANK_ACCOUNTS + " ("
										+ DB2.KEY_BANK_ACCOUNT_NUMBER + ", "
										+ DB2.KEY_BANK_ACCOUNT_BANK_NAME + ", "
										+ DB2.KEY_BANK_ACCOUNT_OPERATOR_CODE + ", "
										+ DB2.KEY_BANK_ACCOUNT_TYPE + ", "
										+ DB2.KEY_BANK_ACCOUNT_IS_PRIMARY
										+ ") VALUES('"
													 + this.getAccountNumber() + "', '"
													 + this.getBankName().replaceAll("'", "''") + "', '"
													 + this.getBankOperatorCode() + "', '"
													 + this.getAccountType() + "', "
													 + (this.getIsPrimary() ? 1 : 0) + ");";
			db.execSQL(sql);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	/**
	 * Method for getting the user's preference
	 *
	 * @param accountNumber
	 * @param context
	 * @return
	 * @throws SQLException
	 */
	public synchronized BankAccount get(String accountNumber, Context context) throws SQLException {
		SQLiteDatabase db = null;
		Cursor item = null;

		try {
			// operate
			db = DB2.getDatabase(context);

			item = db.query(true, DB2.TABLE_BANK_ACCOUNTS, null, DB2.KEY_BANK_ACCOUNT_NUMBER + " = '" + accountNumber + "'", null, null, null, null, null);

			if ((item.getCount() == 0) || !item.moveToFirst()) {
				if(item != null)
					item.close();

				if(db != null)
					db.close();

				return null;
			}

			BankAccount newItem = new BankAccount();
			newItem.setId(item.getInt(0));
			newItem.setAccountNumber(item.getString(1));
			newItem.setBankName(item.getString(2));
			newItem.setBankOperatorCode(item.getString(3));
			newItem.setAccountType(item.getString(4));
			newItem.setIsPrimary(item.getInt(5) == 1 ? true : false);

			item.close();
			db.close();

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
	 * @param context
	 * @return
	 * @throws SQLException
	 */
	public synchronized BankAccount getPrimaryAccount(Context context) throws SQLException {
		SQLiteDatabase db = null;
		Cursor item = null;

		try {
			// operate
			db = DB2.getDatabase(context);

			item = db.query(true, DB2.TABLE_BANK_ACCOUNTS, null, DB2.KEY_BANK_ACCOUNT_IS_PRIMARY + " = 1", null, null, null, null, null);

			if ((item.getCount() == 0) || !item.moveToFirst()) {
				if(item != null)
					item.close();

				if(db != null)
					db.close();

				return null;
			}

			BankAccount newItem = new BankAccount();
			newItem.setId(item.getInt(0));
			newItem.setAccountNumber(item.getString(1));
			newItem.setBankName(item.getString(2));
			newItem.setBankOperatorCode(item.getString(3));
			newItem.setAccountType(item.getString(4));
			newItem.setIsPrimary(item.getInt(5) == 1 ? true : false);

			item.close();
			db.close();

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
	 * Method for getting all the stored usage history
	 *
	 * @param context
	 * @return - An ArrayList of UserInputValues class, an empty ArrayList is
	 *         returned if there are no usage history.
	 */
	public synchronized ArrayList<BankAccount> getAllBankAccounts(Context context) {
		ArrayList<BankAccount> result = new ArrayList<BankAccount>();

		SQLiteDatabase db = null;
		Cursor item = null;

		try {
			db = DB2.getDatabase(context);

			item = db.query(true, DB2.TABLE_BANK_ACCOUNTS, null, null, null, null, null, null, null);

			if (item.moveToFirst()) {
				do {
					BankAccount newItem = new BankAccount();
					newItem.setId(item.getInt(0));
					newItem.setAccountNumber(item.getString(1));
					newItem.setBankName(item.getString(2));
					newItem.setBankOperatorCode(item.getString(3));
					newItem.setAccountType(item.getString(4));
					newItem.setIsPrimary(item.getInt(5) == 1 ? true : false);

					result.add(newItem);
				} while (item.moveToNext());
			}
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

		return result;
	}

	public synchronized void update(SQLiteDatabase db) {
	     // Update the row.
	     try {
	         StringBuilder sql = new StringBuilder();
	         sql.append("Update " + DB2.TABLE_BANK_ACCOUNTS);
	         sql.append(" set " + DB2.KEY_BANK_ACCOUNT_BANK_NAME + " = '" + this.getBankName().replace("'", "''") + "'");
	         sql.append(", " + DB2.KEY_BANK_ACCOUNT_OPERATOR_CODE + " = '" + this.getBankOperatorCode() + "'");
	         sql.append(", " + DB2.KEY_BANK_ACCOUNT_TYPE + " = '" + this.getAccountType() + "'");
	         sql.append(", " + DB2.KEY_BANK_ACCOUNT_IS_PRIMARY + " = " + (this.getIsPrimary() ? 1 : 0));
	         sql.append(" where " + DB2.KEY_BANK_ACCOUNT_NUMBER + " = '" + this.getAccountNumber() + "'");

	         db.execSQL(sql.toString());
		} catch (Exception e) {
		}
	}

	/**
	 * Method for deleting faves
	 *
	 * @param context
	 */
	public synchronized void delete(String accountNumber, Context context) {
		SQLiteDatabase db = null;

		try {
			// Update the row.
			db = DB.getDatabase(context);

			String sql = "Delete from " + DB2.TABLE_BANK_ACCOUNTS + " where " + DB2.KEY_BANK_ACCOUNT_NUMBER + " = '" + accountNumber + "'";
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
    public synchronized void deleteAll(Context context) {
    	SQLiteDatabase db = null;

    	try {
    		// Update the row.
    		db = DB.getDatabase(context);

	        String sql = "Delete from " + DB2.TABLE_BANK_ACCOUNTS;
	        db.execSQL(sql);
		} catch (Exception e) {
			//e.printStackTrace();
		}
    	finally {
			if(db != null)
				db.close();
		}
    }

	public BankAccount(Parcel in) {
        this.setId(in.readInt());
		this.setAccountNumber(in.readString());
		this.setBankName(in.readString());
		this.setBankOperatorCode(in.readString());
		this.setAccountType(in.readString());
        this.setIsPrimary(in.readByte() != 0);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
        out.writeInt(id);
		out.writeString(accountNumber);
		out.writeString(bankName);
        out.writeString(bankOperatorCode);
        out.writeString(accountType);
        out.writeByte((byte) (isPrimary ? 1 : 0));
	}
	
	public static final Creator<BankAccount> CREATOR = new Creator<BankAccount>() {

		@Override
		public BankAccount createFromParcel(Parcel in) {
			return new BankAccount(in);
		}

		@Override
		public BankAccount[] newArray(int size) {
			return new BankAccount[size];
		}
	};
}
