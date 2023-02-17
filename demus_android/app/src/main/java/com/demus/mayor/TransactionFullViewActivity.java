package com.demus.mayor;

import java.text.DecimalFormat;

import com.demus.mayor.models.ListObject;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.R;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionFullViewActivity extends AppCompatActivity {

	private Toolbar toolbar;
	
	ListObject item;

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

		setContentView(R.layout.activity_transaction_fullview);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.transaction);
		
		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		item = getIntent().getParcelableExtra("item");
		
		if(item == null)
			finish();
		
		TextView lblTransactionID = (TextView) findViewById(R.id.lblTransactionID);
		lblTransactionID.setText(item.getSubTitle());
		lblTransactionID.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("Copied Text", item.getSubTitle());
				clipboard.setPrimaryClip(clip);
				
				Toast.makeText(TransactionFullViewActivity.this, getString(R.string.tranx_id_copied), Toast.LENGTH_LONG).show();
			}
		});
		
		TextView lblDescription = (TextView) findViewById(R.id.lblDescription);
		lblDescription.setText(item.getExtraString2());
		
		TextView lblDate = (TextView) findViewById(R.id.lblDate);
		lblDate.setText(Utility.getPrettyDate(Long.parseLong(item.getExtraString3())));
		
		TextView lblStatus = (TextView) findViewById(R.id.lblStatus);
		lblStatus.setText(item.getExtraString());
		
		TextView lblBalance = (TextView) findViewById(R.id.lblBalance);
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		double balance = Double.parseDouble(item.getBalanceAfter());
		if(balance > 0.0)
			lblBalance.setText(getString(R.string.balance) + ": " + getString(R.string.naira_sign) + formatter.format(balance));
		else
			lblBalance.setText(getString(R.string.balance) + ": " + getString(R.string.naira_sign) + "0.00");
		
		if(getIntent().getBooleanExtra("hideBalance", false))
			lblBalance.setVisibility(View.GONE);
		
		Button cmdOk = (Button) findViewById(R.id.cmdOk);
		cmdOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
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
}
