package com.demus.mayor;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demus.mayor.R;

public class ContactActivity extends AppCompatActivity {

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

		setContentView(R.layout.activity_contact);

		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		TextView toolTitle = toolbar.findViewById(R.id.toolbar_title);
		toolTitle.setText(R.string.contact_pure);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		LinearLayout layMail = (LinearLayout) findViewById(R.id.layMail);
		layMail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getString(R.string.contact_email), null));
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ getString(R.string.contact_email) });
				startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
			}
		});
		
		LinearLayout layPhoneCall = (LinearLayout) findViewById(R.id.layPhoneCall);
		layPhoneCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(ContactActivity.this).create();
				alertDialog.setMessage(getString(R.string.call_confirm));
    			
    			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
    		        public void onClick(DialogInterface dialog2, int which2) {
    		        	dialog2.dismiss();
    		        }
    		    });
    			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
    		        public void onClick(DialogInterface dialog2, int which2) {
    		        	startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Uri.encode(getString(R.string.contact_phone)))));
    	    			
    		        	dialog2.dismiss();
    		        }
    		    });
    			
    			alertDialog.show();
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
