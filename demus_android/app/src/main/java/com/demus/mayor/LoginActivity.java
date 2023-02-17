package com.demus.mayor;

import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.handlers.UserAccountAPI_Handler;
import com.demus.mayor.home.HomeActivity;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.models.User;
import com.demus.mayor.utils.Utility;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION_CODES;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LoginActivity extends AppCompatActivity {
	
	static User user;
	
	EditText txtPassword, txtLogin;
	
	static AppCompatActivity current;
	
	static final int PROFILE_PERMISSIONS = 3;
	public static final String PREFS_NAME = "MyMSISDN";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        
        current = this;
        
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M)
       		checkForPermissions();
        
        InitializeUI();
    }
    
    /**
     * This method initializes the User Interface controls.
     */
    private void InitializeUI() {
    	txtLogin = findViewById(R.id.txtLogin);
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    txtLogin.setText(settings.getString("myMSISDN", ""));
    	
    	Button cmdLogin = findViewById(R.id.cmdLogin);
    	cmdLogin.setOnClickListener(v -> {
    	    // Close softKey if opened
    	    Utility.closeSoftKey(current);

			if(txtLogin.getText().toString().equals("") || txtPassword.getText().toString().equals("")) {
				txtLogin.setError(getString(R.string.error_incomplete));

				return;
			}
			else {
				RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
				layProgress.setVisibility(View.VISIBLE);

				SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings1.edit();
				editor.putString("myMSISDN", txtLogin.getText().toString());
				editor.apply();// Commit the edits!

				user = new User();
				user.setEmail(txtLogin.getText().toString());
				user.setPassword(txtPassword.getText().toString());

				//start login
				login(getIntent().getStringExtra("key"));
			}
		});
    	
    	TextView lnkBtnRegister = findViewById(R.id.lnkBtnRegister);
    	lnkBtnRegister.setOnClickListener(v -> {
			Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
			i.putExtra("key", getIntent().getStringExtra("key"));
			   startActivity(i);
			   finish();
		});
    	
    	TextView lnkBtnForgotPass = findViewById(R.id.lnkBtnForgotPass);
    	lnkBtnForgotPass.setOnClickListener(v -> showPhoneOrEmailEntry());
    	
    	txtPassword = findViewById(R.id.txtPassword);
    	
    	CheckBox chkPass = findViewById(R.id.chkPass);
        chkPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (!isChecked) // show password
				txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
			else // hide password
				txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		});
    }
    
    private void checkForPermissions() {		
		int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS);
		int permissionCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
		int permissionCheck3 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
		int permissionCheck4 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
		
		if (permissionCheck != PackageManager.PERMISSION_GRANTED ||
			permissionCheck2 != PackageManager.PERMISSION_GRANTED ||
			permissionCheck3 != PackageManager.PERMISSION_GRANTED ||
			permissionCheck4 != PackageManager.PERMISSION_GRANTED)
			requestPermissions(new String[]{ android.Manifest.permission.GET_ACCOUNTS, 
											 android.Manifest.permission.READ_CONTACTS,
											 android.Manifest.permission.READ_PHONE_STATE,
											 android.Manifest.permission.CALL_PHONE
										   }, PROFILE_PERMISSIONS);
			
	}
    
    private void showPhoneOrEmailEntry() {
    	LayoutInflater li = LayoutInflater.from(current);
		final View promptView = li.inflate(R.layout.prompt_phone_number, null);
				
		AlertDialog alertDialog = new AlertDialog.Builder(current).create();
		alertDialog.setTitle(current.getString(R.string.pwd_recovery));
		alertDialog.setView(promptView);
		alertDialog.setCancelable(true);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, current.getString(R.string.send), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				final String emailOrPhoneNumber = ((EditText) promptView.findViewById(R.id.editTextDialogUserInput)).getText().toString();

				if (!emailOrPhoneNumber.equals("")) {
					RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
					layProgress.setVisibility(View.VISIBLE);
					
					forgotPassword(emailOrPhoneNumber);
				}
				else
					Toast.makeText(current, current.getString(R.string.error_incomplete), Toast.LENGTH_LONG).show();
			}
		});
		alertDialog.show();
	}
    
    private static void showTokenEntry(final String emailOrPhoneNumber) {
    	LayoutInflater li = LayoutInflater.from(current);
		final View promptView = li.inflate(R.layout.prompt_token_entry, null);
				
		AlertDialog alertDialog = new AlertDialog.Builder(current).create();
		alertDialog.setTitle(current.getString(R.string.pwd_recovery));
		alertDialog.setView(promptView);
		alertDialog.setCancelable(true);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, current.getString(R.string.continue_), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				final String token = ((EditText) promptView.findViewById(R.id.editTextDialogUserInput)).getText().toString();

				if (!token.equals("")) {
					LayoutInflater li = LayoutInflater.from(current);
					final View promptView = li.inflate(R.layout.prompt_new_password, null);
							
					AlertDialog alertDialog = new AlertDialog.Builder(current).create();
					alertDialog.setTitle(current.getString(R.string.pwd_recovery));
					alertDialog.setView(promptView);
					alertDialog.setCancelable(true);
					alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, current.getString(R.string.send), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							final String newPassword = ((EditText) promptView.findViewById(R.id.editTextDialogUserInput)).getText().toString();
							final String confirmPassword = ((EditText) promptView.findViewById(R.id.editTextDialogUserInput2)).getText().toString();

							if (newPassword.equals("") || confirmPassword.equals("")) {
								Toast.makeText(current, current.getString(R.string.error_incomplete), Toast.LENGTH_LONG).show();
								
								showTokenEntry(emailOrPhoneNumber);
							}
							else if(!newPassword.equals(confirmPassword)){
								Toast.makeText(current, current.getString(R.string.error_password_match), Toast.LENGTH_LONG).show();
								
								showTokenEntry(emailOrPhoneNumber);
							}
							else {
								RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
								layProgress.setVisibility(View.VISIBLE);
								
								confirmForgotPassowrdToken(token, emailOrPhoneNumber, confirmPassword);
							}
						}
					});
					alertDialog.show();
				}
				else {
					Toast.makeText(current, current.getString(R.string.token_invalid), Toast.LENGTH_LONG).show();
					
					showTokenEntry(emailOrPhoneNumber);
				}
			}
		});
		alertDialog.show();
	}
    
    static void login(final String key) {
		RelativeLayout layProgress = current.findViewById(R.id.layProgress);
		if(layProgress != null) layProgress.setVisibility(View.VISIBLE);

		new Thread() {
			public void run() {
				ServerResponse response = new UserAccountAPI_Handler().login(user.getEmail(), user.getPassword(), key, current, 1);
				
				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);
				
				Message message = new Message();
				message.setData(bundle);
				loginHandler.sendMessage(message);
			}
		}.start();
	}
    
    static Handler loginHandler = new Handler() {
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);
        	
        	ServerResponse response = msg.getData().getParcelable("response");
        	
        	if (response.getResponseCode().equals(Utility.STATUS_OK))
				getBanks();
			else {
				RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
				layProgress.setVisibility(View.GONE);
				
				Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_SHORT).show();
			}
        }
    };
    
    static void getBanks() {
		new Thread() {
			@Override
			public void run() {
				new TransactionAPI_Handler().getBanks(current, 1);
				
				Message message = new Message();
				bankHandler.sendMessage(message);
			}
		}.start();
	}
	
	static Handler bankHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
			layProgress.setVisibility(View.GONE);
			
			Intent i = new Intent(current, HomeActivity.class);
		   	current.startActivity(i);
		   	current.finish();
		}
	};
	
	static void forgotPassword(final String emailOrPhoneNumber) {
		new Thread() {
			public void run() {
				ServerResponse response = new UserAccountAPI_Handler().forgotPassword(emailOrPhoneNumber, current, 1);
				
				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);
				bundle.putString("emailOrPhoneNumber", emailOrPhoneNumber);
				
				Message message = new Message();
				message.setData(bundle);
				forgotPasswordHandler.sendMessage(message);
			}
		}.start();
	}
    
    static Handler forgotPasswordHandler = new Handler() {
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);
        	
        	ServerResponse response = msg.getData().getParcelable("response");
        	String emailOrPhoneNumber = msg.getData().getString("emailOrPhoneNumber");
        	
        	RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
			layProgress.setVisibility(View.GONE);
        	
        	if (response.getResponseCode().equals(Utility.STATUS_OK))
        		showTokenEntry(emailOrPhoneNumber);
			else
				Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    
    static void confirmForgotPassowrdToken(final String token, final String emailOrPhoneNumber, final String newPassword) {
		new Thread() {
			public void run() {
				ServerResponse response = new UserAccountAPI_Handler().confirmForgotPassword(token, emailOrPhoneNumber, newPassword, current, 1);
				
				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);
				
				Message message = new Message();
				message.setData(bundle);
				confirmForgotPassowrdTokenHandler.sendMessage(message);
			}
		}.start();
	}
    
    static Handler confirmForgotPassowrdTokenHandler = new Handler() {
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);
        	
        	ServerResponse response = msg.getData().getParcelable("response");
        	
        	RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
			layProgress.setVisibility(View.GONE);
			
			Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
        	case PROFILE_PERMISSIONS: {
        		if(grantResults.length >= 4) {
	        		if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
	        			grantResults[1] != PackageManager.PERMISSION_GRANTED ||
	        			grantResults[2] != PackageManager.PERMISSION_GRANTED ||
	        			grantResults[3] != PackageManager.PERMISSION_GRANTED) 
	        			Toast.makeText(current, getString(R.string.permissions_err), Toast.LENGTH_LONG).show();
        		}
        		
                return;
        	}
        }
    }
}
