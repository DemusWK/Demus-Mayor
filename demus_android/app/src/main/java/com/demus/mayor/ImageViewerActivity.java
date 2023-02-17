package com.demus.mayor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;

import com.demus.mayor.utils.ImageLoaderNoCache;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("InlinedApi")
/**
 * This is an activity class that facilitates link browsing
 * 
 * @author Lekan
 * @version v1.0
 * @since September, 2015
 * 
 */
public class ImageViewerActivity extends AppCompatActivity {

	Toolbar toolbar;
	
	String originalUrl;

	AppCompatActivity activity;
	ProgressDialog progDialog;
	
	ImageView imgMain;
	
	ProgressBar progressBar;
	
	static ImageLoaderNoCache imageLoader;
	
	Intent returnedIntent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
	
			// clear FLAG_TRANSLUCENT_STATUS flag:
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	
			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	
			// finally change the color
			window.setStatusBarColor(Color.BLACK);
		}
		
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);		
		
		imageLoader = new ImageLoaderNoCache(ImageViewerActivity.this);

		InitializeUI();
	}

	/**
	 * This method initialises the User Interface controls.
	 */
	private void InitializeUI() {
		originalUrl = getIntent().getStringExtra("url");
				
		if(originalUrl == null)
			finish();
				
		imgMain = (ImageView) findViewById(R.id.imgMain);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		if(originalUrl.startsWith("http"))
    		imageLoader.DisplayImage(originalUrl.replace(" ", "%20"), imgMain, progressBar);
    	else
    		finish();
	}
	
	void saveImage(final String url) {
		new Thread() {
			@Override
			public void run() {
				boolean res = imageLoader.storeImage(url, getApplicationContext());
				
				Bundle bundle = new Bundle();
				bundle.putBoolean("res", res);
				
				Message message = new Message();
				message.setData(bundle);
				
				saveImageHandler.sendMessage(message);
			}
		}.start();
	}
	
	Handler saveImageHandler = new Handler() {
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);
        	
        	boolean res = msg.getData().getBoolean("res");
        	
        	if(res)
				Toast.makeText(getApplicationContext(), R.string.save_img_success, Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), R.string.save_img_fail, Toast.LENGTH_SHORT).show();
        }
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuItem3 = menu.add(R.string.save_image);
        menuItem3.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menuItem3.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				if(originalUrl.startsWith("/Uploads"))
					saveImage((Utility.BASE_URL + originalUrl).replace(" ", "%20"));
				else
					saveImage(originalUrl.replace(" ", "%20"));
				
		    	return true;
			}
		});  
        
        return super.onCreateOptionsMenu(menu);
	}
	
	public void onConfigurationChanged(Configuration _newConfig) {
    	super.onConfigurationChanged(_newConfig);
    	
    	if (_newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
    		getSupportActionBar().show();
    	if (_newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
    		getSupportActionBar().hide();
    }
}