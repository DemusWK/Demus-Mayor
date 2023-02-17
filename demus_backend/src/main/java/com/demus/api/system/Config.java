/**
 * 
 */
package com.demus.api.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.demus.entity.DeviceOs;

/**
 * Configuration file for all system management
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public class Config {
	
	public static final Map<DeviceOs, Integer> CURRENT_VERSION = new HashMap<DeviceOs, Integer>();
	
	public static final Map<DeviceOs, List<Integer>> APPROVED_VERSION = new HashMap<DeviceOs, List<Integer>>();
	
	public static final Map<DeviceOs, List<Integer>> EXPIRED_VERSION = new HashMap<DeviceOs, List<Integer>>();
	
	public static final HashMap<String, DeviceOs> VALID_KEYS;
	
	public static final HashMap<String, DeviceOs> EXPIRED_KEYS;
	
	public static final int MAX_ADDRESS = 255;

	public static final int MAX_ACCOUNT_NUMBER = 255;

	public static final int MIN_NAME_SIZE = 0;
	
	public static final int MAX_NAME_SIZE = 20;

	public static final Double STARTING_BALANCE = 0.00;

	public static final Double MIN_REFERRAL_AMOUNT = 0.00;

	public static final String CHAR_SET = "UTF-8";

	public static final String APP_NAME = "Demus Mayor";

	protected static final String APP_REPLY_EMAIL = "info@demusmayor.com";

	public static final String PURCHASE_MESSAGE = "Demus Mayor Airtime Retail Confirmation";

	public static final String CC_TRANSACTION = "";

	public static final String WELCOME_MESSAGE = "Welcome to Demus Mayor";

	public static final String CONFIRM_EMAIL_MSG = "Confirm your Email Address";

	public static final String EMAIL_CONFIRMED_MSG = "Email Confirmation";

	public static final String CASH_BACK = "Unload Wallet Receipt";
	
	public static final String BONUS = "Unload Bonus Wallet Receipt";

	public static final String CREDIT_MSG = "Credit Alert: Wallet Transfer Received";

	public static final String WALLET_TRANSFER = "Debit Alert: Wallet Transfer Confirmation";

	public static final String REFER = "Debit Alert: Referral Confirmation";

	public static final String DEPOSIT_MSG = "Transaction Alert : %s";
	
	public static Double MIN_DEPOSIT = 0.00;

	public static int AUTH_TOKEN_EXPIRY_HOURS = 1;

	public static Double MIN_BALANCE = 0.00;
	
	public static final String ONECARD_PHONE = "08039012001";
	
	static {
		VALID_KEYS = new HashMap<String, DeviceOs>();
		VALID_KEYS.put("<Uop{n8'8Rvt5~_7BN7mcw:61t&646*gT~W7+9CVp2@7Gh50ioneri=ztx'3'6d", DeviceOs.ANDROID);
		EXPIRED_KEYS = new HashMap<String, DeviceOs>();
		CURRENT_VERSION.put(DeviceOs.ANDROID, 104);
		APPROVED_VERSION.put(DeviceOs.ANDROID, Arrays.asList(100));
		EXPIRED_VERSION.put(DeviceOs.ANDROID, Arrays.asList(100));
	}
}
