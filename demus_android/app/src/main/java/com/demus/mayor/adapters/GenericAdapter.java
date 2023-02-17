package com.demus.mayor.adapters;

import android.content.Context;

import java.util.ArrayList;

import com.demus.mayor.R;
import com.demus.mayor.models.ListObject;

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
public class GenericAdapter extends ArrayAdapter<ListObject> {
	
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
	public GenericAdapter(Context _context, int _resource, ArrayList<ListObject> _items) {
		super(_context, _resource, _items);
		
		resource = _resource;
		context = _context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Get the current result item to be formatted
		pos = position;
		final ListObject item = getItem(position);
		
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
		TextView lblSubTitle = (TextView) resultView.findViewById(R.id.lblSubTitle);
		
		lblTitle.setText(item.getTitle());
		lblSubTitle.setText(item.getSubTitle());
						
		return resultView;
	}
}
