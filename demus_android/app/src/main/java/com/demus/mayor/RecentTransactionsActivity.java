package com.demus.mayor;

import java.util.ArrayList;

import com.demus.mayor.adapters.GenericAdapter2;
import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.models.ListObject;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.R;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RecentTransactionsActivity extends AppCompatActivity {

	private Toolbar toolbar;
	
	static AppCompatActivity current;
	
	static ListView listStream;
	static ArrayList<ListObject> items;
	
	AlertDialog alertDialog;
	
	static GenericAdapter2 adapter;
	
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

		setContentView(R.layout.activity_transactions);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.transactions);
		
		current = this;
		
		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		items = new ListObject().getAllTransactions(RecentTransactionsActivity.this);
		
		listStream = (ListView) findViewById(R.id.listStream);
		adapter = new GenericAdapter2(RecentTransactionsActivity.this, R.layout.list_row, items);
		listStream.setAdapter(adapter);
		listStream.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				Intent intent = new Intent(getApplicationContext(), TransactionFullViewActivity.class);
				intent.putExtra("item", items.get(pos));
				startActivity(intent);
			}
		});
		listStream.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("Copied Text", items.get(pos).getSubTitle());
				clipboard.setPrimaryClip(clip);
				
				Toast.makeText(RecentTransactionsActivity.this, getString(R.string.tranx_id_copied), Toast.LENGTH_LONG).show();
				
				return false;
			}
		});
		
		RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
		layProgress.setVisibility(View.VISIBLE);
		
		//check for more recent transactions in the background
		getTransactions();
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
	
	static void getTransactions() {
		new Thread() {
			@Override
			public void run() {
				ServerResponse response = new TransactionAPI_Handler().getTransactions(current, 1);

				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);

				Message message = new Message();
				message.setData(bundle);
				transactionsHandler.sendMessage(message);
			}
		}.start();
	}
	
	static Handler transactionsHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			ServerResponse response = msg.getData().getParcelable("response");
			
			if(response.getResponseCode().equals(Utility.STATUS_OK)) {
				ArrayList<ListObject> fItems = new ListObject().getAllTransactions(current);
				
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
