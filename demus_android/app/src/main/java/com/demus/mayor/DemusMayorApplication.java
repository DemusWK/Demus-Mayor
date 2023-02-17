package com.demus.mayor;

import java.lang.reflect.Field;

import com.demus.mayor.utils.DemusMayorNotificationOpenedHandler;
import com.demus.mayor.utils.ImageLoader;
import com.onesignal.OneSignal;

import android.app.Application;
import android.content.Context;
import android.view.ViewConfiguration;

import co.paystack.android.PaystackSdk;

public class DemusMayorApplication extends Application {
	
	public static ImageLoader imageLoader;
	
	private static Context context;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		System.setProperty("http.keepAlive", "false");
		
		OneSignal.startInit(this)
				 .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
				 .setNotificationOpenedHandler(new DemusMayorNotificationOpenedHandler(getApplicationContext()))
				 .unsubscribeWhenNotificationsAreDisabled(true)
				 .init();

		PaystackSdk.initialize(getApplicationContext());
		
		imageLoader = new ImageLoader(getApplicationContext());
		
		//Force overflow menu across board
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
	}
	
	public static Context getAppContext() {
        return DemusMayorApplication.context;
    }
}
