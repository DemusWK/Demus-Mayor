package com.demus.mayor;

import java.util.ArrayList;

import com.demus.mayor.handlers.UserAccountAPI_Handler;
import com.demus.mayor.models.Bank;
import com.demus.mayor.models.ServerResponse;
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

public class ChangePasswordActivity extends AppCompatActivity {

	private Toolbar toolbar;
	
	static AppCompatActivity current;
	
	String chosenBank;
	 
	static ArrayList<Bank> banks;
	 
	static String[] bankNames;

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

		setContentView(R.layout.activity_change_password);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.change_pass);
		
		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		EditText txtOldPassword = (EditText) findViewById(R.id.txtOldPassword);
		txtOldPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_icon_lit, 0, 0, 0);
				else
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_icon, 0, 0, 0);
			}
		});
		
		EditText txtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
		txtNewPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_icon_lit, 0, 0, 0);
				else
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_icon, 0, 0, 0);
			}
		});
		
		EditText txtConfirmNewPassword = (EditText) findViewById(R.id.txtConfirmNewPassword);
		txtConfirmNewPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_icon_lit, 0, 0, 0);
				else
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_icon, 0, 0, 0);
			}
		});
		
		//recent transaction
		Button cmdSend = (Button) findViewById(R.id.cmdSend);
		cmdSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditText txtOldPassword = (EditText) findViewById(R.id.txtOldPassword);
				EditText txtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
				EditText txtConfirmNewPassword = (EditText) findViewById(R.id.txtConfirmNewPassword);
				
				if(txtOldPassword.getText().toString().equals("") ||
				   txtNewPassword.getText().toString().equals("") ||
				   txtConfirmNewPassword.getText().toString().equals("")) {
					Toast.makeText(current, current.getString(R.string.error_incomplete), Toast.LENGTH_LONG).show();
					return;
				}
				
				if(!txtNewPassword.getText().toString().equals(txtConfirmNewPassword.getText().toString())) {
					txtNewPassword.setError(getString(R.string.error_password_match));
					txtConfirmNewPassword.setError("");
					return;
				}
				
				//change password thread
				RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
				layProgress.setVisibility(View.VISIBLE);
				changePassword(txtNewPassword.getText().toString(), Utility.getUserAccount(ChangePasswordActivity.this).getBio());
			}
		});
	}
	
	private void changePassword(final String newPassword, final String key) {
		new Thread() {
			@Override
			public void run() {
				ServerResponse response = new UserAccountAPI_Handler().changePassword(newPassword, key, ChangePasswordActivity.this, 1);

				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);

				Message message = new Message();
				message.setData(bundle);
				changePasswordHandler.sendMessage(message);
			}
		}.start();
	}
	
	static Handler changePasswordHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
			layProgress.setVisibility(View.GONE);
			
			ServerResponse response = msg.getData().getParcelable("response");
			
			if(response.getResponseCode().equals(Utility.STATUS_OK)) {
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
