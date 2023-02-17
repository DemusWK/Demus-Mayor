package com.demus.mayor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demus.mayor.R;

public class TermsOfServiceActivity extends AppCompatActivity {

	private Toolbar toolbar;
	
	CheckBox chkTerms;
	 
	boolean isChecked = false;
	
	static AppCompatActivity current;
	
	public static final String PREFS_NAME = "Notification_Settings";

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

		setContentView(R.layout.activity_terms_conditions);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.terms_conditions);
		
		current = this;
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    boolean notified = settings.getBoolean("notified", false);
	    
	    if(!notified)
	    	initializeUI();
	    else {
	    	Intent intent = new Intent(TermsOfServiceActivity.this, LoginActivity.class);
	    	intent.putExtra("key", getIntent().getStringExtra("key"));
			startActivity(intent);
			finish();
	    }
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		TextView txtTOS = (TextView) findViewById(R.id.txtTOS);
		txtTOS.setText(Html.fromHtml(getString(R.string.tos)));
		
		chkTerms = (CheckBox) findViewById(R.id.chkTerms);
		
		Button cmdAccept = (Button) findViewById(R.id.cmdAccept);
		cmdAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
				layProgress.setVisibility(View.VISIBLE);
				
				if (!chkTerms.isChecked()) {
					layProgress.setVisibility(View.GONE);
					
					chkTerms.setError("");
					
					Toast.makeText(getApplicationContext(), R.string.terms_agree_error, Toast.LENGTH_LONG).show();
					
					return;
				}
				else {
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		        	
		    	    boolean notified = settings.getBoolean("notified", false);
	    			if(!notified) {
	    				SharedPreferences.Editor editor = settings.edit();
	    			    editor.putBoolean("notified", true);
	    			    
	    			    // Commit the edits!
	    			    editor.commit();
	    			}
	    			
					Intent intent = new Intent(TermsOfServiceActivity.this, LoginActivity.class);
					intent.putExtra("key", getIntent().getStringExtra("key"));
					startActivity(intent);
					finish();
				}
			}
		});
	}
	
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
