package com.demus.api.vendors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demus.api.RESTAccessTokenGetter;
import com.demus.crud.VendorApiLogCrud;
import com.demus.entity.Subscriber;
import com.demus.entity.Subscription;
import com.demus.entity.VendorApiLog;
import com.demus.model.VendorConnectionResponse;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This class facilitates vending of airtime and data
 * 
 * @author Lekan Baruwa
 * @since September - December 2019
 */
@Service
public class VendorApiImpl implements VendorApi {
	
	@Autowired
	VendorApiLogCrud vendorApiLogCrud;
	
	final int MAX_CONNECTIONS = 5;
	
	@Value("${com.demus.phone}")
	private String PHONE;
	
	@Value("${com.demus.pin}")
	private String PIN;
	
	@Value("${com.demus.password}")
	private String PASSWORD;
	
	@Value("${com.demus.recharge.local.clientid}")
	private String LOCAL_CLIENT_ID;
	
	@Value("${com.demus.recharge.local.secret}")
	private String LOCAL_CLIENT_SECRET;
	
	private Logger logger = LoggerFactory.getLogger(VendorApiLog.class);
	
	@Override
	public VendorApiLog deliverOrderRequest(DeliverOrderRq request, String mno, boolean isData, boolean isTest) throws VendorRelationshipException {
		String phoneNumber = request.phoneNumber;
		if (phoneNumber == null || phoneNumber.length() != 11)
			throw new RuntimeException("Phone number is invalid");
		
		logger.error("Subscription quantity: " + request.subscription.getQuantity());
		logger.error("Product code: " + request.subscription.getProduct().getProductCode());
		
		Double amount = (isData ? request.subscription.getCost() : request.subscription.getQuantity() * 1.00);
		String batchId = request.subscription.getProduct().getProductCode();
		Subscriber owner = request.order.getSubscriber();
		Subscription subscription = request.subscription;
		VendorApiLog log = new VendorApiLog(owner, phoneNumber, batchId, amount, subscription);
		log = vendorApiLogCrud.save(log);
		
		if (log.getId() == null) 
			throw new RuntimeException("Could not generate the request id");
		
		//call demus
		VendorConnectionResponse response = demusServerRecharge(log, mno, 0, isData, isTest);
		
		String responseCode = response.getResponseCode();
		logger.error("Demus-Telco Response Code " + responseCode);
		
		if (response.getStatus().equals("OK")) {
			String auditId = "DML-" + response.getTxRefID();
			logger.error("Demus-Telco Response Body - Ref: " + response.getTxRefID());
			
			log.setAuditId(auditId);
			log.setResponseCode(responseCode);
			log.setConfirmationCode (response.getSeqTxRefID());
			log = vendorApiLogCrud.saveAndFlush(log);
			
			return log;
		}
		else
			throw new RuntimeException("Account recharge failed, please try again.");
	}
	
	private VendorConnectionResponse demusServerRecharge(VendorApiLog log, String mno, int tries, boolean isData, boolean isTest) {
		VendorConnectionResponse response = new VendorConnectionResponse();
		
		logger.error("demusServerRecharge Called!!");
		
		if(!mno.equals("GLO"))
			return response;
		
		Response resp = null;
		
		String jsonObj = null;
		
        try {
        	final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	        OkHttpClient client = new OkHttpClient.Builder()
					        	    .retryOnConnectionFailure(true)
					        	    .build();
	        
	        String rawUrl = "https://api.demusmayor.com:8444/services/telco/api/v1/topup" + (isTest ? "/test" : "");
	        String clientId = LOCAL_CLIENT_ID;
	        String clientSecret = LOCAL_CLIENT_SECRET;
	        
	        String to = log.getPhoneNumber();
	        
	        String intlNumber = "";
	        if (to.startsWith("0"))
	            intlNumber = "234" + to.substring(1);
	        else if(to.startsWith("7") || to.startsWith("8") || to.startsWith("9"))
	            intlNumber = "234" + to;
	        else
	            intlNumber = to;
	        
	        StringBuffer strJsonBody = new StringBuffer();
	        strJsonBody.append("{")
	        		   .append("\"client_id\": \"" + clientId + "\",")
					   .append("\"msisdn\": \"" + intlNumber + "\",")
					   .append("\"request_id\": \"" + UUID.randomUUID().toString().replaceAll("-", "") + "\",")
	        		   .append("\"network\": \"" + mno.toLowerCase() + "\",")
		               .append("\"amount\": \"" + log.getAmount().intValue() + "\",")
		               .append("\"product_type\": \"" + (isData ? "data" : "airtime") + "\",")
		               .append("\"client_secret\": \"" + clientSecret + "\",")
		               .append("\"checksum\": \"" + generateCheckSum(rawUrl, clientId, clientSecret) + "\"")
		               .append("}");

	        URL url = new URL(rawUrl);
	        logger.error("URL: " + url);
	        
	        RequestBody body = RequestBody.create(JSON, strJsonBody.toString());
	        
	        Request request = new Request.Builder()
				              .url(url)
				              .addHeader("Content-Type", "application/json")
				              .post(body)
				              .build();
	          
	        resp = client.newCall(request).execute();
	        jsonObj = resp.body().string();
	        
	        logger.error("Converted response: " + jsonObj);
	        
	        JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonObj);
            
            if(jsonObject.get("status") != null) {
	            response.setStatus(Boolean.parseBoolean(jsonObject.get("status").toString()) ? "OK" : "FAILED");
	            response.setResponseCode(String.valueOf(resp.code()));
	            response.setResponseMessage(jsonObject.get("message").toString());
	            
	            logger.error("Response data object: " + jsonObject.get("data").toString());
            }
//            response.setStatus("OK");
//            response.setResponseCode("200");
//            response.setResponseMessage("Works");
	    } 
        catch(Exception e) {
		    final StringWriter sw = new StringWriter();
	    	final PrintWriter pw = new PrintWriter(sw, true);
		    e.printStackTrace(pw);
		    
		    String err = sw.getBuffer().toString();
		    
	    	logger.error("Demus-Telco Recharge error: " + e.getMessage() + " > " + err);
	    		
	    	response.setResponseMessage("Demus-Telco Recharge error: " + jsonObj + " >> " + err);
	    }
		finally {
			if(resp != null)
				resp.body().close();
		}
		
		return response;
	}
	
	private String generateCheckSum(String url, String clientId, String clientSecret) {
		String checkSum = "";
		
		try {
			String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
			String combinedString = encodedUrl + "||" + clientId + "||" + clientSecret;
			
			checkSum = Base64.getEncoder().encodeToString(new BCryptPasswordEncoder().encode(combinedString).getBytes());
		} catch (UnsupportedEncodingException e) {
			final StringWriter sw = new StringWriter();
	    	final PrintWriter pw = new PrintWriter(sw, true);
		    e.printStackTrace(pw);
		    
		    String err = sw.getBuffer().toString();
		    
	    	logger.error("generateCheckSum error: " + e.getMessage() + " > " + err);
		}
		
		return checkSum;
	}
	
	protected VendorConnectionResponse ecleverRecharge(VendorApiLog log, String mno, int tries, boolean isData) {
		VendorConnectionResponse response = new VendorConnectionResponse();
		
		if(!mno.equals("GLO"))
			return response;
		
		logger.error("ecleverRecharge Called!!");
		
		Response resp = null;
		
		String jsonObj = null;
		
        try {
        	final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	        OkHttpClient client = new OkHttpClient.Builder()
					        	    .retryOnConnectionFailure(true)
					        	    .build();
	        
	        StringBuffer strJsonBody = new StringBuffer();
	        strJsonBody.append("{").append("\"mobileNetworkOperator\": \"" + mno + "\",")
					   .append("\"productCode\": \"" + log.getSubscription().getProduct().getProductCode() + "\",")
					   .append("\"phoneNumber\": \"" + PHONE + "\",")
					   .append("\"receiverNumber\": \"" + log.getPhoneNumber() + "\",");
	        
	        if(!isData)
		        strJsonBody.append("\"amount\": " + log.getAmount().intValue() + ",");
	        
	        strJsonBody.append("\"pin\": " + PIN + ",")
		               .append("\"promoCode\": null,")
		               .append("\"clientId\": \"867606021427730\",")
		               .append("\"clientOs\": \"ANDROID\",")
		               .append("\"version\": 100,")
		               .append("\"clientSecret\": \"<Uop{n8'8Rvt5~_7BN7mcw:61t&646*gT~W7+9CVp2@7Gh50ioneri=ztx'3'6d\"}");

	        logger.error("Request Body: " + strJsonBody.toString());
	
	        URL url = isData ? new URL("https://api.eclevermobile.com:8443/secure/product/data_recharge") : 
	        				   new URL("https://api.eclevermobile.com:8443/secure/product/recharge");
	        logger.error("URL: " + url);
	        
	        String token = RESTAccessTokenGetter.getInstance().getAccessToken(PHONE, PASSWORD);
	        logger.error("Token: " + token);
	        
	        RequestBody body = RequestBody.create(JSON, strJsonBody.toString());
	        
	        Request request = new Request.Builder()
				              .url(url)
				              .addHeader("Content-Type", "application/json")
				              .addHeader("Authorization", "Basic " + token)
				              .post(body)
				              .build();
	          
	        resp = client.newCall(request).execute();
	        jsonObj = resp.body().string();
	        
	        logger.error("Converted response: " + jsonObj);
	        
	        JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonObj);
            
            if(jsonObject.get("status") != null) {
	            response.setStatus(jsonObject.get("status").toString());
	            response.setResponseCode(String.valueOf(resp.code()));
	            response.setResponseMessage(jsonObject.get("message").toString());
	            
	            if(response.getStatus().equals("OK")) {
	            	JSONObject result = ((JSONObject)((JSONArray) jsonObject.get("results")).get(0));
		            response.setTxRefID(result.get("transactionId").toString());
		            response.setSeqTxRefID(result.get("id").toString());
	            }
            }
	    } 
        catch(Exception e) {
		    final StringWriter sw = new StringWriter();
	    	final PrintWriter pw = new PrintWriter(sw, true);
		    e.printStackTrace(pw);
		    
		    String err = sw.getBuffer().toString();
		    
	    	logger.error("MTN Recharge error: " + e.getMessage() + " > " + err);
	    		
	    	response.setResponseMessage("MTN Recharge error: " + jsonObj + " >> " + err);
	    }
		finally {
			if(resp != null)
				resp.body().close();
		}
		
		return response;
	}
}
