package com.demus.mayor;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.demus.mayor.adapters.CustomSpinnerAdapter;
import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.handlers.UserAccountAPI_Handler;
import com.demus.mayor.home.HomeActivity;
import com.demus.mayor.models.NetObject;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.models.User;
import com.demus.mayor.utils.Utility;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("NewApi")
public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	EditText txtPassword, txtPhone, txtFName, txtLName, txtEmail, txtPin, txtConfirmPin;

	String chosenNetwork;

	String[] newtworkNames;

	private Toolbar toolbar;

	static AppCompatActivity current;

	static User user;

	static final int PROFILE_PERMISSIONS = 3;

	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();

			// clear FLAG_TRANSLUCENT_STATUS flag:
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

			// finally change the color
			window.setStatusBarColor(getResources().getColor(R.color.ColorPrimaryDark));
		}

		setContentView(R.layout.activity_register);

		toolbar = findViewById(R.id.tool_bar);
		if (toolbar != null) {
			TextView titleView = toolbar.findViewById(R.id.toolbar_title);
			titleView.setText(getText(R.string.register));
		}
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("");

		current = this;

		if (Build.VERSION.SDK_INT >= VERSION_CODES.M)
			checkForPermissions();
		else
			getLoaderManager().initLoader(0, null, this);

		InitializeUI();
	}

	private void checkForPermissions() {
		int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS);
		int permissionCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
		int permissionCheck3 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
		int permissionCheck4 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);

		if (permissionCheck == PackageManager.PERMISSION_GRANTED &&
			permissionCheck2 == PackageManager.PERMISSION_GRANTED &&
			permissionCheck3 == PackageManager.PERMISSION_GRANTED &&
			permissionCheck4 == PackageManager.PERMISSION_GRANTED)
			getLoaderManager().initLoader(0, null, this);
		else
			requestPermissions(new String[]{ android.Manifest.permission.GET_ACCOUNTS,
                    						 android.Manifest.permission.READ_CONTACTS,
                    						 android.Manifest.permission.READ_PHONE_STATE,
                    						 android.Manifest.permission.CALL_PHONE
                    					   }, PROFILE_PERMISSIONS);
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void InitializeUI() {
		txtPhone = findViewById(R.id.txtPhon);

		newtworkNames = getResources().getStringArray(R.array.mobile_neworks);

		ArrayList<NetObject> netObjects = new ArrayList<NetObject>();
		NetObject netObject = new NetObject();
		netObject.setId(1);
		netObject.setText("MTN");
		netObject.setImgResource(R.drawable.mtn);
		netObjects.add(netObject);

		netObject = new NetObject();
		netObject.setId(2);
		netObject.setText("AIRTEL");
		netObject.setImgResource(R.drawable.airtel);
		netObjects.add(netObject);

		netObject = new NetObject();
		netObject.setId(3);
		netObject.setText("9 MOBILE");
		netObject.setImgResource(R.drawable.ninemobile);
		netObjects.add(netObject);

		netObject = new NetObject();
		netObject.setId(4);
		netObject.setText("GLO");
		netObject.setImgResource(R.drawable.glo);
		netObjects.add(netObject);

		/*netObject = new NetObject();
		netObject.setId(5);
		netObject.setText("VISAFONE");
		netObject.setImgResource(R.drawable.visafone);
		netObjects.add(netObject);*/

		Spinner spinNetwork = findViewById(R.id.spinNetwork);
		CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(current, R.layout.custom_spinner_normal_state, netObjects);
		spinNetwork.setAdapter(adapter);
		spinNetwork.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
				chosenNetwork = ((NetObject) parent.getItemAtPosition(pos)).getText();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		Button cmdRegister = findViewById(R.id.home_recharge_btn_top);
		cmdRegister.setOnClickListener(v -> {
			RelativeLayout layProgress = findViewById(R.id.layProgress);
			layProgress.setVisibility(View.VISIBLE);
			Utility.closeSoftKey(current);

			if (txtEmail.getText().toString().equals("") ||
				txtFName.getText().toString().equals("") ||
				txtLName.getText().toString().equals("") ||
				txtPassword.getText().toString().equals("") ||
				txtPin.getText().toString().equals("") ||
				txtConfirmPin.getText().toString().equals("") ||
				chosenNetwork.equals("")) {
				layProgress.setVisibility(View.GONE);

				// create alert dialog
				Toast.makeText(getApplicationContext(),R.string.error_incomplete, Toast.LENGTH_LONG).show();

				return;
			}

			if (!validateEmail(txtEmail.getText().toString())) {
				layProgress.setVisibility(View.GONE);

				txtEmail.setError(getString(R.string.invalid_email));

				return;
			}

			if (!validateName(txtFName.getText().toString())) {
				layProgress.setVisibility(View.GONE);

				txtFName.setError(getString(R.string.invalid_name));

				return;
			}

			if (!validateName(txtLName.getText().toString())) {
				layProgress.setVisibility(View.GONE);

				txtLName.setError(getString(R.string.invalid_name));

				return;
			}

			if(!txtPin.getText().toString().equals(txtConfirmPin.getText().toString())) {
				layProgress.setVisibility(View.GONE);

				txtPin.setError(getString(R.string.error_pin_match));
				txtConfirmPin.setError(getString(R.string.error_pin_match));

				return;
			}
			else {
				if(chosenNetwork.equals("9 MOBILE"))
					chosenNetwork = "ETISALAT";

				user = new User();
				user.setUserID(Utility.getDeviceID(RegisterActivity.this));
				user.setCountryCode("NGN");
				user.setMobileNetwork(chosenNetwork);
				user.setPhoneNumber(txtPhone.getText().toString());
				user.setFirstName(txtFName.getText().toString());
				user.setLastName(txtLName.getText().toString());
				user.setEmail(txtEmail.getText().toString());
				user.setPassword(txtPassword.getText().toString());
				user.setPin(Integer.parseInt(txtPin.getText().toString()));

				//start registration
				register(getIntent().getStringExtra("key"));
			}
		});

		txtFName = findViewById(R.id.txtFName);
		txtLName = findViewById(R.id.txtLName);
		txtEmail = findViewById(R.id.txtEmail);
		txtPassword = findViewById(R.id.txtPassword);
		txtPin = findViewById(R.id.txtPin);
		txtConfirmPin = findViewById(R.id.txtConfirmPin);

		CheckBox chkPass = findViewById(R.id.chkPass);
        chkPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (!isChecked) // show password
				txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
			else // hide password
				txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		});

        getDeviceInfo();
	}

	private boolean validateEmail(String email) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}

	private boolean validateName(String name) {
		String regExpn = "^([A-Za-z][A-Za-z ,.'`-]{1,})$";

		CharSequence inputStr = name;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
			startActivity(i);
			finish();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
		startActivity(i);
		finish();
	}

	static void register(final String key) {
		new Thread() {
			public void run() {
				ServerResponse response = new UserAccountAPI_Handler().registerUser(user, key, current, 1);

				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);

				Message message = new Message();
				message.setData(bundle);
				registrationHandler.sendMessage(message);
			}
		}.start();
	}

    static Handler registrationHandler = new Handler() {
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);

        	ServerResponse response = msg.getData().getParcelable("response");

        	//close progress
        	if (response.getResponseCode().equals(Utility.STATUS_OK))
				getBanks();
			else {
				RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
				layProgress.setVisibility(View.GONE);

				Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();
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

	public void getDeviceInfo() {
    	Cursor c = null;

    	try {
	        //Get email
	        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
	    	Account[] accounts = AccountManager.get(RegisterActivity.this).getAccounts();

	    	for (Account account : accounts) {
		    	if (emailPattern.matcher(account.name).matches()) {
			        String possibleEmail = account.name;

			        if(validateEmail(possibleEmail)) {
				        txtEmail.setText(possibleEmail);
				        break;
			        }
			    }
	    	}
    	}
    	catch(Exception ex) {

    	}
    	finally {
    		if(c != null)
    			c.close();
    	}
	}

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    	try {
    		return new CursorLoader(this,
	                // Retrieve data rows for the device user's 'profile' contact.
	                Uri.withAppendedPath(
	                        ContactsContract.Profile.CONTENT_URI,
	                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
	                ProfileQuery.PROJECTION,

	                // Select only  name.
	                ContactsContract.Contacts.Data.MIMETYPE + "=?",
	                new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE},

	                // Show primary fields first. Note that there won't be
	                // a primary fields if the user hasn't specified one.
	                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
		}
    	catch (Exception e) {

		}

    	return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    	if(cursor != null) {
	        //move cursor
	    	cursor.moveToFirst();

	        String mimeType;

	        try {
	        	while (!cursor.isAfterLast()) {
	                mimeType = cursor.getString(ProfileQuery.MIME_TYPE);
	                switch (mimeType) {

	                    case ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE:
	                        String fname = cursor.getString(ProfileQuery.GIVEN_NAME),
	                        	   lname = cursor.getString(ProfileQuery.FAMILY_NAME);

	                        if(validateName(fname))
	                    		txtFName.setText(fname);

	                    	if(validateName(lname))
	                    		txtLName.setText(lname);

	                        break;
	                }
	                cursor.moveToNext();
	            }
			}
	        catch (Exception e) {

			}
    	}
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.Contacts.Data.MIMETYPE
        };

        /**
         * Column index for the family name in the profile query results
         */
        int FAMILY_NAME = 0;
        /**
         * Column index for the given name in the profile query results
         */
        int GIVEN_NAME = 1;
        /**
         * Column index for the MIME type in the profile query results
         */
        int MIME_TYPE = 2;
    }

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
