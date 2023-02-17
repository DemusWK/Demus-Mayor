package com.demus.mayor;

import com.demus.mayor.handlers.UserAccountAPI_Handler;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.models.User;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.R;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PinResetActivity extends AppCompatActivity {

	User user;

	EditText txtPassword;

	private Toolbar toolbar;
	
	static AppCompatActivity current;
	
	static boolean isFirstTime;

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

		setContentView(R.layout.activity_reset_pin);

		toolbar = findViewById(R.id.tool_bar);
		if (toolbar != null) {
			TextView titleView = toolbar.findViewById(R.id.toolbar_title);
			titleView.setText(getText(R.string.reset_pin));
		}
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		current = this;
		
		isFirstTime = getIntent().getBooleanExtra("isFirstTime", false);

		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		txtPassword = findViewById(R.id.txtPassword);

		
		CheckBox chkPass = findViewById(R.id.chkPass);
        chkPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (!isChecked) // show password
				txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
			else // hide password
				txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		});
		
		Button cmdSend = findViewById(R.id.cmdSend);
		cmdSend.setOnClickListener(v -> {
			RelativeLayout layProgress = findViewById(R.id.layProgress);
			layProgress.setVisibility(View.VISIBLE);

			Utility.closeSoftKey(current);

			if (txtPassword.getText().toString().equals("")) {
				layProgress.setVisibility(View.GONE);

				// create alert dialog
				Toast.makeText(getApplicationContext(), R.string.error_incomplete, Toast.LENGTH_LONG).show();

				return;
			}
			else {
				if(!txtPassword.getText().toString().equals(Utility.getUserAccount(getApplicationContext()).getPassword()))
					Toast.makeText(getApplicationContext(), R.string.incorrect_password, Toast.LENGTH_LONG).show();
				else
					//start pin reset
					resetPin();
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
				if(!isFirstTime)
					finish();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		if(!isFirstTime)
			finish();
	}
	
	static void resetPin() {
		new Thread() {
			public void run() {
				ServerResponse response = new UserAccountAPI_Handler().resetPin(current, 1);
				
				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);
				
				Message message = new Message();
				message.setData(bundle);
				resetPinHandler.sendMessage(message);
			}
		}.start();
	}
    
    static Handler resetPinHandler = new Handler() {
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);
        	
        	ServerResponse response = msg.getData().getParcelable("response");
        	
        	//close progress			
        	if (response.getResponseCode().equals(Utility.STATUS_OK)) {
        		Toast.makeText(current, current.getString(R.string.pin_reset_suc), Toast.LENGTH_LONG).show();
        		
        		Intent intent = new Intent(current, ChangePinActivity.class);
        		current.startActivity(intent);
				current.finish();
        	}
			else {
				RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
				layProgress.setVisibility(View.GONE);
				
				if(isFirstTime)
					Toast.makeText(current, current.getString(R.string.pin_reset_ft_error), Toast.LENGTH_LONG).show();
				else
					Toast.makeText(current, current.getString(R.string.general_fail), Toast.LENGTH_LONG).show();
			}
        }
    };
}
