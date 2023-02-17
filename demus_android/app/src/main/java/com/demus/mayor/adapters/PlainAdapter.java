package com.demus.mayor.adapters;

import android.content.Context;

import java.util.ArrayList;

import com.demus.mayor.R;
import com.demus.mayor.models.NetObject;

import android.view.*;
import android.widget.*;

/**
 * This is a customized array adapter class for the stream list.
 * 
 * @author Lekan Baruwa
 * @email 
 * @version v1.0
 * @since July, 2015
 */
public class PlainAdapter extends ArrayAdapter<NetObject> {
	
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
	public PlainAdapter(Context _context, int _resource, ArrayList<NetObject> _items) {
		super(_context, _resource, _items);
		
		resource = _resource;
		context = _context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Get the current result item to be formatted
		pos = position;
		final NetObject item = getItem(position);
		
		if(convertView == null) {
			resultView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, resultView, true);
		}
		else
			resultView = (LinearLayout)convertView;
		
		//bio
		TextView lblText = (TextView) resultView.findViewById(R.id.lblMainText);
		
		lblText.setText(item.getText());
						
		return resultView;
	}
}
