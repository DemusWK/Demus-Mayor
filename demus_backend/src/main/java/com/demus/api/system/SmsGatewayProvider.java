package com.demus.api.system;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demus.api.ex.ApiException;
import com.demus.api.io.ApiResponse;
import com.demus.api.io.ApiResponseStatus;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * This 
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 5 Apr 2016
 */
@Service
public class SmsGatewayProvider implements SmsService {
	
	RestTemplate restTemplate = new RestTemplate();
	
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	final int MAX_CONNECTIONS = 5;
	
	@Value("${com.demus.ebulksms}")
	private String apiKey;

	OkHttpClient client = new OkHttpClient();

	@Override
	public ApiResponse send(String to, String message, String from) throws ApiException {
		HttpURLConnection urlConnection = null;
		URL url;
		DataInputStream in = null;
		BufferedReader reader = null;
		
		String intlNumber = "";
        if (to.startsWith("0"))
            intlNumber = "234" + to.substring(1);
        else if(to.startsWith("7") || to.startsWith("8") || to.startsWith("9"))
            intlNumber = "234" + to;
        else if(to.startsWith("234"))
            intlNumber = to;
        else
        	return new ApiResponse("Sms not sent", null, 0, ApiResponseStatus.SERVER_ERROR);

		try {
			String main_url = "http://api.ebulksms.com:8080/sendsms?username=demusmayor2012@gmail.com&" +
					          "apikey=" + apiKey + "&sender=" + from + "&" +
					          "messagetext=" + URLEncoder.encode(message, "UTF-8") + "&flash=0&recipients=" + intlNumber;
			
			url = new URL(main_url);
       	 
        	urlConnection = (HttpURLConnection) url.openConnection();
        	urlConnection.setDoInput (true);
        	urlConnection.setUseCaches (false);
        	urlConnection.setRequestMethod("GET");
        	urlConnection.connect();
	  
        	int respCode = urlConnection.getResponseCode();

            if(respCode == 200 || respCode == 201) {
            	in = new DataInputStream(urlConnection.getInputStream());
                
                reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                
                String tokenVariables = URLDecoder.decode(reader.readLine(), "UTF-8");
                
                in.close();
                reader.close();
                
                System.out.println(tokenVariables);
                
                if(tokenVariables.startsWith("SUCCESS"))
                	return new ApiResponse("Sms sent", null, 0, ApiResponseStatus.OK);
                else
                	return new ApiResponse("Sms not sent", null, 0, ApiResponseStatus.SERVER_ERROR);
            }
            else
            	System.out.println(respCode);
		} catch (Exception e) {
		  e.printStackTrace();
		}
		finally {
			try {
				if(in != null)
					in.close();
				
				if(reader != null)
					reader.close();
			} 
			catch (Exception e) {
			}
		}
		
		return new ApiResponse("Sms not sent", null, 0, ApiResponseStatus.SERVER_ERROR);
	}
}
