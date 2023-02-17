package com.demus.mayor;

import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.models.ListObject;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.R;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ConfirmTransactionActivity extends AppCompatActivity {

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

		setContentView(R.layout.activity_confirm_transaction);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		// setup header
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.transaction);
		
		current = this;
		
		initializeUI();
	}

	/**
	 * This method initializes the User Interface controls.
	 */
	private void initializeUI() {
		Button cmdSend = (Button) findViewById(R.id.cmdSend);
		cmdSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditText txtTransactionCode = (EditText) findViewById(R.id.txtTransactionCode);
				
				if(txtTransactionCode.getText().toString().equals(""))
					txtTransactionCode.setError(getString(R.string.tranx_code_err));
				else {
					RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
					layProgress.setVisibility(View.VISIBLE);
					
					checkTranx(txtTransactionCode.getText().toString());
				}
			}
		});
	}
	
	static void checkTranx(final String tranxCode) {
		new Thread() {
			@Override
			public void run() {
				ListObject tranx = new TransactionAPI_Handler().getTransaction(tranxCode, current, 1);

				Bundle bundle = new Bundle();
				bundle.putParcelable("tranx", tranx);

				Message message = new Message();
				message.setData(bundle);
				checkTranxHandler.sendMessage(message);
			}
		}.start();
	}
	
	static Handler checkTranxHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
			layProgress.setVisibility(View.GONE);
			
			ListObject tranx = msg.getData().getParcelable("tranx");
			
			if(tranx != null) {
				Intent intent = new Intent(current, TransactionFullViewActivity.class);
				intent.putExtra("item", tranx);
				intent.putExtra("hideBalance", !tranx.getImageUrl().equals(Utility.getUserAccount(current).getPhoneNumber()));
				current.startActivity(intent);
				current.finish();
			}
			else
				Toast.makeText(current, current.getString(R.string.tranx_not_found), Toast.LENGTH_LONG).show();
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
