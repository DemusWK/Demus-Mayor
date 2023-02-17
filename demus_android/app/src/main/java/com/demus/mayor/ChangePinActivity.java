package com.demus.mayor;

import com.demus.mayor.handlers.UserAccountAPI_Handler;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.models.User;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.R;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class ChangePinActivity extends AppCompatActivity {

	private Toolbar toolbar;
	
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

		setContentView(R.layout.activity_change_pin);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.change_pin);
		
		current = this;

		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		EditText txtOldPin = (EditText) findViewById(R.id.txtOldPin);
		txtOldPin.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pin_icon_lit, 0, 0, 0);
				else
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pin_icon, 0, 0, 0);
			}
		});
		
		EditText txtNewPin = (EditText) findViewById(R.id.txtNewPin);
		txtNewPin.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pin_icon_lit, 0, 0, 0);
				else
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pin_icon, 0, 0, 0);
			}
		});
		
		EditText txtConfirmPin = (EditText) findViewById(R.id.txtConfirmNewPin);
		txtConfirmPin.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pin_icon_lit, 0, 0, 0);
				else
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pin_icon, 0, 0, 0);
			}
		});
		
		//change pin
		Button cmdSend = (Button) findViewById(R.id.cmdSend);
		cmdSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditText txtOldPin = (EditText) findViewById(R.id.txtOldPin);
				EditText txtNewPin = (EditText) findViewById(R.id.txtNewPin);
				EditText txtConfirmPin = (EditText) findViewById(R.id.txtConfirmNewPin);
				
				if(txtOldPin.getText().toString().equals("") ||
				   txtNewPin.getText().toString().equals("") ||
				   txtConfirmPin.getText().toString().equals("")) {
					Toast.makeText(current, current.getString(R.string.error_incomplete), Toast.LENGTH_LONG).show();
					return;
				}
				
				if(txtNewPin.getText().toString().length() < 4) {
					txtNewPin.setError(getString(R.string.error_pin_short));
					return;
				}
				
				if(!txtNewPin.getText().toString().equals(txtConfirmPin.getText().toString())) {
					txtNewPin.setError(getString(R.string.error_pin_match));
					txtConfirmPin.setError(getString(R.string.error_pin_match));
					return;
				}
				
				//change pin thread
				RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
				layProgress.setVisibility(View.VISIBLE);
				changePin(txtOldPin.getText().toString(), txtNewPin.getText().toString());
			}
		});
	}
	
	private void changePin(final String oldPin, final String newPin) {
		new Thread() {
			@Override
			public void run() {
				ServerResponse response = new UserAccountAPI_Handler().changePin(oldPin, newPin, ChangePinActivity.this, 1);

				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);
				bundle.putString("newPin", newPin);

				Message message = new Message();
				message.setData(bundle);
				changePinHandler.sendMessage(message);
			}
		}.start();
	}
	
	static Handler changePinHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
			layProgress.setVisibility(View.GONE);
			
			ServerResponse response = msg.getData().getParcelable("response");
			String newPin = msg.getData().getString("newPin");
			
			if(response.getResponseCode().equals(Utility.STATUS_OK)) {
				User user = Utility.getUserAccount(current);
				user.setPin(Integer.parseInt(newPin));
				user.update(current, user.getUserID());
				
				Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();
				current.finish();
			}
			else
				Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();
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
}
