package com.demus.mayor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.demus.mayor.R;

@SuppressLint("InlinedApi")
@SuppressWarnings("deprecation")
/**
 * This is an activity class that facilitates link browsing
 * 
 * @author Lekan
 * @version v1.0
 * @since September, 2015
 * 
 */
public class BrowserActivity extends AppCompatActivity {

	//NewsItem newsItem;
	String originalUrl;

	AppCompatActivity activity;
	ProgressDialog progDialog;
	
	WebView mWebView;
	
	boolean isFirstTime = true;
	
	private Toolbar toolbar;
	
	static RelativeLayout layProgress;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
	
			// clear FLAG_TRANSLUCENT_STATUS flag:
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	
			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	
			// finally change the color
			window.setStatusBarColor(getResources().getColor(R.color.ColorPrimaryDark));
		}

		//set content
		setContentView(R.layout.activity_qt_pay_browser);
		
		// setup header
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		InitializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void InitializeUI() {
		originalUrl = getIntent().getStringExtra("originalUrl");
		
		if(originalUrl == null)
			finish();
		else {
			if(!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://"))
				originalUrl = "http://" + originalUrl;
			
			mWebView = (WebView) findViewById(R.id.webView1);
			mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	
			activity = this;
			
			getSupportActionBar().setTitle(getString(R.string.loading));
	
			layProgress = (RelativeLayout) findViewById(R.id.layProgress);
			
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setLoadWithOverviewMode(true);
			mWebView.getSettings().setUseWideViewPort(true);
			mWebView.setWebViewClient(new WebViewClient() {
	
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
	
					return true;
				}
	
				@Override
				public void onPageFinished(WebView view, final String url) {
					if(getSupportActionBar() != null) {
						getSupportActionBar().setTitle(view.getTitle());
						getSupportActionBar().setSubtitle(view.getUrl());
					}
					
					originalUrl = view.getUrl();
					
					if(isFirstTime) {
						isFirstTime = false;
						
						layProgress.setVisibility(View.GONE);
					}
				}
			});
			mWebView.setWebChromeClient(new WebChromeClient(){
				public void onProgressChanged(WebView view, int progress) {
	               if(progress < 100 && layProgress.getVisibility() == View.GONE)
	            	   layProgress.setVisibility(View.VISIBLE);
	               else if(progress >= 100 && layProgress.getVisibility() == View.VISIBLE)
	            	   layProgress.setVisibility(View.GONE);
				}
			});
	
			mWebView.loadUrl(originalUrl);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuItem0 = menu.add(R.string.reload);
		menuItem0.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menuItem0.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				mWebView.reload();
		   		
				return true;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(mWebView != null){
	        // Check if the key event was the Back button and if there's history
	        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack())
	        	mWebView.goBack();
	        else if ((keyCode == KeyEvent.KEYCODE_BACK) && !mWebView.canGoBack())
	        	finish();
	        
	        return true;
	    }
	    
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
}