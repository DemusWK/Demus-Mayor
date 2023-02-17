package com.demus.mayor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demus.mayor.R;
import com.demus.mayor.models.BankAccount;

import java.util.ArrayList;

/**
 * This is a customized array adapter class for the stream list.
 * 
 * @author Lekan Baruwa
 * @email 
 * @version v1.0
 * @since July, 2015
 */
public class BankAccountAdapter extends ArrayAdapter<BankAccount> {

	int resource;
	static Context context;

	LinearLayout resultView;

	int pos;

	/**
	 * Default constructor for the adapter
	 * @param _context
	 * @param _resource
	 * @param _items
	 */
	public BankAccountAdapter(Context _context, int _resource, ArrayList<BankAccount> _items) {
		super(_context, _resource, _items);
		
		resource = _resource;
		context = _context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Get the current result item to be formatted
		pos = position;
		final BankAccount item = getItem(position);
		
		if(convertView == null) {
			resultView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, resultView, true);
		}
		else
			resultView = (LinearLayout)convertView;
		
		//bio
		TextView lblTitle = (TextView) resultView.findViewById(R.id.lblTitle);
		lblTitle.setText(item.getBankName());

		TextView lblSubTitle = (TextView) resultView.findViewById(R.id.lblSubTitle);
		lblSubTitle.setText(item.getAccountNumber() + " (" + item.getAccountType() + ")");
						
		return resultView;
	}
}
