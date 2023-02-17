package com.demus.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RESTAccessTokenGetter {
	 
	private static final RESTAccessTokenGetter instance = new RESTAccessTokenGetter();
	
	private String accessToken = "";
	
	final static int MAX_CONNECTIONS = 5;
	
	private Logger logger = LoggerFactory.getLogger(RESTAccessTokenGetter.class);
 
	protected RESTAccessTokenGetter() {
	}
 
	// Runtime initialization
	// By default ThreadSafe
	public static RESTAccessTokenGetter getInstance() {
		return instance;
	}

	public String getAccessToken(String username, String password) {
		logger.error("Token Request U&P: " + username + " >> " + password);
		if(accessToken.equals(""))
			getEcleverToken(username, password, 0);
			
		return accessToken;
	}
	
	private void getEcleverToken(String username, String password, int tries) {
		Response resp = null;
		String jsonObj = null;
		
		try {
		   final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	       OkHttpClient client = new OkHttpClient.Builder()
					        	    .retryOnConnectionFailure(true)
					        	    .build();
	        
	       StringBuffer strJsonBody = new StringBuffer();
	       strJsonBody.append("{")
	        		   .append("\"username\": \"" + username + "\",")
					   .append("\"password\": \"" + password + "\",")
					   .append("\"clientId\": \"867606021427730\",")
					   .append("\"clientOs\": \"ANDROID\",")
		               .append("\"version\": 100,")
		               .append("\"clientSecret\": \"<Uop{n8'8Rvt5~_7BN7mcw:61t&646*gT~W7+9CVp2@7Gh50ioneri=ztx'3'6d\"")
		               .append("}");
	       logger.error("Token Request Body: " + strJsonBody.toString());
	
	       URL url = new URL("https://api.eclevermobile.com:8443/public/person/login?pushKey=bb4380cd-96f4-4926-b2ed-73902233f351");
	       logger.error("Token Request URL: " + url);
	       
	       RequestBody body = RequestBody.create(JSON, strJsonBody.toString());
	        
	       Request request = new Request.Builder()
				              .url(url)
				              .addHeader("Content-Type", "application/json")
				              .post(body)
				              .build();
	          
	       resp = client.newCall(request).execute();
	       jsonObj = resp.body().string();
	        
	       logger.error("Token Request Response: " + jsonObj);
	        
	       JSONParser parser = new JSONParser();
           JSONObject jsonObject = (JSONObject) parser.parse(jsonObj);
           
           String status = jsonObject.get("status").toString();
           if(status != null && status.equals("OK"))
        	   accessToken = ((JSONObject)((JSONArray) jsonObject.get("results")).get(0)).get("authCode").toString();
	    } 
		catch(Exception e) {
		    final StringWriter sw = new StringWriter();
	    	final PrintWriter pw = new PrintWriter(sw, true);
		    e.printStackTrace(pw);
		    
		    String err = sw.getBuffer().toString();
		    
	    	logger.error("Token Request Error: " + e.getMessage() + " > " + err);
	    }
		finally {
			if(resp != null)
				resp.body().close();
		}
	}
}
