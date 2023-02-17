package com.demus.mayor.handlers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.demus.mayor.R;
import com.demus.mayor.models.ListObject;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.models.User;
import com.demus.mayor.utils.DB2;
import com.demus.mayor.utils.Utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UserAccountAPI_Handler {
	
	final String REGISTRATION_URL = "/public/person/register2";
	final String LOGIN_URL = "/public/person/login";
	final String MOBILE_CONFIRMATION_URL = "/public/person/phonenumber/confirm";
	final String RESEND_MOBILE_CONFIRMATION_URL = "/secure/person/phonenumber/confirm-request";
	final String RESET_PIN_URL = "/secure/person/pin/reset";
	final String CHANGE_PIN_URL = "/secure/person/pin";
	final String MESSAGES_URL = "/secure/person/messages";
	final String CHANGE_PASSWORD_URL = "/secure/person/password";
	final String FORGOT_PASSWORD_URL = "/public/person/password/forgot";
	final String CONFIRM_FORGOT_PASSWORD_URL = "/public/person/password/confirm";
	final String UPDATE_DEVICE_URL = "/secure/person/device";

	public ServerResponse registerUser(User user, String key, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	if(key == null)
        		key = "";
        	
        	url = new URL(Utility.BASE_URL + REGISTRATION_URL + "?pushKey=" + key);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("email", user.getEmail());
            json.put("firstName", user.getFirstName());
            json.put("lastName", user.getLastName());
            json.put("password", user.getPassword());
            json.put("mobileNetworkOperator", user.getMobileNetwork());
            json.put("mobileNumber", user.getPhoneNumber());
            json.put("countryCode", user.getCountryCode());
            json.put("profilePictureURL", user.getProfilePictureURL());
            json.put("pin", String.valueOf(user.getPin()));
            json.put("clientId", user.getUserID());
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
                
                //delete current user, if any
            	Utility.clearAllData(context);
            	                	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                if(jsonResponse.optString("status").equals(Utility.STATUS_OK)) {
                	JSONArray resultsArray = jsonResponse.getJSONArray("results");
                    
                    if(resultsArray.length() > 0) {
                    	JSONObject resp = resultsArray.getJSONObject(0);
                    	user.setAuthCode(resp.optString("authCode"));
                    }
                    
                    user.insert(context);
                }
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
            }
        }
        catch(Exception e) {
            //e.printStackTrace();
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return registerUser(user, key, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse login(String username, String password, String key, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	if(key == null)
        		key = "";
        	
        	url = new URL(Utility.BASE_URL + LOGIN_URL + "?pushKey=" + key);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
                
                //delete current user, if any
            	Utility.clearAllData(context);
            	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                User user = new User();
                
                if(jsonResponse.optString("status").equals(Utility.STATUS_OK)) {
                	JSONArray resultsArray = jsonResponse.getJSONArray("results");
                    
                    if(resultsArray.length() > 0) {
                    	JSONObject result = resultsArray.getJSONObject(0);                    	
                    	JSONObject wallet = result.getJSONObject("wallet");
                        
                    	//get subscriber info
                    	user.setUserID(Utility.getDeviceID(context));
                    	user.setEmail(result.optString("email"));
                    	user.setPhoneNumber(result.optString("phoneNumber"));
                    	user.setPassword(password);
                    	user.setFirstName(result.optString("firstName"));
                    	user.setLastName(result.optString("lastName"));
                    	user.setMobileNetwork(wallet.optString("mobileNetworkOperator"));
                    	user.setCurrentBalance(wallet.optDouble("balance"));
                    	
                    	if(result.optString("entityStatus").equals("ACTIVE")) {
	                    	user.setPhoneVerified(1);
	                    	user.setEmailVerified(1);
                    	}
                    	
                    	user.setAuthCode(result.optString("authCode"));
                    	
                    	user.insert(context);
                    	
                    	Utility.setUserAccount(user);
                    }
                }
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
            }
        }
        catch(Exception e) {
            //e.printStackTrace();
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return login(username, password, key, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse getTags(String username, String password, String key, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + LOGIN_URL + "?pushKey=" + key);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
            	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                ArrayList<String> tags = null;
                
                if(jsonResponse.optString("status").equals(Utility.STATUS_OK)) {
                	JSONArray resultsArray = jsonResponse.getJSONArray("results");
                    
                    if(resultsArray.length() > 0) {
                    	JSONObject result = resultsArray.getJSONObject(0);
                    	
                    	//get all tags
                    	JSONArray tagsJsonArray = result.getJSONArray("tags");
                    	
                    	if(tagsJsonArray.length() > 0) {
	                    	tags = new ArrayList<String>();
	                    	
	                    	for (int i = 0; i < tagsJsonArray.length(); i++)
								tags.add(tagsJsonArray.optString(i));
                    	}
                    }
                }
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
                response.setTags(tags);
            }
        }
        catch(Exception e) {
            //e.printStackTrace();
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return getTags(username, password, key, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse confirmPhoneNumber(String accessCode, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		User user = Utility.getUserAccount(context);
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + MOBILE_CONFIRMATION_URL);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("accessCode", accessCode);
            json.put("mobileNumber", user.getPhoneNumber());
            json.put("phoneNumber", user.getPhoneNumber());
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
            	                	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                if(jsonResponse.optString("status").equals(Utility.STATUS_OK)) {
                	user.setPhoneVerified(1);
                	user.update(context, user.getUserID());
                	
                	Utility.setUserAccount(user);
                }
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
            }

        }
        catch(Exception e) {
            //e.printStackTrace();
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return confirmPhoneNumber(accessCode, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse resendAccessCode(String phoneNumber, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + RESEND_MOBILE_CONFIRMATION_URL);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	urlConnection.setRequestProperty("Authorization", "Basic " + Utility.getUserAccount(context).getAuthCode());
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("username", phoneNumber);
            json.put("phoneNumber", phoneNumber);
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
            	                	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
            }

        }
        catch(Exception e) {
            //e.printStackTrace();
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return resendAccessCode(phoneNumber, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse resetPin(Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + RESET_PIN_URL);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	urlConnection.setRequestProperty("Authorization", "Basic " + Utility.getUserAccount(context).getAuthCode());
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("password", Utility.getUserAccount(context).getPassword());
            json.put("phoneNumber", Utility.getUserAccount(context).getPhoneNumber());
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200 || responseCode == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
            	                	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return resetPin(context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse changePin(String oldPin, String newPin, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + CHANGE_PIN_URL);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	urlConnection.setRequestProperty("Authorization", "Basic " + Utility.getUserAccount(context).getAuthCode());
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("oldPin", oldPin);
            json.put("pin", newPin);
            json.put("phoneNumber", Utility.getUserAccount(context).getPhoneNumber());
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
            	                	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return changePin(oldPin, newPin, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse changePassword(String newPassword, String key, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + CHANGE_PASSWORD_URL);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	urlConnection.setRequestProperty("Authorization", "Basic " + Utility.getUserAccount(context).getAuthCode());
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("oldPassword", Utility.getUserAccount(context).getPassword());
            json.put("newPassword", newPassword);
            json.put("confirmPassword", newPassword);
            json.put("phoneNumber", Utility.getUserAccount(context).getPhoneNumber());
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
            	                	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
                
                if(response.getResponseCode().equals(Utility.STATUS_OK))
                	login(Utility.getUserAccount(context).getPhoneNumber(), newPassword, key, context, 1);
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return changePassword(newPassword, key, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse forgotPassword(String emailOrPhoneNumber, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + FORGOT_PASSWORD_URL);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("emailOrPhoneNumber", emailOrPhoneNumber);
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
            	                	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return forgotPassword(emailOrPhoneNumber, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse confirmForgotPassword(String token, String emailOrPhoneNumber, String newPassword, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + CONFIRM_FORGOT_PASSWORD_URL);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("token", token);
            json.put("emailOrPhoneNumber", emailOrPhoneNumber);
            json.put("newPassword", newPassword);
            json.put("confirmPassword", newPassword);
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
            	                	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
            }

        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return confirmForgotPassword(token, emailOrPhoneNumber, newPassword, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse getMessages(Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
		SQLiteDatabase db = null;
		
        try {
        	url = new URL(Utility.BASE_URL + MESSAGES_URL);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	urlConnection.setRequestProperty("Authorization", "Basic " + Utility.getUserAccount(context).getAuthCode());
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	Calendar calFrom = Calendar.getInstance();
        	calFrom.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        	calFrom.clear(Calendar.MINUTE);
        	calFrom.clear(Calendar.SECOND);
        	calFrom.clear(Calendar.MILLISECOND);

        	// get start of this week in milliseconds
        	calFrom.set(Calendar.DAY_OF_WEEK, calFrom.getFirstDayOfWeek());
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("startDate", Utility.getConstructedDate(calFrom.getTimeInMillis()));
            json.put("phoneNumber", Utility.getUserAccount(context).getPhoneNumber());
            json.put("password", Utility.getUserAccount(context).getPassword());
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
                
                db = DB2.getDatabase(context);
            	                	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
                
                JSONArray tranxArray = jsonResponse.getJSONArray("results");
                
                for (int i = 0; i < tranxArray.length(); i++) {
					JSONObject tranxObj = tranxArray.getJSONObject(i);
					
					ListObject tranx = new ListObject();					
					tranx.setSubTitle(tranxObj.optString("title"));
					tranx.setExtraString2(tranxObj.optString("message"));
					tranx.setExtraString3(String.valueOf(tranxObj.optLong("createDate")));
					tranx.setExtraString(tranxObj.optString("pictureUrl"));
					tranx.setImageUrl(tranxObj.optString("pictureThumbUrl"));
					tranx.setIsNotif(1);
					tranx.setMainID(tranxObj.optString("id"));
					tranx.setBalanceAfter(tranxObj.optString("link"));
					
					if(tranx.getNotification(db) == null)
						tranx.insertObject(db);
					else
						tranx.updateNotification(db);
				}
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return getMessages(context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        	if(db != null)
        		db.close();
        }
        
        return response;
	}
	
	public ServerResponse updatePushKey(String pushKey, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + UPDATE_DEVICE_URL);
        	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setDoOutput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("POST");
        	urlConnection.setRequestProperty("Content-Type", "application/json");
        	urlConnection.setRequestProperty("Authorization", "Basic " + Utility.getUserAccount(context).getAuthCode());
        	
        	if (tries > 0 && tries <= Utility.MAX_CONNECTIONS)
        		urlConnection.setRequestProperty("Connection", "close");
        	
        	urlConnection.connect();
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("pushNotificationKey", pushKey);
            json.put("clientId", Utility.getDeviceID(context));
            json.put("clientOs", Utility.CLIENT_OS);
            json.put("version", Utility.CLIENT_VERSION);
            json.put("clientSecret", Utility.CLIENT_SECRET);
            
            printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(json.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            if(urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                in = new DataInputStream(urlConnection.getInputStream());
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                
                in.close();
                
                String jsonObj = sb.toString();
                
                reader.close();//close reader
                sb = null;
            	                	
                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));
                
                if(response.getResponseCode().equals(Utility.STATUS_OK)) {
                	User user = Utility.getUserAccount(context);
                	user.setBio(pushKey);
                	user.update(context, user.getUserID());
                }
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return updatePushKey(pushKey, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
}
