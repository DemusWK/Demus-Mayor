package com.demus.mayor.adapters;

import android.content.Context;

import com.demus.mayor.R;

import android.view.*;
import android.widget.*;

/**
 * This is a customized array adapter class for the news stream list.
 * 
 * @author Lekan Baruwa
 * @email 
 * @version v1.0
 * @since July, 2015
 */
public class CustomSpinnerAdapter2 extends ArrayAdapter<String> {
	int resource;
	Context context;
	
	/**
	 * Default constructor for the adapter 
	 * @param _context
	 * @param _resource
	 * @param _items
	 */
	public CustomSpinnerAdapter2(Context _context, int _resource, String[] _items) {
		super(_context, _resource, _items);
		resource = _resource;
		context = _context;
	}
	
	@Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout resultView;
		
		//Get the current result item to be formatted
		final String item = getItem(position);
		
		if(convertView == null){
			resultView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, resultView, true);
		}
		else
			resultView = (LinearLayout) convertView;
		
		TextView lblMainText = (TextView) resultView.findViewById(R.id.lblMainText);
		lblMainText.setText(item);
		
		return resultView;
	}
}