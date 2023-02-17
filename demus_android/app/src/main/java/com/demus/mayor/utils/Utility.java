package com.demus.mayor.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.demus.mayor.models.ListObject;
import com.demus.mayor.models.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utility {

	public static final String BASE_URL = "https://api.demusmayor.com";
	public static final String SERVER_URL = "https://api.demusmayor.com:8444/";
	
	private static final String CHECK_APP_UPDATE_URL = "/public/device/checkupdate";
	
	public static final String STATUS_OK = "OK";
//	public static final String STATUS_SERVER_ERROR = "SERVER_ERROR";
//	public static final String STATUS_DENIED = "SERVER_DENIED";
	
//	public static final String TAG_REFERRED = "REFERRED";
//	public static final String TAG_PASSWORD_EXPIRED = "PASSWORD_EXPIRED";
	public static final String TAG_PIN_EXPIRED = "PIN_EXPIRED";
	public static final String TAG_EMAIL_EXPIRED = "EMAIL_EXPIRED";
//	public static final String TAG_PHONENUMBER_EXPIRED = "PHONENUMBER_EXPIRED";
	
	public static final String CLIENT_OS = "ANDROID";
	public static final int CLIENT_VERSION = 101;
	public static final String CLIENT_SECRET = "<Uop{n8'8Rvt5~_7BN7mcw:61t&646*gT~W7+9CVp2@7Gh50ioneri=ztx'3'6d";
	
	public static final String STATUS_UPDATED = "UPDATED";
	public static final String STATUS_UPDATE_AVAILABLE = "AVAILABLE";
	public static final String STATUS_EXPIRED = "EXPIRED";
	public static final String STATUS_ERROR = "ERROR";
	public static final String STATUS_GENERAL_ERROR = "GENERAL_ERROR";
	
	public static final int MAX_CONNECTIONS = 5;
	
	private static User user;
	
	public static final String QT_SHORT_CODE = "*322*1*04277301*<amount>#";
	
	public static String checkForUpdate(Context context) {
		HttpURLConnection urlConnection = null;
		URL url;
		DataOutputStream printout;
		DataInputStream in;

        try {
        	url = new URL(BASE_URL + CHECK_APP_UPDATE_URL);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/json");
			urlConnection.setRequestProperty("Connection", "close");

			urlConnection.connect();
        	
        	JSONObject json = new JSONObject();
            json.put("clientId", getDeviceID(context));
            json.put("clientOs", CLIENT_OS);
            json.put("version", CLIENT_VERSION);
            json.put("clientSecret", CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == 200) {
            	in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                
                StringBuilder sb = new StringBuilder();
                
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader

				JSONObject jsonResponse = new JSONObject(jsonObj);
                
                JSONArray resultsArray = jsonResponse.getJSONArray("results");
                
                if(resultsArray.length() > 0) {
                	if(resultsArray.optJSONObject(0).optString("status").equals(STATUS_UPDATED))
                		return STATUS_UPDATED;
                	else if(resultsArray.optJSONObject(0).optString("status").equals(STATUS_UPDATE_AVAILABLE))
                		return STATUS_UPDATE_AVAILABLE;
                	else if(resultsArray.optJSONObject(0).optString("status").equals(STATUS_EXPIRED))
                		return STATUS_EXPIRED;
                }
                else
                	return STATUS_ERROR;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        // return JSON String
        return STATUS_ERROR;
	}
	
	@SuppressLint("MissingPermission")
	public static String getDeviceID(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			
			if(tm.getDeviceId() != null)
				return "" + tm.getDeviceId();
			else
				return "" + tm.getSimSerialNumber();
		}
		catch (Exception e) {
			User user = Utility.getUserAccount(context);
			
			if(user != null)
				return user.getUserID();
			else
				return UUID.randomUUID().toString();
		}
	}
	
	public static void clearAllData(Context context) {
		new ListObject().deleteAll(context);
		new User().deleteAll(context);
	}
	
	public static User getUserAccount(Context context) {
		if(user == null)
			user = new User().getAppOwner(context);
		
		return user;
	}
	
	public static void setUserAccount(User updatedUser) {
		user = updatedUser;
	}
	
	public static String getConstructedDate(long dateToFormat) {
		try {// 2016/12/14T04:05:20
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK);
        	
        	Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Lagos"), Locale.UK);
        	calendar.setTimeInMillis(dateToFormat);
        	
			return formatter.format(calendar.getTime());
		} catch (Exception e) {
			//e.printStackTrace();
		}
        
		return null;
	}
	
	public static String getPrettyDate(long dateToFormat) {
		try {// 14/12/2016
        	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mma", Locale.UK);
        	
        	Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Lagos"), Locale.UK);
        	calendar.setTimeInMillis(dateToFormat);
        	
			return formatter.format(calendar.getTime());
		} catch (Exception e) {
			//e.printStackTrace();
		}
        
		return null;
	}
	
	static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count = is.read(bytes, 0, buffer_size);
              if(count == -1)
                  break;
              
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ignored){}
    }

    public static void closeSoftKey(Activity activity) {
		if (activity == null) return;
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
}
