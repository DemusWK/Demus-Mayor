package com.demus.mayor;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.models.BankAccount;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.R;

public class PrimaryBankAccountActivity extends AppCompatActivity {

	private Toolbar toolbar;
	
	static AppCompatActivity current;

	BankAccount bankAccount;

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

		setContentView(R.layout.activity_add_bank_account);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.account);
		
		current = this;

		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
	    bankAccount = getIntent().getParcelableExtra("item");

	    if(bankAccount == null) {
            finish();

            return;
        }
	    else {
            ((TextView) findViewById(R.id.lblBankName)).setText(bankAccount.getBankName());
            ((TextView) findViewById(R.id.lblAccountType)).setText(bankAccount.getAccountType());
            ((TextView) findViewById(R.id.lblAccountNumber)).setText(bankAccount.getAccountNumber());

            if(bankAccount.getIsPrimary())
                ((TextView) findViewById(R.id.lblIsPrimary)).setText(getString(R.string.yes));
            else
                ((TextView) findViewById(R.id.lblIsPrimary)).setText(getString(R.string.no));
        }

		//change pin
		Button cmdSend = (Button) findViewById(R.id.cmdSend);
		cmdSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditText txtPin = (EditText) findViewById(R.id.txtPin);

				if(txtPin.getText().toString().length() < 4) {
					txtPin.setError(getString(R.string.error_pin_short));
					return;
				}
				else {
					//change pin thread
					RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
					layProgress.setVisibility(View.VISIBLE);

					doPrimaryBankAccountUpdate(bankAccount, txtPin.getText().toString());
				}
			}
		});

        if(bankAccount.getIsPrimary()) {
            cmdSend.setVisibility(View.GONE);

            EditText txtPin = (EditText) findViewById(R.id.txtPin);
            txtPin.setVisibility(View.GONE);
        }
	}
	
	private void doPrimaryBankAccountUpdate(final BankAccount bankAccount, final String pin) {
		new Thread() {
			@Override
			public void run() {
				ServerResponse response = new TransactionAPI_Handler().primaryBankAccountUpdate(bankAccount, pin, PrimaryBankAccountActivity.this, 1);
				
				Bundle bundle = new Bundle();
				bundle.putParcelable("response", response);

				Message message = new Message();
				message.setData(bundle);
				primaryBankAccountHandler.sendMessage(message);
			}
		}.start();
	}
	
	static Handler primaryBankAccountHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
			layProgress.setVisibility(View.GONE);
			
			final ServerResponse response = msg.getData().getParcelable("response");

            if(response.getResponseCode().equals(Utility.STATUS_OK))
                Toast.makeText(current, current.getString(R.string.primary_acct_success), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(current, current.getString(R.string.general_error), Toast.LENGTH_LONG).show();

            current.finish();
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
