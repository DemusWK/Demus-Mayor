package com.demus.mayor;

import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import android.os.Bundle;
import android.widget.TextView;

import com.demus.mayor.R;

public class ShowMessagePromptActivity extends AppCompatActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// setup header
		setContentView(R.layout.prompt_important_message);
		
		InitializeUI();
	}
	
	public void InitializeUI() {
		TextView lblMessage = (TextView) findViewById(R.id.lblMessage);
		lblMessage.setText(getIntent().getStringExtra("msg"));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
