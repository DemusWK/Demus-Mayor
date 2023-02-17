package com.demus.mayor;

import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.home.HomeActivity;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.R;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShareStockActivity extends AppCompatActivity {

	EditText txtValue, txtRecipient, txtPin;

	static AppCompatActivity current;

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

		setContentView(R.layout.activity_share_stock);

		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.share_stock);
		
		current = this;

		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		txtRecipient = (EditText) findViewById(R.id.txtRecipient);
		txtRecipient.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.contact_icon_lit, 0, 0, 0);
				else
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.contact_icon, 0, 0, 0);
			}
		});
		
		txtValue = (EditText) findViewById(R.id.txtValue);
		txtValue.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.amount_icon_lit, 0, 0, 0);
				else
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.amount_icon, 0, 0, 0);
			}
		});
		
		txtPin = (EditText) findViewById(R.id.txtPin);
		txtPin.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pin_icon_lit, 0, 0, 0);
				else
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pin_icon, 0, 0, 0);
			}
		});
				
		Button cmdSend = (Button) findViewById(R.id.cmdSend);
		cmdSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
				layProgress.setVisibility(View.VISIBLE);
				
				if (txtValue.getText().toString().equals("") ||
					txtRecipient.getText().toString().equals("") ||
					txtPin.getText().toString().equals("")) {
					layProgress.setVisibility(View.GONE);
					
					//txtValue.setError(getString(R.string.error_incomplete));

					// create alert dialog
					Toast.makeText(getApplicationContext(), R.string.error_incomplete, Toast.LENGTH_LONG).show();

					return;
				}
				else
					shareStock(txtRecipient.getText().toString(), txtValue.getText().toString(), txtPin.getText().toString());
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
	
	private static void shareStock(final String recipient, final String amount, final String pin) {
		new Thread() {
			@Override
			public void run() {
				ServerResponse response = new TransactionAPI_Handler().shareStock(recipient, amount, pin, current, 1);

				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);

				Message message = new Message();
				message.setData(bundle);
				shareStockHandler.sendMessage(message);
			}
		}.start();
	}
	
	static Handler shareStockHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			ServerResponse response = msg.getData().getParcelable("response");
			
			Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();
			
			if(response.getResponseCode().equals(Utility.STATUS_OK)) {
				Intent intent = new Intent(current, HomeActivity.class);
				intent.putExtra("nav_index", 3);
				
				current.startActivity(intent);
				current.finish();
			}
			else {
				RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
				layProgress.setVisibility(View.GONE);
			}
		}
	};
}
