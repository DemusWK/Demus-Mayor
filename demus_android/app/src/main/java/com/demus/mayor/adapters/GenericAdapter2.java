package com.demus.mayor.adapters;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import com.demus.mayor.BrowserActivity;
import com.demus.mayor.DemusMayorApplication;
import com.demus.mayor.InfoFullViewActivity;
import com.demus.mayor.R;
import com.demus.mayor.TransactionFullViewActivity;
import com.demus.mayor.models.ListObject;
import com.demus.mayor.utils.Utility;

import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * This is a customized array adapter class for the stream list.
 * 
 * @author Lekan Baruwa
 * @email 
 * @version v1.0
 * @since July, 2015
 */
public class GenericAdapter2 extends ArrayAdapter<ListObject> {
	
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
	public GenericAdapter2(Context _context, int _resource, ArrayList<ListObject> _items) {
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
		
		resultView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(item.getIsNotif() == 0) {
					Intent i = new Intent(context, TransactionFullViewActivity.class);
					i.putExtra("item", item);
					context.startActivity(i);
				}
				else {
					Intent i = new Intent(context, InfoFullViewActivity.class);
					i.putExtra("info", item);
					context.startActivity(i);
				}
			}
		});
		
		//bio
		TextView lblTitle = (TextView) resultView.findViewById(R.id.lblTitle);
		TextView lblSubTitle = (TextView) resultView.findViewById(R.id.lblSubTitle);
		TextView lblTime = (TextView) resultView.findViewById(R.id.lblTime);
		TextView lblReadMore = (TextView) resultView.findViewById(R.id.lblReadMore);
		ImageView imgNotif = (ImageView) resultView.findViewById(R.id.imgNotif);
		
		lblTime.setText(Utility.getPrettyDate(Long.parseLong(item.getExtraString3())));
		
		if(item.getIsNotif() == 0) {
			String title = item.getTitle();
			
			if(title.contains("ETISALAT"))
				lblTitle.setText(title.replace("ETISALAT", "9MOBILE"));
			else
				lblTitle.setText(title);
			
			lblSubTitle.setText(item.getSubTitle() + " (" + item.getExtraString() + ")");
			
			lblReadMore.setVisibility(View.GONE);
			
			imgNotif.setVisibility(View.GONE);
		}
		else {
			lblTitle.setText(item.getSubTitle());
			lblSubTitle.setText(item.getTitle());
			
			lblReadMore.setVisibility(View.VISIBLE);
			lblReadMore.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(context, InfoFullViewActivity.class);
					i.putExtra("info", item);
					context.startActivity(i);
				}
			});
			
			if(item.getImageUrl().equals(""))
				imgNotif.setVisibility(View.GONE);
			else {
				imgNotif.setVisibility(View.VISIBLE);
				
				DemusMayorApplication.imageLoader.DisplayImage(Utility.SERVER_URL + item.getImageUrl().replace(" ", "%20"), imgNotif);
				
				imgNotif.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(context, BrowserActivity.class);
						intent.putExtra("originalUrl", item.getBalanceAfter());
						context.startActivity(intent);
					}
				});
			}
		}
						
		return resultView;
	}
}
