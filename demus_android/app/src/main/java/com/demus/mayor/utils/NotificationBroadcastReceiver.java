package com.demus.mayor.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import com.demus.mayor.models.ListObject;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.legacy.content.WakefulBroadcastReceiver;

public class NotificationBroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// get data
		Bundle dataBundle = intent.getBundleExtra("data");

		SQLiteDatabase db = null;

		try {
			boolean appIsActive = dataBundle.getBoolean("isActive");

			JSONObject customJSON = new JSONObject(dataBundle.getString("custom"));

			if (customJSON.has("a")) {
				JSONObject additionalData = customJSON.getJSONObject("a");

				if (additionalData.has("stacked_notifications")) {
					try {
						JSONArray notifs = additionalData.getJSONArray("stacked_notifications");
						
						db = DB2.getDatabase(context);

						for (int i = 0; i < notifs.length(); i++) {
							JSONObject notifObj = notifs.getJSONObject(0);
							
							ListObject tranx = new ListObject();					
							tranx.setSubTitle(notifObj.optString("title"));
							tranx.setExtraString2(notifObj.optString("message"));
							tranx.setExtraString3(String.valueOf(notifObj.optLong("createDate")));
							tranx.setExtraString(notifObj.optString("pictureUrl"));
							tranx.setImageUrl(notifObj.optString("pictureThumbUrl"));
							tranx.setIsNotif(1);
							tranx.setMainID(notifObj.optString("id"));
							
							if(tranx.getTransaction(db) == null)
								tranx.insertObject(db);
							else
								tranx.updateTransaction(db);
						}
					}
					catch (Exception ex) {

					}
				} 
				else {
					db = DB2.getDatabase(context);

					ListObject tranx = new ListObject();					
					tranx.setSubTitle(additionalData.optString("title"));
					tranx.setExtraString2(additionalData.optString("message"));
					tranx.setExtraString3(String.valueOf(additionalData.optLong("createDate")));
					tranx.setExtraString(additionalData.optString("pictureUrl"));
					tranx.setImageUrl(additionalData.optString("pictureThumbUrl"));
					tranx.setIsNotif(1);
					tranx.setMainID(additionalData.optString("id"));
					
					if(tranx.getTransaction(db) == null)
						tranx.insertObject(db);
					else
						tranx.updateTransaction(db);
				}
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		finally {
			if (db != null)
				db.close();
		}
	}
}