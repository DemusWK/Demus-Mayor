package com.demus.mayor.utils;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.demus.mayor.NotificationStreamActivity;
import com.demus.mayor.models.ListObject;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal.NotificationOpenedHandler;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class DemusMayorNotificationOpenedHandler implements NotificationOpenedHandler {

	private SQLiteDatabase db;

	// A weak reference to the enclosing context
	private static WeakReference<Context> mContext;

	public DemusMayorNotificationOpenedHandler(Context context) {
		mContext = new WeakReference<>(context);
	}

	@Override
	public void notificationOpened(OSNotificationOpenResult result) {
		if (mContext.get() != null) {
			try {
				db = DB2.getDatabase(mContext.get());

				JSONObject notifObj = result.toJSONObject();

				ListObject tranx = new ListObject();
				tranx.setSubTitle(notifObj.optString("title"));
				tranx.setExtraString2(notifObj.optString("message"));
				tranx.setExtraString3(String.valueOf(notifObj.optLong("createDate")));
				tranx.setExtraString(notifObj.optString("pictureUrl"));
				tranx.setImageUrl(notifObj.optString("pictureThumbUrl"));
				tranx.setIsNotif(1);
				tranx.setMainID(notifObj.optString("id"));
				tranx.setBalanceAfter(notifObj.optString("link"));

				if(tranx.getTransaction(db) == null)
					tranx.insertObject(db);
				else
					tranx.updateTransaction(db);

				Intent i = new Intent(mContext.get(), NotificationStreamActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("isNotification", true);
				mContext.get().startActivity(i);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if(db != null)
					db.close();
			}
		}
	}
}
