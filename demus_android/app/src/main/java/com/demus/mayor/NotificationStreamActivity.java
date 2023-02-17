package com.demus.mayor;

import java.util.ArrayList;
import java.util.Timer;

import com.demus.mayor.adapters.GenericAdapter2;
import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.handlers.UserAccountAPI_Handler;
import com.demus.mayor.models.ListObject;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * This is an activity class that facilitates link browsing
 * 
 * @author Lekan
 * @version v1.0
 * @since September, 2015
 * 
 */
public class NotificationStreamActivity extends AppCompatActivity {
	
	public static ArrayList<ListObject> items;

	static PullToRefreshListView notificationStream;
	static GenericAdapter2 adapter;
	
	Timer statusTimer;
	
	Parcelable state;
	
	static AppCompatActivity current;
	
	private Toolbar toolbar;
	
	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        
        if (savedInstanceState != null)
        	items = savedInstanceState.getParcelableArrayList("news_items");
        
        InitializeUI();
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
		
		setContentView(R.layout.activity_all_notifications);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.messages);
		
		current = this;
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();

		InitializeUI();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    	    
	    if(state != null)
	        notificationStream.getRefreshableView().onRestoreInstanceState(state);
	    
	    adapter.notifyDataSetChanged();
    }
	
	@Override
	public void onPause() {
	    // Save ListView state @ onPause
	    state = notificationStream.getRefreshableView().onSaveInstanceState();
	    super.onPause();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void InitializeUI() {
		items = new ListObject().getAllNotifications(NotificationStreamActivity.this);
		
		notificationStream = (PullToRefreshListView) findViewById(R.id.listStream);
		adapter = new GenericAdapter2(NotificationStreamActivity.this, R.layout.list_row, items);
		notificationStream.setAdapter(adapter);
		notificationStream.setOnRefreshListener(new OnRefreshListener<ListView>() {
			
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getNotifications();
			}
		});
		
		RelativeLayout layNothingHere = (RelativeLayout) findViewById(R.id.layNothingHere);
		layNothingHere.setVisibility(View.GONE);
		
		RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
		layProgress.setVisibility(View.VISIBLE);
		
		getNotifications();
	}
	
	static void getNotifications() {
		new Thread() {
			public void run() {
				ServerResponse resp = new TransactionAPI_Handler().getTransactions(current, 1);
				resp = new UserAccountAPI_Handler().getMessages(current, 1);
				
				Bundle bundle = new Bundle();
				bundle.putParcelable("response", resp);
				
				Message message = new Message();
				message.setData(bundle);
				notificationHandler.sendMessage(message);
			}
		}.start();
	}
	
	static Handler notificationHandler = new Handler() {
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);
        	
        	ArrayList<ListObject> fItems = new ListObject().getAllNotifications(current);
			
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
			
			RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
			layProgress.setVisibility(View.GONE);
			
			notificationStream.onRefreshComplete();
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
        outState.putParcelableArrayList("news_items", items);
    }
}