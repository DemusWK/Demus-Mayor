package com.demus.mayor;

import com.demus.mayor.models.ListObject;
import com.demus.mayor.utils.Utility;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InlinedApi")
public class InfoFullViewActivity extends AppCompatActivity {

	private Toolbar toolbar;
	
	static ListObject info;
	
	static AppCompatActivity current;

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
		
		setContentView(R.layout.activity_info_fullview);
		
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.message));

		current = this;
		
		initializeUI();
	}
	
	private void initializeUI() {
		info = getIntent().getParcelableExtra("info");
		
		TextView lblTitle = (TextView) findViewById(R.id.lblTitle);
		TextView lblExcerpt = (TextView) findViewById(R.id.lblExcerpt);
		TextView lblTime = (TextView) findViewById(R.id.lblTime);
		
		lblTitle.setText(Html.fromHtml(info.getTitle()));
		lblTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InfoFullViewActivity.this, MainBrowserActivity.class);
				intent.putExtra("originalUrl", info.getBalanceAfter());
				startActivity(intent);
			}
		});

		lblExcerpt.setText(Html.fromHtml(info.getSubTitle()));
		lblTime.setText(Utility.getPrettyDate(Long.parseLong(info.getExtraString3())));
		
		ImageView imgNews = (ImageView) findViewById(R.id.imgNews);
		imgNews.setFocusable(false);
		imgNews.setFocusableInTouchMode(false);
		imgNews.setClickable(true);
		imgNews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent i = new Intent(InfoFullViewActivity.this, ImageViewerActivity.class);
				i.putExtra("url", Utility.SERVER_URL + info.getExtraString());
				startActivity(i);*/
				Intent intent = new Intent(InfoFullViewActivity.this, MainBrowserActivity.class);
				intent.putExtra("originalUrl", info.getBalanceAfter());
				startActivity(intent);
			}
		});
		
		if(info.getImageUrl() != null) {
			DemusMayorApplication.imageLoader.DisplayImage(Utility.SERVER_URL + info.getImageUrl().replace(" ", "%20"), imgNews);
			imgNews.setVisibility(View.VISIBLE);
		}
		else
			imgNews.setVisibility(View.GONE);
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
