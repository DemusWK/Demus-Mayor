package com.demus.mayor;

import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.home.HomeActivity;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

@SuppressLint("InlinedApi")
public class MainBrowserActivity extends AppCompatActivity {

	private Toolbar toolbar;
	
	WebView mWebView;
	
	String originalUrl;
	
	boolean isFirstTime = true;
	
	static AppCompatActivity current;
	
	static RelativeLayout layProgress;
	
	static boolean isAlreadyCalled = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		
		setContentView(R.layout.activity_qt_pay_browser);
		
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		
		originalUrl = getIntent().getStringExtra("originalUrl");
		
		current = this;
		
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.quick_teller);
		getSupportActionBar().setSubtitle(originalUrl);
		
		initializeUI();
	}
	
	private void initializeUI() {
		layProgress = (RelativeLayout) findViewById(R.id.layProgress);
		
		mWebView = (WebView) findViewById(R.id.webView1);
		mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

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
	public void onBackPressed() {
		finish();
	}
}
