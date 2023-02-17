package com.demus.mayor.handlers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.demus.mayor.R;
import com.demus.mayor.models.BankAccount;
import com.demus.mayor.models.PaystackTransaction;
import com.demus.mayor.models.Product;
import com.demus.mayor.models.Bank;
import com.demus.mayor.models.ListObject;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.models.User;
import com.demus.mayor.utils.DB2;
import com.demus.mayor.utils.Utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class TransactionAPI_Handler {
	
	final String AIRTIME_RECHARGE_PURCHASE_URL = "/secure/product/recharge";
	final String DATA_RECHARGE_PURCHASE_URL = "/secure/product/data_recharge";
	final String REFER_URL = "/secure/wallet/refer";
	final String CHECK_BALANCE_URL = "/secure/wallet/balance";
	final String CASHBACK_URL = "/secure/wallet/cashback";
	final String BANKS_URL = "/public/banks";	
	//final String PRODUCTS_URL = "/public/products";
	final String GET_TRANSACTION_URL = "/secure/wallet/transaction";
	final String RECENT_TRANSACTIONS_URL = "/secure/wallet/transactions";
	final String ACCOUNT_STMT_URL = "/secure/wallet/account_statement";
	final String LOAD_PIN_URL = "/secure/wallet/pin_load";
	final String SHARE_STOCK_URL = "/secure/wallet/transfer";
    final String CONFIRM_TRANSACTION_URL = "/secure/wallet/deposit";
    final String BANK_ACCOUNT_UPDATE_URL = "/secure/wallet/update_primary_bank";
    final String BANK_ACCOUNTS_URL = "/secure/person/banks";

	public ServerResponse recharge(Product product, Context context, int tries) {		
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	if(product.getType() == Product.AIRTIME_PRODUCT_TYPE)
        		url = new URL(Utility.BASE_URL + AIRTIME_RECHARGE_PURCHASE_URL + "?nonce=" + UUID.randomUUID().toString());
        	else
        		url = new URL(Utility.BASE_URL + DATA_RECHARGE_PURCHASE_URL + "?nonce=" + UUID.randomUUID().toString());
        	 
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
            json.put("mobileNetworkOperator", product.getMobileNetworkOperator());
            json.put("receiverNumber", product.getReceiverNumber());
            
            if(product.getType() == Product.AIRTIME_PRODUCT_TYPE)
	            json.put("amount", product.getAmount().toString());
            else
	            json.put("productCode", product.getAmount());
            
            json.put("pin", product.getPin());
            json.put("promoCode", product.getPromoCode());
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

        } catch(Exception e) {
            //e.printStackTrace();
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return recharge(product, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse refer(Product referent, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + REFER_URL);
        	 
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
            json.put("receiverPhoneNumber", referent.getReceiverNumber());
            json.put("amount", referent.getAmount().toString());
            json.put("pin", referent.getPin());
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
                
                if(response.getResponseCode().equals(Utility.STATUS_OK)) {
                	JSONArray resultArray = jsonResponse.getJSONArray("results");
                	response.setExtraDoubleValue(resultArray.getJSONObject(0).optDouble("balanceAfterTransaction"));
                	
	                JSONObject walletObject = resultArray.getJSONObject(0).optJSONObject("wallet");
	                response.setExtraDoubleValue2(walletObject.optDouble("bonusBalance"));
                }
            }

        } catch(Exception e) {
            //e.printStackTrace();
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return refer(referent, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse checkBalance(Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + CHECK_BALANCE_URL);
        	 
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
        	
        	User user = Utility.getUserAccount(context);
        	
        	//data output
            JSONObject json = new JSONObject();
            json.put("password", Utility.getUserAccount(context).getPassword());
            json.put("phoneNumber", user.getPhoneNumber());
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
                
                JSONArray resultArray = jsonResponse.getJSONArray("results");
                response.setExtraDoubleValue(resultArray.getJSONObject(0).optDouble("balance"));
                response.setExtraDoubleValue2(resultArray.getJSONObject(0).optDouble("bonusBalance"));
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return checkBalance(context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse getAccountStatement(String pin, String fromDate, String toDate, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + ACCOUNT_STMT_URL);
        	 
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
            json.put("startDate", fromDate);
            json.put("endDate", toDate);
            json.put("pin", pin);
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

                return getAccountStatement(pin, fromDate, toDate, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}

    public ServerResponse confirmPaystack(PaystackTransaction tranx, Context context, int tries) {
        HttpURLConnection urlConnection = null;
        DataOutputStream printout;
        DataInputStream  in;
        URL url;

        ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));

        try {
            url = new URL(Utility.BASE_URL + CONFIRM_TRANSACTION_URL);

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
            json.put("tranxRef", tranx.getPaystackTransactionRef());
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

                if(response.getResponseCode().equals(Utility.STATUS_OK)) {
                    JSONArray resultArray = jsonResponse.getJSONArray("results");
                    response.setExtraDoubleValue(resultArray.getJSONObject(0).optDouble("balanceAfterTransaction"));

                    JSONObject walletObject = resultArray.getJSONObject(0).optJSONObject("wallet");
                    response.setExtraDoubleValue2(walletObject.optDouble("bonusBalance"));
                }
            }
        }
        catch(Exception e) {
            if (tries <= Utility.MAX_CONNECTIONS) {
                if(urlConnection != null)
                    urlConnection.disconnect();

                urlConnection = null;

                return confirmPaystack(tranx, context, tries + 1);
            }
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }
	
	public ServerResponse cashback(String bankCode, String accountNumber, String accountType, String amount, String pin, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + CASHBACK_URL);
        	 
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
            json.put("amount", amount);
            json.put("bankOperatorCode", bankCode);
            json.put("accountNumber", accountNumber);
            json.put("bankAccountType", accountType);
            json.put("pin", pin);
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
                
                if(response.getResponseCode().equals(Utility.STATUS_OK)) {
                	JSONArray resultArray = jsonResponse.getJSONArray("results");
                	response.setExtraDoubleValue(resultArray.getJSONObject(0).optDouble("balanceAfterTransaction"));
                		                
	                JSONObject walletObject = resultArray.getJSONObject(0).optJSONObject("wallet");	                
	                response.setExtraDoubleValue2(walletObject.optDouble("bonusBalance"));
                }
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return cashback(bankCode, accountNumber, accountType, amount, pin, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}

    public ServerResponse primaryBankAccountUpdate(BankAccount bankAcc, String pin, Context context, int tries) {
        HttpURLConnection urlConnection = null;
        DataOutputStream printout;
        DataInputStream  in;
        URL url;

        SQLiteDatabase db = null;

        ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));

        try {
            url = new URL(Utility.BASE_URL + BANK_ACCOUNT_UPDATE_URL);

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
            json.put("bankOperatorCode", bankAcc.getBankOperatorCode());
            json.put("accountNumber", bankAcc.getAccountNumber());
            json.put("bankAccountType", bankAcc.getAccountType());
            json.put("isPrimary", bankAcc.getIsPrimary());
            json.put("pin", pin);
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

                db = DB2.getDatabase(context);

                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));

                JSONArray tranxArray = jsonResponse.getJSONArray("results");

                for (int i = 0; i < tranxArray.length(); i++) {
                    JSONObject tranxObj = tranxArray.getJSONObject(i);

                    JSONObject bankOperator = tranxObj.getJSONObject("bankOperator");

                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setAccountNumber(tranxObj.optString("accountNumber"));
                    bankAccount.setBankName(bankOperator.optString("name"));
                    bankAccount.setBankOperatorCode(bankOperator.optString("bankOperatorCode"));
                    bankAccount.setAccountType(tranxObj.optString("type"));
                    bankAccount.setIsPrimary(tranxObj.optBoolean("isPrimary"));

                    if(bankAccount.get(bankAccount.getAccountNumber(), context) == null)
                        bankAccount.insert(db);
                    else
                        bankAccount.update(db);
                }
            }
        }
        catch(Exception e) {
            if (tries <= Utility.MAX_CONNECTIONS) {
                if(urlConnection != null)
                    urlConnection.disconnect();

                urlConnection = null;

                return primaryBankAccountUpdate(bankAcc, pin, context, tries + 1);
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

    public ServerResponse shareStock(String recipient, String amount, String pin, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + SHARE_STOCK_URL);
        	 
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
            json.put("amount", amount);
            json.put("receiverPhoneNumber", recipient);
            json.put("pin", pin);
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
                
                if(response.getResponseCode().equals(Utility.STATUS_OK)) {
                	JSONArray resultArray = jsonResponse.getJSONArray("results");
                	response.setExtraDoubleValue(resultArray.getJSONObject(0).optDouble("balanceAfterTransaction"));
                	
	                JSONObject walletObject = resultArray.getJSONObject(0).optJSONObject("wallet");
	                response.setExtraDoubleValue2(walletObject.optDouble("bonusBalance"));
                }
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return shareStock(recipient, amount, pin, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
	
	public ServerResponse getTransactions(Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
		SQLiteDatabase db = null;
		
        try {
        	url = new URL(Utility.BASE_URL + RECENT_TRANSACTIONS_URL);
        	 
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
            json.put("page", 0);
            json.put("size", 20);
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
					tranx.setSubTitle(tranxObj.optString("transactionId"));
					tranx.setExtraString2(tranxObj.optString("description"));
					tranx.setExtraString3(String.valueOf(tranxObj.optLong("createDate")));
					tranx.setExtraString(tranxObj.optString("transactionStatus"));
					tranx.setBalanceAfter(tranxObj.optString("balanceAfterTransaction"));
					tranx.setImageUrl("");
					tranx.setIsNotif(0);
					
					if(tranx.getTransaction(db) == null)
						tranx.insertObject(db);
					else
						tranx.updateTransaction(db);
				}
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return getTransactions(context, tries + 1);
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

    public ServerResponse getBankAccounts(Context context, int tries) {
        HttpURLConnection urlConnection = null;
        DataOutputStream printout;
        DataInputStream  in;
        URL url;

        ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));

        SQLiteDatabase db = null;

        try {
            url = new URL(Utility.BASE_URL + BANK_ACCOUNTS_URL);

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

                db = DB2.getDatabase(context);

                // try parse the string to a JSON object
                JSONObject jsonResponse = new JSONObject(jsonObj);
                response.setResponseCode(jsonResponse.optString("status"));
                response.setResponseMessage(jsonResponse.optString("message"));

                JSONArray tranxArray = jsonResponse.getJSONArray("results");

                for (int i = 0; i < tranxArray.length(); i++) {
                    JSONObject tranxObj = tranxArray.getJSONObject(i);

                    JSONObject bankOperator = tranxObj.getJSONObject("bankOperator");

                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setAccountNumber(tranxObj.optString("accountNumber"));
                    bankAccount.setBankName(bankOperator.optString("name"));
                    bankAccount.setBankOperatorCode(bankOperator.optString("bankOperatorCode"));
                    bankAccount.setAccountType(tranxObj.optString("type"));
                    bankAccount.setIsPrimary(tranxObj.optBoolean("isPrimary"));

                    if(bankAccount.get(bankAccount.getAccountNumber(), context) == null)
                        bankAccount.insert(db);
                    else
                        bankAccount.update(db);
                }
            }
        }
        catch(Exception e) {
            if (tries <= Utility.MAX_CONNECTIONS) {
                if(urlConnection != null)
                    urlConnection.disconnect();

                urlConnection = null;

                return getBankAccounts(context, tries + 1);
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
	
	public ListObject getTransaction(String tranxCode, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ListObject tranx = null;
		
        try {
        	url = new URL(Utility.BASE_URL + GET_TRANSACTION_URL);
        	 
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
            json.put("id", tranxCode);
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
                JSONArray tranxArray = jsonResponse.getJSONArray("results");
                
                JSONObject tranxObj = tranxArray.getJSONObject(0);
				
				tranx = new ListObject();					
				tranx.setSubTitle(tranxObj.optString("transactionId"));
				tranx.setExtraString2(tranxObj.optString("description"));
				tranx.setExtraString3(String.valueOf(tranxObj.optLong("createDate")));
				tranx.setExtraString(tranxObj.optString("transactionStatus"));
				tranx.setBalanceAfter(tranxObj.optString("balanceAfterTransaction"));
				tranx.setImageUrl(tranxObj.getJSONObject("wallet").optString("walletId"));
				tranx.setIsNotif(0);
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return getTransaction(tranxCode, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return tranx;
	}
	
	public ArrayList<Bank> getBanks(Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		SQLiteDatabase db = null;
		
		ArrayList<Bank> banks = new ArrayList<Bank>();
		
        try {
        	url = new URL(Utility.BASE_URL + BANKS_URL);
        	 
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
                
                JSONArray bankArray = jsonResponse.getJSONArray("results");
                
                db = DB2.getDatabase(context);
                
                for (int i = 0; i < bankArray.length(); i++) {
					JSONObject bankObj = bankArray.getJSONObject(i);
					
					Bank bank = new Bank();
					bank.setBankCode(bankObj.optString("bankOperatorCode"));
					bank.setBankName(bankObj.optString("name"));
					
					if(bank.get(bank.getBankCode(), db) == null)
						bank.insert(db);
					
					banks.add(bank);
				}
            }
        }
        catch(Exception e) {
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return getBanks(context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        	if(db != null)
        		db.close();
        }
        
        return banks;
	}
	
	public ServerResponse loadPin(String cardPin, Context context, int tries) {
		HttpURLConnection urlConnection = null;
		DataOutputStream printout;
		DataInputStream  in;
		URL url;
		
		ServerResponse response = new ServerResponse();
        response.setResponseCode(Utility.STATUS_GENERAL_ERROR);
        response.setResponseMessage(context.getString(R.string.general_fail));
		
        try {
        	url = new URL(Utility.BASE_URL + LOAD_PIN_URL);
        	 
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
            json.put("walletId", Utility.getUserAccount(context).getPhoneNumber());
            json.put("pinNumber", cardPin);
            json.put("phoneNumber", Utility.getUserAccount(context).getPhoneNumber());
            json.put("promoCode", "");
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
                	JSONArray resultArray = jsonResponse.getJSONArray("results");
                	response.setExtraDoubleValue(resultArray.getJSONObject(0).optDouble("balanceAfterTransaction"));
                	
	                JSONObject walletObject = resultArray.getJSONObject(0).optJSONObject("wallet");
	                response.setExtraDoubleValue2(walletObject.optDouble("bonusBalance"));
                }
            }

        } catch(Exception e) {
            //e.printStackTrace();
        	if (tries <= Utility.MAX_CONNECTIONS) {
        		if(urlConnection != null)
            		urlConnection.disconnect();
        		
                urlConnection = null;

                return loadPin(cardPin, context, tries + 1);
            }
        }
        finally {
        	if(urlConnection != null)
        		urlConnection.disconnect();
        }
        
        return response;
	}
}
