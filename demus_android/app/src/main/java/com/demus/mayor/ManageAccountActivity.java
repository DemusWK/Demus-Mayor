package com.demus.mayor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.demus.mayor.adapters.GenericAdapter;
import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.models.ListObject;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.R;

import android.annotation.SuppressLint;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ManageAccountActivity extends AppCompatActivity {

	//ListView listStream;
	
	GenericAdapter adapter;

	static AppCompatActivity current;
	
	AlertDialog alertDialog, alertDialog2;
	
	static final int IS_ACCT_STMT = 1,
			  		 IS_CHECK_BALANCE = 2;
	
	static int actionType = 0;
	
	int chosenStmtOption;
	
	String pin;
	String fromDate, toDate;

	static View newView;

	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
	
			// clear FLAG_TRANSLUCENT_STATUS flag:
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	
			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	
			// finally change the color
			window.setStatusBarColor(getResources().getColor(R.color.ColorPrimaryDark));
		}

		setContentView(R.layout.activity_manage_account);

		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.manage_account);
		
		current = this;

		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		ArrayList<ListObject> menuItems = getMenuItems();
		
		LayoutInflater li = LayoutInflater.from(this);
		newView = li.inflate(R.layout.prompt_enter_pin, null);
    	alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setView(newView);
		alertDialog.setCancelable(true);
		
		//set click listener
		((Button) newView.findViewById(R.id.cmdSend)).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				EditText txtPin = (EditText) alertDialog.findViewById(R.id.txtPin);
				
				if(txtPin.getText().toString().equals("")) {
					Toast.makeText(current, getString(R.string.incorrect_pin), Toast.LENGTH_LONG).show();
					alertDialog.dismiss();
					return;
				}
				
				RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
				layProgress.setVisibility(View.VISIBLE);
				
				pin = txtPin.getText().toString();
				
				if(actionType == IS_ACCT_STMT)
					getAccountStatement(pin, fromDate, toDate);
				
				//clear
				actionType = 0;
				txtPin.setText("");
				
				alertDialog.dismiss();
			}
		});
		
		View newView2 = li.inflate(R.layout.prompt_acct_stmt_options, null);
    	alertDialog2 = new AlertDialog.Builder(this).create();
		alertDialog2.setView(newView2);
		alertDialog2.setCancelable(true);
		alertDialog2.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
				layProgress.setVisibility(View.GONE);
			}
		});
		
		ListView listStream = (ListView) newView2.findViewById(R.id.listStream);
		ArrayAdapter<String> adapterM = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.acct_stmt_values));
		listStream.setAdapter(adapterM);
		listStream.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				chosenStmtOption = pos + 1;
				
				alertDialog2.dismiss();
				
				RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
				layProgress.setVisibility(View.VISIBLE);
				
				//Construct date
				Calendar calFrom = Calendar.getInstance(TimeZone.getTimeZone("Africa/Lagos"), Locale.UK);
				calFrom.set(Calendar.HOUR_OF_DAY, 0);
				calFrom.clear(Calendar.MINUTE);
				calFrom.clear(Calendar.SECOND);
				calFrom.clear(Calendar.MILLISECOND);
				
				Calendar calTo = Calendar.getInstance(TimeZone.getTimeZone("Africa/Lagos"), Locale.UK);
				
				if(chosenStmtOption == 2)
					calFrom.set(Calendar.DAY_OF_WEEK, calFrom.getFirstDayOfWeek());
				else if(chosenStmtOption == 3)
					calFrom.set(Calendar.DAY_OF_MONTH, 1);
				else if(chosenStmtOption == 4)
					calFrom.add(Calendar.DAY_OF_YEAR, -28);
				else if(chosenStmtOption == 5)
					calFrom.add(Calendar.DAY_OF_YEAR, -84);
				else if(chosenStmtOption == 6)
					calFrom.add(Calendar.DAY_OF_YEAR, -168);
				
				fromDate = Utility.getConstructedDate(calFrom.getTimeInMillis());
				toDate = Utility.getConstructedDate(calTo.getTimeInMillis());

				//show disclaimer
				((TextView) newView.findViewById(R.id.lblDisclaimer)).setVisibility(View.VISIBLE);
				
				alertDialog.show();
			}
		});		
		
		listStream = (ListView) findViewById(R.id.listStream);
		adapter = new GenericAdapter(this, R.layout.list_row_mgt, menuItems);
		listStream.setAdapter(adapter);
		listStream.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
//				if(pos == 0) {
//					actionType = IS_CHECK_BALANCE;
//
//					RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
//					layProgress.setVisibility(View.VISIBLE);
//
//					checkBalance();
//				}
				if(pos == 0) {
					Intent intent = new Intent(getApplicationContext(), ShareStockActivity.class);
					startActivity(intent);
				}
//				else if(pos == 2) {
//					Intent intent = new Intent(getApplicationContext(), CashbackActivity.class);
//					startActivity(intent);
//				}
				else if(pos == 1) {
					Intent intent = new Intent(getApplicationContext(), ConfirmTransactionActivity.class);
					startActivity(intent);
				}				
				else if(pos == 2) {
					actionType = IS_ACCT_STMT;
					alertDialog2.show();
				}
//				else if(pos == 4) {
//					Intent intent = new Intent(getApplicationContext(), BankAccountsActivity.class);
//					startActivity(intent);
//				}
				else if(pos == 3) {
					Intent intent = new Intent(getApplicationContext(), ChangePinActivity.class);
					startActivity(intent);
				}
				else if(pos == 4) {
					Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
					startActivity(intent);
				}
				else {
					Intent intent = new Intent(getApplicationContext(), PinResetActivity.class);
					startActivity(intent);
				}
			}
		});
	}
	
	private ArrayList<ListObject> getMenuItems() {
		ArrayList<ListObject> listObjects = new ArrayList<ListObject>();
		
//		ListObject object = new ListObject();
//		object.setTitle(getString(R.string.check_balance));
//		object.setSubTitle(getString(R.string.check_balance_sub));
//		listObjects.add(object);
		
		ListObject object = new ListObject();
		object.setTitle(getString(R.string.share_stock));
		object.setSubTitle(getString(R.string.share_stock_sub));
		listObjects.add(object);
		
//		object = new ListObject();
//		object.setTitle(getString(R.string.cash_back));
//		object.setSubTitle(getString(R.string.cash_back_sub));
//		listObjects.add(object);
		
		object = new ListObject();
		object.setTitle(getString(R.string.confirm_tranx));
		object.setSubTitle(getString(R.string.confirm_tranx_sub));
		listObjects.add(object);
		
		object = new ListObject();
		object.setTitle(getString(R.string.acct_stmt));
		object.setSubTitle(getString(R.string.acct_stmt_sub));		
		listObjects.add(object);

//		object = new ListObject();
//		object.setTitle(getString(R.string.view_bank_accounts));
//		object.setSubTitle(getString(R.string.view_bank_accounts_sub));
//		listObjects.add(object);
		
		object = new ListObject();
		object.setTitle(getString(R.string.change_pin));
		object.setSubTitle(getString(R.string.change_pin_sub));
		listObjects.add(object);		
		
		object = new ListObject();
		object.setTitle(getString(R.string.change_pass));
		object.setSubTitle(getString(R.string.change_pass_sub));
		listObjects.add(object);
		
		object = new ListObject();
		object.setTitle(getString(R.string.reset_pin));
		object.setSubTitle(getString(R.string.reset_pin_sub));
		listObjects.add(object);
		
		return listObjects;
	}
	
	private static void checkBalance() {
		new Thread() {
			@Override
			public void run() {
				ServerResponse response = new TransactionAPI_Handler().checkBalance(current, 1);

				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);
				bundle.putInt("actionType", IS_CHECK_BALANCE);

				Message message = new Message();
				message.setData(bundle);
				genericHandler.sendMessage(message);
			}
		}.start();
	}
	
	private static void getAccountStatement(final String pin, final String fromDate, final String toDate) {
		new Thread() {
			@Override
			public void run() {
				ServerResponse response = new TransactionAPI_Handler().getAccountStatement(pin, fromDate, toDate, current, 1);

				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);
				bundle.putInt("actionType", IS_ACCT_STMT);

				Message message = new Message();
				message.setData(bundle);
				genericHandler.sendMessage(message);
			}
		}.start();
	}
	
	@SuppressLint("HandlerLeak")
	static Handler genericHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
			layProgress.setVisibility(View.GONE);
			
			ServerResponse response = msg.getData().getParcelable("response");
			actionType = msg.getData().getInt("actionType");
			
			if(response.getResponseCode().equals(Utility.STATUS_OK)) {
				if(actionType == IS_ACCT_STMT)
					Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();
				
				if(response.getResponseMessage().contains("expired")) {
					Intent i = new Intent(current, PinResetActivity.class);
					current.startActivity(i);
				}
			}

			if(actionType == IS_ACCT_STMT)
				//hide disclaimer
				((TextView) newView.findViewById(R.id.lblDisclaimer)).setVisibility(View.GONE);
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
