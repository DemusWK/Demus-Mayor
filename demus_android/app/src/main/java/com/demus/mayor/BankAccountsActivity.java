package com.demus.mayor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.demus.mayor.adapters.BankAccountAdapter;
import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.models.BankAccount;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.R;

import java.util.ArrayList;

public class BankAccountsActivity extends AppCompatActivity {

	private Toolbar toolbar;
	
	static AppCompatActivity current;
	
	static ListView listStream;
	static ArrayList<BankAccount> items;
	
	AlertDialog alertDialog;
	
	static BankAccountAdapter adapter;
	
	static Parcelable state;
	
	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        	items = savedInstanceState.getParcelableArrayList("items");
	}

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

		setContentView(R.layout.activity_bank_accounts);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.bank_accounts);
		
		current = this;
		
		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		items = new BankAccount().getAllBankAccounts(this);
		
		listStream = (ListView) findViewById(R.id.listStream);
		adapter = new BankAccountAdapter(BankAccountsActivity.this, R.layout.list_row_mgt, items);
		listStream.setAdapter(adapter);
		listStream.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				Intent intent = new Intent(getApplicationContext(), PrimaryBankAccountActivity.class);
				intent.putExtra("item", items.get(pos));
				startActivity(intent);
			}
		});
		
		RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
		layProgress.setVisibility(View.VISIBLE);
		
		//check for more recent transactions in the background
		getBankAccounts();
	}
	
	@Override
	public void onPause() {    
	    // Save ListView state @ onPause
	    state = listStream.onSaveInstanceState();
	    super.onPause();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    // Restore previous state (including selected item index and scroll position)
	    if(state != null)
	        listStream.onRestoreInstanceState(state);
    	
    	adapter.notifyDataSetChanged();
	}
	
	static void getBankAccounts() {
		new Thread() {
			@Override
			public void run() {
				ServerResponse response = new TransactionAPI_Handler().getBankAccounts(current, 1);

				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);

				Message message = new Message();
				message.setData(bundle);
				bankAccountsHandler.sendMessage(message);
			}
		}.start();
	}
	
	static Handler bankAccountsHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			ServerResponse response = msg.getData().getParcelable("response");
			
			if(response.getResponseCode().equals(Utility.STATUS_OK)) {
				ArrayList<BankAccount> fItems = new BankAccount().getAllBankAccounts(current);
				
				if(fItems.size() > 0) {
					items.clear();
					items.addAll(fItems);
	    		}
				
				RelativeLayout layNothingHere = (RelativeLayout) current.findViewById(R.id.layNothingHere);
				if(items.size() <= 0)
					layNothingHere.setVisibility(View.VISIBLE);
				else {
					layNothingHere.setVisibility(View.GONE);
					
					adapter.notifyDataSetChanged();
				}
			}
			else
				Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();
			
			RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
			layProgress.setVisibility(View.GONE);
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case 
				android.R.id.home:
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
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("items", items);
    }
}
