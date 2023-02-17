package com.demus.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demus.Company;
import com.demus.api.User;
import com.demus.api.admin.Filter;
import com.demus.api.admin.ModelApi;
import com.demus.api.ex.ApiAuthenticationException;
import com.demus.api.ex.ApiException;
import com.demus.api.ex.ClientExpiredException;
import com.demus.api.ex.InvalidStateException;
import com.demus.api.io.ApiRequest;
import com.demus.api.io.ApiRequestValidator;
import com.demus.api.io.ApiResponse;
import com.demus.api.io.ApiResponseStatus;
import com.demus.api.system.Config;
import com.demus.api.system.EmailService;
import com.demus.api.system.SmsService;
import com.demus.api.vendors.DeliverOrderRq;
import com.demus.api.vendors.VendorApi;
import com.demus.crud.PaystackAuthorizationTokenCrud;
import com.demus.crud.PlatformConfigurationCrud;
import com.demus.crud.ProductCrud;
import com.demus.crud.PushMessageCrud;
import com.demus.crud.SubscriberCrud;
import com.demus.crud.WalletCrud;
import com.demus.crud.WalletTransactionCrud;
import com.demus.entity.ActivateTokenType;
import com.demus.entity.ActivationToken;
import com.demus.entity.BankAccount;
import com.demus.entity.BankAccountType;
import com.demus.entity.BankOperator;
import com.demus.entity.Device;
import com.demus.entity.DeviceOs;
import com.demus.entity.EntityStatus;
import com.demus.entity.Gender;
import com.demus.entity.MobileNetworkOperator;
import com.demus.entity.Order;
import com.demus.entity.OrderStatus;
import com.demus.entity.PaymentGateway;
import com.demus.entity.PaystackAuthorizationToken;
import com.demus.entity.Permission;
import com.demus.entity.PlatformConfiguration;
import com.demus.entity.Product;
import com.demus.entity.PushMessage;
import com.demus.entity.Refer;
import com.demus.entity.Role;
import com.demus.entity.Subscriber;
import com.demus.entity.SubscriberTag;
import com.demus.entity.Subscription;
import com.demus.entity.VendorApiLog;
import com.demus.entity.Wallet;
import com.demus.entity.WalletTransaction;
import com.demus.entity.WalletTransactionStatus;
import com.demus.entity.WalletTransactionType;
import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Public and Secure APIs for client side actions
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Service
@SuppressWarnings("unchecked")
@DependsOn ({"ConfigurePlatform"})
public class SubscriberModelImpl implements SubscriberModel {
	
	@Autowired
	ModelApi modelApi;
	
	@Autowired
	VendorApi vendorApi;
	
	@Autowired
	SmsService smsService;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Value("${com.demus.host}")
	private String host;
	
	@Value("${com.demus.sms}")
	private Boolean smsServiceActive;
	
	@Value("${com.demus.vendor}")
	private Boolean vendorServiceActive;
	
	@Value("${com.demus.transfer}")
	private Boolean transferService;
	
	@Value("${com.demus.pinLoad}")
	private Boolean pinLoadActive;
	
	@Value("${com.demus.phone}")
	private String PHONE;
	
	@Value("${com.demus.pin}")
	private String PIN;
	
	@Autowired
	private HttpServletRequest httpRequest;
	
	@Value("${com.demus.smsLabel}")
	private String smsLabel;
	
	@Value("${com.demus.paystack.secret}")
	private String payStackSecret;
	
	@Value("${com.demus.paystack.secret.test}")
	private String payStackSecretTest;
	
	@Autowired
	private PaystackAuthorizationTokenCrud paystackAuthorizationTokenCrud;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	PlatformConfigurationCrud platformConfigurationCrud;
	
	PlatformConfiguration platformConfiguration;
	
	@Value("${com.eclever.product.mtn}")
	private String MTN_CODE;
	
	@Value("${com.eclever.product.glo}")
	private String GLO_CODE;
	
	@Value("${com.eclever.product.etisalat}")
	private String ETISALAT_CODE;
	
	@Value("${com.eclever.product.airtel}")
	private String AIRTEL_CODE;
	
	@Value("${com.eclever.product.visafone}")
	private String VISAFONE_CODE;
	
	@Value("${com.eclever.product.generic}")
	private String GENERIC_CODE;
	
	@Value("${com.demus.transactionPrefix}")
	private String TRANSACTION_PREFIX;
	
	@Value("${com.demus.playStoreUrl}")
	private String playStoreUrl; 
	
	@Autowired
	private ProductCrud productCrud;
	
	final int MAX_CONNECTIONS = 5;
	
	@PostConstruct
	public void init () throws ApiException {
		List<PlatformConfiguration> configurations = platformConfigurationCrud.findAll();
		if (configurations == null || configurations.size() == 0) {
			logger.error("Configurations not setup");
			throwMsg(true, "Platform configuration table has no record");
		}
		platformConfiguration = configurations.get(0);
	}
	
	@PostConstruct
	private void configureVelocity () {
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
	}
	
	private Logger logger = LoggerFactory.getLogger(SubscriberModelImpl.class);
	
	private ApiResponse success(String message, Object object) {
		if (object == null)
			return new ApiResponse(message, Collections.emptyList(), 0, ApiResponseStatus.OK);
		else if (object instanceof List) {
			List<Object> list = (List<Object>) object;
			return new ApiResponse(message, object, list.size(), ApiResponseStatus.OK);
		} else {
			List<Object> list = Arrays.asList(object);
			return new ApiResponse(message, list, list.size(), ApiResponseStatus.OK);
		}
	}
	
	public Subscriber getSessionUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			return null;
		Subscriber subscriber = null;
		try {
			subscriber = ((User) authentication.getPrincipal()).getSubscriber();
		} catch (Exception e) {
			System.out.println(authentication.getPrincipal());
		}
		
		return subscriber;
	}
	
	private void validateRequest (DepositRq request) throws ApiException {
		required("Switch Secret", request.switchSecret);
		//throwMsg(!request.switchSecret.equals(switchSecret), "The switch secret is not correct");
		required("Payment Log Id", request.paymentLogId);
		required("Amount", request.amount);
		required("Wallet Id", request.walletId);
		required("Payment Gateway", request.paymentGateway);
	}

	private void validateRequest (ApiRequest request) throws ApiException {
		ApiRequestValidator.notNull("request", request);
		String clientId = request.clientId;
		DeviceOs clientOs = request.clientOs;
		String phoneNumber = request.phoneNumber;
		required("Client Os", clientOs);
		if (!clientOs.equals(DeviceOs.WEB)) {
			String clientSecret = request.clientSecret;
			Integer version = request.version;
			ApiRequestValidator.notNull("client id", clientId);
			ApiRequestValidator.notNull("client operating system", clientOs);
			ApiRequestValidator.notNull("client secret", clientSecret);
			ApiRequestValidator.notNull("client version", version);
	
			Boolean validKey = Config.VALID_KEYS.get(clientSecret) != null;
			Boolean expiredKey = Config.EXPIRED_KEYS.get(clientSecret) != null;
			if (validKey)
				return;
			else if (expiredKey) 
				throw new ClientExpiredException("Client expired please update app");
			else 	
				throw new ApiAuthenticationException("Client secret expired, not valid");
		}
		
		final Subscriber user = getSessionUser();
		// Run this line only if the current session is authenticated
		if (user != null && phoneNumber != null) {
			user.setLastSeen(new Date());
			user.setLastIp(httpRequest.getRemoteAddr() + "-" + httpRequest.getHeader("X-Forwarded-For"));
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						logger.debug("Trying to log user request");
						modelApi.putSubscriber(user);
					} catch (ApiException e) {
					}	
				}
			}).start();
			String auditorPhoneNumber = user.getPhoneNumber();
			List<Permission> permissions = user.getRole().getPermissions();
			if (!permissions.contains(new Permission("SUPPORT")) && !phoneNumber.equals(auditorPhoneNumber)) 
				throw new ApiException("Request unauthorized. Target wallet id and your wallet id are not the same.");
		} 
		
	}
	
	@Autowired
	SubscriberCrud subscriberCrud;
	
	@Override
	public ApiResponse confirmSubscriber (String id) throws ApiException {
		required("Phone Number or Email", id);
		logger.debug("Confirming " + id);
		/*ApiResponse response = modelApi.getSubscriberByPhonenumber(id, null);
		if (!hasData(response)) {
			response = modelApi.getSubscriberByEmail(id);
			throwMsg(!hasData(response), "Subscriber not available");
		}*/
		Subscriber subscriber = subscriberCrud.findByPhoneNumberOrEmail(id);//(Subscriber) getData(response);
		throwCannotTransact(subscriber);
		return success("Got subscriber", subscriber);
	}
 	
	/**
	 * Generate auth code for communication with server
	 */
	@Override
	public ApiResponse login(LoginRq request) throws ApiException {
		if(request.clientOs == null) {
			request.clientOs = DeviceOs.WEB;
		}
		validateRequest(request);
		
		String username = request.username;
		required("Email or Phone Number", username);
		String password = request.password;
		required("Password", password);
		ApiResponse responseEmail = modelApi.getSubscriberByEmail(username);
		ApiResponse responsePhoneNumber = modelApi.getSubscriberByPhonenumber(username, null);
		String error = "User details supplied is incorrect";
		if (!hasData(responseEmail) && !hasData(responsePhoneNumber) && EmailValidator.getInstance().isValid(username)) {
			throwMsg(true, error + " 1");
		} else if (!hasData(responseEmail) && !hasData(responsePhoneNumber) && username.length() == 11) {
			throwMsg(true, error + " 2");
		} else if (!hasData(responseEmail) && !hasData(responsePhoneNumber)) {
			throwMsg(true, error + " 3");
		}
		logger.debug("Attempting to determine if subscriber email or phone number was supplied");
		Subscriber potentialSubscriber = null;
		
		if (hasData(responseEmail)) {
			potentialSubscriber = (Subscriber) getData(responseEmail);
		} else if (hasData(responsePhoneNumber)) {
			potentialSubscriber = (Subscriber) getData(responsePhoneNumber);
		} else{
			throwMsg(true, error);
		}
		logger.debug("Subscriber found validating password");
		throwMsg(!potentialSubscriber.matchPassword(password), error);
		String clientId = request.clientId;
		DeviceOs clientOs = request.clientOs;
		Integer version = request.version;
		String pushNotificationKey = request.pushNotificationKey;
		if (request.clientId != null) {
			ApiResponse response = modelApi.getDevice(clientId, clientOs);
			Device device = null;
			if (hasData(response)) {
				// If this device is already registered. Claim its ownership.
				device = (Device) getData(response);
				device.setSubscriber(potentialSubscriber);
				device.setEntityStatus(EntityStatus.ACTIVE);
			//throwMsg(!hasData(modelApi.putDevice(device)), "Could not switch device owner");
			} else {
				device = new Device(clientId, clientOs, version, pushNotificationKey, potentialSubscriber);
			}
			
			device.setPushNotificationKey(pushNotificationKey);
			response = modelApi.putDevice(device);
			throwMsg(!hasData(response), "Could not register the device");
		}
		String email = potentialSubscriber.getEmail();
		String authCode = getAuthCode(email, password);
		potentialSubscriber.setAuthCode(authCode);
		return success("Login successful", potentialSubscriber);
	}
	
	private String getAuthCode (String username, String password) {
		String auth_code = username.toLowerCase() + ":" + password;
		auth_code = new String(Base64.getEncoder().encodeToString(auth_code.getBytes()));
		return auth_code;
	}
	
	/**
	 * An api that can be called activate a subscriber account
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse confirmEmail(ConfirmDataRq request) throws ApiException {
		String accessCode = request.accessCode;
		required("Access Code", accessCode);
		ApiResponse response = modelApi.getActivationToken(null, accessCode, ActivateTokenType.EMAIL);
		throwMsg(!hasData(response), "Token not found or has expired");
		ActivationToken activationToken = (ActivationToken) getData(response);
		Subscriber subscriber = activationToken.getSubscriber();
		subscriber.getTags().remove(SubscriberTag.EMAIL_EXPIRED);
		response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not update subscriber account");
		subscriber = (Subscriber) getData(response);
		if(!subscriber.getTags().contains(SubscriberTag.PHONENUMBER_EXPIRED)) {
			subscriber.setEntityStatus(EntityStatus.ACTIVE);
			applyReferralWorth(subscriber);
			logger.debug("Activated subscriber and applied any pending wallet amount");
		}
		response = modelApi.deleteActivationToken(Arrays.asList(activationToken.getId()));
		throwMsg(notOK(response), "Could not clear subscriber activation tokens");
		String extraMessage = "Thank you for confirming your email address. " + (subscriber.getEntityStatus() == EntityStatus.ACTIVE ? "Your account is now Active" : "It appears you are yet to confirm your Phone Number. Once you do so on the App, your account will be activated.");
		Template template = velocityEngine.getTemplate("templates/email-confirmed.html");
	    VelocityContext model = new VelocityContext();
	    model.put("user", subscriber);
		model.put("appName", Config.APP_NAME);
		model.put("extraText", extraMessage);
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		emailService.sendEmail(subscriber.getEmail(), Config.EMAIL_CONFIRMED_MSG, Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
		return success(stringWriter.toString(), subscriber);
	}

	/**
	 * 
	 * This method is used to confirm a phone number
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse confirmPhoneNumber(ConfirmDataRq request) throws ApiException {
		String accessCode = request.accessCode;
		required("Access Code", accessCode);
		String phoneNumber = request.phoneNumber;
		required("Phone Number", phoneNumber);
		Subscriber subscriber = getSubscriber(request);
		ApiResponse response = modelApi.getActivationToken(subscriber, accessCode, ActivateTokenType.SMS);
		throwMsg(!hasData(response), "Token not found or has expired");
		
		ActivationToken activationToken = (ActivationToken) getData(response);
		
		subscriber.getTags().remove(SubscriberTag.PHONENUMBER_EXPIRED);
		response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not update subscriber account");
		
		subscriber = (Subscriber) getData(response);
		subscriber.setEntityStatus(EntityStatus.ACTIVE);
		//applyReferralWorth(subscriber);
		logger.debug("Activated subscriber and applied any pending wallet amount");
		
//		if(!subscriber.getTags().contains(SubscriberTag.EMAIL_EXPIRED)) {
//			subscriber.setEntityStatus(EntityStatus.ACTIVE);
//			applyReferralWorth(subscriber);
//			logger.debug("Activated subscriber and applied any pending wallet amount");
//		}
		
		response = modelApi.deleteActivationToken(Arrays.asList(activationToken.getId()));
		throwMsg(notOK(response), "Could not clear subscriber activation tokens");
		return success("Thank you for confirming your Phone Number. " + (subscriber.getEntityStatus() == EntityStatus.ACTIVE ? "You account is now Active" : "Once you confirm your Email Address, your account will be activated."), subscriber);
	}
	
	/**
	 * 
	 * This method is used to request email tokens
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse requestEmailToken(GenerateTokenRq request) throws ApiException {
		String email = request.username;
		required("Email", email);
		throwMsg(notEmail(email), "Invalid email");
		ApiResponse response = modelApi.getSubscriberByEmail(email);
		throwMsg(!hasData(response), "Subscriber not found with that email address");
		Subscriber subscriber = (Subscriber) getData(response);
		throwMsg(!subscriber.getTags().contains(SubscriberTag.EMAIL_EXPIRED), "Subscriber email confirmed already");
		response = modelApi.deleteActivationTokens(subscriber, ActivateTokenType.EMAIL);
		throwMsg(notOK(response), "Could not delete old tokens");
		String token = generateLongToken();
		ActivationToken activationToken = new ActivationToken(subscriber, token, ActivateTokenType.EMAIL);
		response = modelApi.putActivationToken(activationToken);
		throwMsg(!hasData(response), "Could not generate activation token");
		Template template = velocityEngine.getTemplate("templates/confirm-email.html");
	    VelocityContext model = new VelocityContext();
	    model.put("user", subscriber);
	    model.put("confirmationLink", host + "/public/person/email/confirm?accessCode=" + token);
		model.put("appName", Config.APP_NAME);
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		emailService.sendEmail(subscriber.getEmail(), Config.CONFIRM_EMAIL_MSG, Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
		return success("A confirmation code has been sent to your email account.", email);
	}
	
	@Autowired 
	HttpServletRequest request;
	
	@Autowired 
	AuthenticationManager authenticationProvider;
	
	@Value("${com.demus.local.password}")
	private String PASSWORD;
	
	/**
	 * 
	 * This method is a convinience method to apply all inactive referrals to subscriber account
	 * @param subscriber
	 * @return
	 * @throws ApiException
	 */
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR', 'SUPPORT') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	private Wallet applyReferralWorth (Subscriber subscriber) throws ApiException {
		required("Subscriber", subscriber);
//		String username = "admin@demusmayor.com";
//		String password = PASSWORD;
//		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
//	    token.setDetails(new WebAuthenticationDetails(request));
//		Authentication authentication = this.authenticationProvider.authenticate(token);
//		SecurityContextHolder.getContext().setAuthentication(authentication);
		Wallet wallet = getWallet(subscriber);
		ApiResponse response = modelApi.getRefer(subscriber.getPhoneNumber(), EntityStatus.INACTIVE);
		if (hasData(response)) {
			List<Refer> referrals = (List<Refer>) getListData(response);
			for (Refer refer : referrals) {
				WalletTransaction transaction = walletTransaction(subscriber, wallet, Math.abs(refer.getTransaction().getAmount()), String.format("Referral from wallet id %s worth Amount %s", refer.getSubscriber().getWallet().getWalletId(), priceFormat.format(refer.getTransaction().getAmount())), PaymentGateway.WALLET, refer.getId().toString(), WalletTransactionType.REFER, null);
				refer.setEntityStatus(EntityStatus.ACTIVE);
				throwMsg(!hasData(modelApi.putRefer(refer)), "Could not update referral");
				sendTransactionAlert(subscriber, wallet, transaction);
			}
		}
		return wallet;
	}
	
	private boolean notOK (ApiResponse response) {
		return response.status != ApiResponseStatus.OK;
	}
	
	private boolean notEmail (String email) {
		return !EmailValidator.getInstance().isValid(email);
	}

	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR', 'SUPPORT') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse requestSmsToken(GenerateTokenRq request) throws ApiException {
		String updatePhoneNumber = request.username;
		String phoneNumber = request.phoneNumber;
		required("Phone Number", phoneNumber);
		validatePhoneNumber(updatePhoneNumber);
		ApiResponse response = modelApi.getSubscriberByPhonenumber(phoneNumber, null);
		throwMsg(!hasData(response), "Subscriber not found with that phone number");
		Subscriber subscriber = (Subscriber) getData(response);
		throwMsg(!subscriber.getTags().contains(SubscriberTag.PHONENUMBER_EXPIRED), "Subscriber phone number confirmed already");
		response = modelApi.deleteActivationTokens(subscriber, ActivateTokenType.SMS);
		throwMsg(notOK(response), "Could not delete old tokens");
		String token = generateShortToken(5);
		
		if (smsServiceActive)
			smsService.send(updatePhoneNumber, "Dear Customer, your Demus Mayor code is: " + token + "", smsLabel);
		
		ActivationToken activationToken = new ActivationToken(subscriber, token, ActivateTokenType.SMS);
		response = modelApi.putActivationToken(activationToken);
		throwMsg(!hasData(response), "Could not generate activation token");
		subscriber.setPhoneNumber(updatePhoneNumber);
		throwMsg(!hasData(modelApi.putSubscriber(subscriber)), "Could not update subscriber");
		subscriber.getWallet().setWalletId(updatePhoneNumber);
		Wallet wallet = subscriber.getWallet();
		throwMsg(!hasData(modelApi.putWallet(wallet)), "Could not update subscriber wallet");
		return success("A confirmation code has been sent to your mobile phone", phoneNumber);
	}

	private String generateShortToken (int length) {
		return RandomStringUtils.randomNumeric(length);
	}
	
	private boolean notLive () {
		String env = environment.getActiveProfiles()[0];
		if (env.equals("test")) 
			return true;
		return false;
	}
	
	private String generateLongToken () {
		String token = null;
		if (notLive()) 
			 token = "2a57b6fc-867b-4a70-a366-f9beab1cb3a6";
		else
			token = UUID.randomUUID().toString();
		logger.debug(token);
		return token;
	}
	
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse register(RegisterationRq request) throws ApiException {
		validateRequest(request);
		
		String mobileNumber = request.mobileNumber;
		throwMsg(isNull(mobileNumber), "phone number is required");
		throwMsg(mobileNumber.length() != 11, "phone number must be 11 characters");
		throwMsg(modelApi.getSubscriberByPhonenumber(mobileNumber, null).size > 0 || modelApi.getWallet(mobileNumber, null).size > 0, "Phone number already registered to another subscriber");
		MobileNetworkOperator mobileNetworkOperator = request.mobileNetworkOperator;// MobileNetworkOperator.GENERIC;
		throwMsg(isNull(mobileNetworkOperator), "Mobile Network Operator is not valid");
		String firstName = request.firstName;
		throwMsg (isNull(firstName), "First name is required");
		throwMsg (notValidString(firstName, Config.MIN_NAME_SIZE, Config.MAX_NAME_SIZE), "First name must be between " + Config.MIN_NAME_SIZE + " and " + Config.MAX_NAME_SIZE + " characters");
		String lastName = request.lastName;
		throwMsg (isNull(lastName), "Last name is required");
		throwMsg (notValidString(firstName, Config.MIN_NAME_SIZE, Config.MAX_NAME_SIZE), "Last name must be between " + Config.MIN_NAME_SIZE + " and " + Config.MAX_NAME_SIZE + " characters");
		String email = request.email;
		throwMsg(isNull(email), "Email is required");
		throwMsg(!EmailValidator.getInstance().isValid(email), "Email is not valid");
		throwMsg(hasData(modelApi.getSubscriberByEmail(email)), "Email already exists");
		String password = request.password;
		throwMsg(isNull(password), "Password is required");
		throwMsg(notValidString(password, 6, 255), "Password must be a minimum of 6 characters long");
		String pin = "0000";//request.pin;
		throwMsg(isNull(pin), "PIN is required");
		throwMsg(notValidString(pin, 4, 4), "PIN must be a minimum of 4 digits long");
		String countryCode = request.countryCode;
		throwMsg(isNull(countryCode), "Country code is required");
		String facebookId = request.facebookId;
		String facebookApiSecret = request.facebookApiSecret;
		throwMsg(!isNull(facebookId) && isNull(facebookApiSecret), "Faceboook Id was supplied without Api Secret");
		String googleId = request.googleId;
		String googleApiSecret = request.googleApiSecret;
		throwMsg(!isNull(googleId) && isNull(googleApiSecret), "Google Id was supplied without Api Secret");
		String dob = request.dateOfBirth;
		Gender gender = request.gender;
		String bankAccountNumber = request.bankAccountNumber;
		String bankOperatorCode = request.bankOperatorCode;
		BankAccountType bankAccountType = request.bankAccountType;
		throwMsg(!isNull(bankAccountNumber) && isNull(bankOperatorCode), "Bank account number was supplied without the Bank Code");
		throwMsg(!isNull(bankAccountNumber) && isNull(bankAccountType), "Bank account number was supplied without the Bank Account Type CURRENT or SAVINGS");
		ApiResponse bankResponse = modelApi.getBankOperator(bankOperatorCode);
		throwMsg(!isNull(bankAccountNumber) && !hasData(bankResponse), "Bank operator code incorrect");
		// All validations passed: Save Subscriber, Create a Wallet, and map a bank if provided
		Subscriber subscriber = new Subscriber(mobileNumber, firstName, lastName, password, pin, email, gender, dob, facebookId, facebookApiSecret, googleId, googleApiSecret);
		subscriber.setEntityStatus(EntityStatus.INACTIVE);
		subscriber.getTags().add(SubscriberTag.PIN_EXPIRED);
		subscriber.getTags().add(SubscriberTag.PHONENUMBER_EXPIRED);
		subscriber.getTags().add(SubscriberTag.EMAIL_EXPIRED);
		ApiResponse response = modelApi.getRole("SUBSCRIBER");
		throwMsg(!hasData(response), "Could not get subscriber role to apply to account");
		Role role = (Role) getData(response);
		subscriber.setRole(role);
		subscriber.setPin(pin);//generateShortToken(4)
		if(notLive()) {
			subscriber.setEntityStatus(EntityStatus.ACTIVE);
			subscriber.getTags().remove(SubscriberTag.PIN_EXPIRED);
		}
		response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not create subscriber");
		subscriber = (Subscriber) getData(response);
		subscriber.setAuthCode(getAuthCode(email, password));
		// Wallet is initialized with subscriber phone number
		Wallet wallet = new Wallet(subscriber.getPhoneNumber(), subscriber, mobileNetworkOperator);
		response = modelApi.putWallet(wallet);		
		throwMsg(!hasData(response), "Could not save wallet");
		wallet = (Wallet) getData(response);
		subscriber.setWallet(wallet);
		if (!isNull(bankAccountNumber)) {
			BankOperator operator = (BankOperator) getData(bankResponse);
			BankAccount bankAccount = new BankAccount(bankAccountNumber, operator, subscriber, bankAccountType, true);
			modelApi.putBankAccount(bankAccount);
		}
		String clientId = request.clientId;
		DeviceOs clientOs = request.clientOs;
		Integer version = request.version;
		String pushNotificationKey = request.pushNotificationKey;
		response = modelApi.getDevice(clientId, clientOs);
		Device device = null;
		if (hasData(response)) {
			// If this device is already registered. Claim its ownership.
			device = (Device) getData(response);
			device.setSubscriber(subscriber);
			device.setEntityStatus(EntityStatus.ACTIVE);
			device.setPushNotificationKey(pushNotificationKey);
			//throwMsg(!hasData(modelApi.putDevice(device)), "Could not switch device owner");
		} else {
			device = new Device(clientId, clientOs, version, pushNotificationKey, subscriber);
		}
		
		response = modelApi.putDevice(device);
		throwMsg(!hasData(response), "Could not register the device");
		GenerateTokenRq generateTokenRq = new GenerateTokenRq();
		generateTokenRq.username = email;
		requestEmailToken(generateTokenRq);
		generateTokenRq.phoneNumber = mobileNumber;
		generateTokenRq.username = mobileNumber;
		requestSmsToken(generateTokenRq);
		Template template = velocityEngine.getTemplate("templates/welcome-registration.html");
	    VelocityContext model = new VelocityContext();
	    model.put("user", subscriber);
		model.put("wallet", wallet);
		model.put("appName", Config.APP_NAME);
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		emailService.sendEmail(subscriber.getEmail(), Config.WELCOME_MESSAGE, Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
		return success("Welcome to Demus Mayor. You'll receive a verification code to confirm your phone number and a verification mail to confirm your email. Thank you.", subscriber);
	}
	
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse register2(RegisterationRq request) throws ApiException {
		if(request.clientOs == null) {
			request.clientOs = DeviceOs.WEB;
		}
		
		validateRequest(request);
		String mobileNumber = request.mobileNumber;
		throwMsg(isNull(mobileNumber), "phone number is required");
		throwMsg(mobileNumber.length() != 11, "phone number must be 11 characters");
		throwMsg(modelApi.getSubscriberByPhonenumber(mobileNumber, null).size > 0 || modelApi.getWallet(mobileNumber, null).size > 0, "Phone number already registered to another subscriber");
		MobileNetworkOperator mobileNetworkOperator = MobileNetworkOperator.GENERIC;
		//throwMsg(isNull(mobileNetworkOperator), "Mobile Network Operator is not valid");
		String firstName = request.firstName;
		throwMsg (isNull(firstName), "First name is required");
		throwMsg (notValidString(firstName, Config.MIN_NAME_SIZE, Config.MAX_NAME_SIZE), "First name must be between " + Config.MIN_NAME_SIZE + " and " + Config.MAX_NAME_SIZE + " characters");
		String lastName = request.lastName;
		throwMsg (isNull(lastName), "Last name is required");
		throwMsg (notValidString(firstName, Config.MIN_NAME_SIZE, Config.MAX_NAME_SIZE), "Last name must be between " + Config.MIN_NAME_SIZE + " and " + Config.MAX_NAME_SIZE + " characters");
		String email = request.email;
		throwMsg(isNull(email), "Email is required");
		throwMsg(!EmailValidator.getInstance().isValid(email), "Email is not valid");
		throwMsg(hasData(modelApi.getSubscriberByEmail(email)), "Email already exists");
		String password = request.password;
		throwMsg(isNull(password), "Password is required");
		throwMsg(notValidString(password, 6, 255), "Password must be a minimum of 6 characters long");
		String pin = request.pin;
		throwMsg(isNull(pin), "PIN is required");
		throwMsg(notValidString(pin, 4, 4), "PIN must be a minimum of 4 digits long");
		String countryCode = request.countryCode;
		throwMsg(isNull(countryCode), "Country code is required");
		String facebookId = request.facebookId;
		String facebookApiSecret = request.facebookApiSecret;
		throwMsg(!isNull(facebookId) && isNull(facebookApiSecret), "Faceboook Id was supplied without Api Secret");
		String googleId = request.googleId;
		String googleApiSecret = request.googleApiSecret;
		throwMsg(!isNull(googleId) && isNull(googleApiSecret), "Google Id was supplied without Api Secret");
		String dob = request.dateOfBirth;
		Gender gender = request.gender;
		String bankAccountNumber = request.bankAccountNumber;
		String bankOperatorCode = request.bankOperatorCode;
		BankAccountType bankAccountType = request.bankAccountType;
		throwMsg(!isNull(bankAccountNumber) && isNull(bankOperatorCode), "Bank account number was supplied without the Bank Code");
		throwMsg(!isNull(bankAccountNumber) && isNull(bankAccountType), "Bank account number was supplied without the Bank Account Type CURRENT or SAVINGS");
		ApiResponse bankResponse = modelApi.getBankOperator(bankOperatorCode);
		throwMsg(!isNull(bankAccountNumber) && !hasData(bankResponse), "Bank operator code incorrect");
		// All validations passed: Save Subscriber, Create a Wallet, and map a bank if provided
		Subscriber subscriber = new Subscriber(mobileNumber, firstName, lastName, password, pin, email, gender, dob, facebookId, facebookApiSecret, googleId, googleApiSecret);
		subscriber.setEntityStatus(EntityStatus.INACTIVE);
		subscriber.getTags().add(SubscriberTag.PHONENUMBER_EXPIRED);
		subscriber.getTags().add(SubscriberTag.EMAIL_EXPIRED);
		
		ApiResponse response = modelApi.getRole("SUBSCRIBER");
		throwMsg(!hasData(response), "Could not get subscriber role to apply to account");
		Role role = (Role) getData(response);
		subscriber.setRole(role);
		subscriber.setPin(pin);//generateShortToken(4)
		
//		if(notLive()) {
//			subscriber.setEntityStatus(EntityStatus.ACTIVE);
//			subscriber.getTags().remove(SubscriberTag.PIN_EXPIRED);
//		}
		
		response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not create subscriber");
		subscriber = (Subscriber) getData(response);
		subscriber.setAuthCode(getAuthCode(email, password));
		// Wallet is initialized with subscriber phone number
		Wallet wallet = new Wallet(subscriber.getPhoneNumber(), subscriber, mobileNetworkOperator);
		response = modelApi.putWallet(wallet);		
		throwMsg(!hasData(response), "Could not save wallet");
		wallet = (Wallet) getData(response);
		subscriber.setWallet(wallet);
		if (!isNull(bankAccountNumber)) {
			BankOperator operator = (BankOperator) getData(bankResponse);
			BankAccount bankAccount = new BankAccount(bankAccountNumber, operator, subscriber, bankAccountType, true);
			modelApi.putBankAccount(bankAccount);
		}
		String clientId = request.clientId;
		DeviceOs clientOs = request.clientOs;
		Integer version = request.version;
		String pushNotificationKey = request.pushNotificationKey;
		response = modelApi.getDevice(clientId, clientOs);
		Device device = null;
		if (hasData(response)) {
			// If this device is already registered. Claim its ownership.
			device = (Device) getData(response);
			device.setSubscriber(subscriber);
			device.setEntityStatus(EntityStatus.ACTIVE);
			device.setPushNotificationKey(pushNotificationKey);
			//throwMsg(!hasData(modelApi.putDevice(device)), "Could not switch device owner");
		} else {
			device = new Device(clientId, clientOs, version, pushNotificationKey, subscriber);
		}
		
		response = modelApi.putDevice(device);
		throwMsg(!hasData(response), "Could not register the device");
		
		GenerateTokenRq generateTokenRq = new GenerateTokenRq();
		generateTokenRq.username = email;
		requestEmailToken(generateTokenRq);
		
		generateTokenRq.phoneNumber = mobileNumber;
		generateTokenRq.username = mobileNumber;
		requestSmsToken(generateTokenRq);
		
		Template template = velocityEngine.getTemplate("templates/welcome-registration.html");
	    VelocityContext model = new VelocityContext();
	    model.put("user", subscriber);
		model.put("wallet", wallet);
		model.put("appName", Config.APP_NAME);
		
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		
		emailService.sendEmail(subscriber.getEmail(), Config.WELCOME_MESSAGE, Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
		return success("Welcome to Demus Mayor. You'll receive a verification code to confirm your phone number and a verification mail to confirm your email. Thank you.", subscriber);
	}
	
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse register3(RegisterationRq request) throws ApiException {
		if(request.clientOs == null)
			request.clientOs = DeviceOs.WEB;
		
		validateRequest(request);
		String mobileNumber = request.mobileNumber;
		throwMsg(isNull(mobileNumber), "phone number is required");
		throwMsg(mobileNumber.length() != 11, "phone number must be 11 characters");
		throwMsg(modelApi.getSubscriberByPhonenumber(mobileNumber, null).size > 0 || modelApi.getWallet(mobileNumber, null).size > 0, "Phone number already registered to another subscriber");
		MobileNetworkOperator mobileNetworkOperator = MobileNetworkOperator.GENERIC;
		
		String firstName = request.firstName;
		throwMsg (isNull(firstName), "First name is required");
		throwMsg (notValidString(firstName, Config.MIN_NAME_SIZE, Config.MAX_NAME_SIZE), "First name must be between " + Config.MIN_NAME_SIZE + " and " + Config.MAX_NAME_SIZE + " characters");
		String lastName = request.lastName;
		throwMsg (isNull(lastName), "Last name is required");
		throwMsg (notValidString(firstName, Config.MIN_NAME_SIZE, Config.MAX_NAME_SIZE), "Last name must be between " + Config.MIN_NAME_SIZE + " and " + Config.MAX_NAME_SIZE + " characters");
		String email = request.email;
		throwMsg(isNull(email), "Email is required");
		throwMsg(!EmailValidator.getInstance().isValid(email), "Email is not valid");
		throwMsg(hasData(modelApi.getSubscriberByEmail(email)), "Email already exists");
		String password = request.password;
		throwMsg(isNull(password), "Password is required");
		throwMsg(notValidString(password, 6, 255), "Password must be a minimum of 6 characters long");
		String pin = request.pin;
		throwMsg(isNull(pin), "PIN is required");
		throwMsg(notValidString(pin, 4, 4), "PIN must be a minimum of 4 digits long");
		String countryCode = request.countryCode;
		throwMsg(isNull(countryCode), "Country code is required");
		
		// All validations passed: Save Subscriber, Create a Wallet, and map a bank if provided
		Subscriber subscriber = new Subscriber(mobileNumber, firstName, lastName, password, pin, email, null, null, null, null, null, null);
		subscriber.setEntityStatus(EntityStatus.ACTIVE);
		
		ApiResponse response = modelApi.getRole("SUBSCRIBER");
		throwMsg(!hasData(response), "Could not get subscriber role to apply to account");
		Role role = (Role) getData(response);
		subscriber.setRole(role);
		subscriber.setPin(pin);
		
		response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not create subscriber");
		subscriber = (Subscriber) getData(response);
		subscriber.setAuthCode(getAuthCode(email, password));
		
		// Wallet is initialized with subscriber phone number
		Wallet wallet = new Wallet(subscriber.getPhoneNumber(), subscriber, mobileNetworkOperator);
		response = modelApi.putWallet(wallet);		
		throwMsg(!hasData(response), "Could not save wallet");
		
		return success("Vendor account created successfully.", subscriber);
	}
	
	private WalletTransaction walletTransaction (Subscriber subscriber, Wallet wallet, Double amount, String description,  PaymentGateway gateway, String reference, WalletTransactionType type, WalletTransaction referenceTransaction) throws ApiException {
		throwMsg(wallet == null || amount == null, "Cannot create wallet transaction due to null wallet, or amount");
		Double oldBalance = wallet.getBalance();
		Double newBalance = oldBalance + amount;
		//wallet.setBalance(newBalance);
		walletCrud.updateBalance(wallet.getWalletId(), amount);
		ApiResponse response = null;
		WalletTransaction transaction = new WalletTransaction(generateTransactionId(subscriber), wallet, amount, newBalance, type, gateway, null, null, WalletTransactionStatus.COMPLETED, EntityStatus.ACTIVE);
		transaction.setDescription(description);
		transaction.setReference(reference);
		transaction.setReferenceTransaction(referenceTransaction);
		response = modelApi.putWalletTransaction(transaction);
		throwMsg(!hasData(response), "Could not create transaction for referral " + transaction.getId());
		return transaction;
	}
	
	private WalletTransaction walletTransaction (Subscriber subscriber, Wallet wallet, Double amount, String description,  PaymentGateway gateway, String reference, WalletTransactionType type, WalletTransaction referenceTransaction, String paymentAuthorizationToken) throws ApiException {
		throwMsg(wallet == null || amount == null, "Cannot create wallet transaction due to null wallet, or amount");
		Double oldBalance = wallet.getBalance();
		Double newBalance = oldBalance + amount;
		//wallet.setBalance(newBalance);
		walletCrud.updateBalance(wallet.getWalletId(), amount);
		ApiResponse response = null;
		WalletTransaction transaction = new WalletTransaction(generateTransactionId(subscriber), wallet, amount, newBalance, type, gateway, null, null, WalletTransactionStatus.COMPLETED, EntityStatus.ACTIVE, paymentAuthorizationToken);
		transaction.setDescription(description);
		transaction.setReference(reference);
		transaction.setReferenceTransaction(referenceTransaction);
		response = modelApi.putWalletTransaction(transaction);
		throwMsg(!hasData(response), "Could not create transaction for referral " + transaction.getId());
		return transaction;
	}
	
	private WalletTransaction walletTransaction2(Subscriber subscriber, Wallet wallet, Double amount, String description,  PaymentGateway gateway, String reference, WalletTransactionType type, String paystackRef, WalletTransaction referenceTransaction) throws ApiException {
		throwMsg(wallet == null || amount == null, "Cannot create wallet transaction due to null wallet, or amount");
		Double oldBalance = wallet.getBalance();
		Double newBalance = oldBalance + amount;
		//wallet.setBalance(newBalance);
		walletCrud.updateBalance(wallet.getWalletId(), amount);
		ApiResponse response = null;
		WalletTransaction transaction = new WalletTransaction(generateTransactionId(subscriber), wallet, amount, newBalance, type, gateway, null, null, WalletTransactionStatus.COMPLETED, paystackRef, EntityStatus.ACTIVE);
		transaction.setDescription(description);
		transaction.setReference(reference);
		transaction.setReferenceTransaction(referenceTransaction);
		response = modelApi.putWalletTransaction(transaction);
		throwMsg(!hasData(response), "Could not create transaction for referral " + transaction.getId());
		return transaction;
	}
	
	private Object getData (ApiResponse response) throws ApiException {
		List<Object> objects = ((List<Object>) getListData(response));
		return objects == null || objects.size() == 0 ? null : objects.get(0);
	}
	
	private Object getListData (ApiResponse response) throws ApiException {
		if (!hasData(response))
			return null;
		return response.results;
	}
	
	private boolean hasData (ApiResponse response) throws ApiException {
		if(isNull(response))
			return false;
		if (response.status == ApiResponseStatus.OK && response.size > 0)
			return true;
		throwMsg(response.status != ApiResponseStatus.OK, response.message);
		return false;
	}
	
	private void throwMsg (Boolean test, String message) throws ApiException {
		if (test)
			throw new ApiException (message);
	}
	
	private void throwInvalidState (Boolean test, String message) throws ApiException {
		if (test)
			throw new InvalidStateException (message);
	}
	
	private boolean isNull (Object object) {
		return object == null;
	}
	
	private boolean notValidString (String object, int min, int max) {
		if (isNull(object)) 
			return true;
		else if (object.length() > max || object.length() < min)
			return true;
		return false;
	}
	
	private void required (String title, Object object) throws ApiException {
		throwMsg(isNull(object), title + " is required.");
	}
	
	/**
	 * 
	 * This method puts a subscribers phone to inactive when logged out 
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse logoutUser(ApiRequest request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		ApiResponse response = modelApi.getDeviceClientId(request.clientId);
		throwMsg(!hasData(response), "Device client id not correct");
		Device device = (Device) getData(response);
		throwMsg(!device.getSubscriber().equals(subscriber), "Unauthorized request. You have to login with this device.");
		device.setEntityStatus(EntityStatus.INACTIVE);
		throwMsg(!hasData(modelApi.putDevice(device)), "Server error could not log you out");
		return success("Logout Successful. Thanks for choosing Demus Mayor", null);
	}
	
	/**
	 * 
	 * This method creates a subscription for a particular airtime product using the quantity as 
	 * the airtime value. 
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse recharge(RechargeRetailRq request) throws ApiException {
		if(request.clientOs == null) {
			request.clientOs = DeviceOs.WEB;
		}
		validateRequest(request);
		
		Subscriber subscriber = getSubscriber(request);
		throwCannotTransact(subscriber);
		MobileNetworkOperator mobileNetworkOperator = request.mobileNetworkOperator;
		required("Mobile Network", mobileNetworkOperator);
		throwMsg(!mobileNetworkOperator.name().equals("GLO"), "This service is not yet available. Thank you for choosing Demus Mayor.");
		String phoneNumber = request.receiverNumber;
		required("Phone number", phoneNumber);
		
		//Only check type here if it's web
		Integer type = 1;
		if(request.clientOs == DeviceOs.WEB) {
			type = request.type;
			required("Recharge type: 1 - Airtime and 2 - Data", type);
		}
		
		//response object
		ApiResponse response;
		
		String productCode = null;
		String operatorMain = "";
		switch (request.mobileNetworkOperator) {
			case GLO:
				productCode = GLO_CODE;
				operatorMain = "GLO";
				break;
			case AIRTEL:
				productCode = AIRTEL_CODE;
				operatorMain = "AIRTEL";
				break;
			case ETISALAT:
				productCode = ETISALAT_CODE;
				operatorMain = "9MOBILE";
				break;
			case VISAFONE:
				productCode = VISAFONE_CODE;
				operatorMain = "SMILE";
				break;
			case MTN:
				productCode = MTN_CODE;
				operatorMain = "MTN";
				break;
			case GENERIC:
				productCode = GENERIC_CODE;
				operatorMain = "GENERIC";
				break;
			default:
				productCode = ETISALAT_CODE;
				operatorMain = "9MOBILE";
				break;
		}
		required("Airtime product code", productCode);
		
		Integer quantity = request.amount;
		required("Amount", quantity);
		String pin = request.pin;
		required("Pin", pin);
		throwMsg(!subscriber.matchPin(pin), "Pin is incorrect");
		logger.debug("Api data validation passed");
		
		Product product = productCrud.findByProductCodeAndCategoryCategoryCode(productCode, "AIRTIME", EntityStatus.ACTIVE);
		throwMsg(product == null || !product.getEntityStatus().equals(EntityStatus.ACTIVE), "Product not available at the moment");
		throwMsg(quantity > product.getStockCount(), "Product sold out. Please try again later.");
		Integer minPurchaseQuantity =  product.getMinPurchaseQuantity();
		throwMsg(quantity < minPurchaseQuantity, "Minimum purchase quanity is " + minPurchaseQuantity);
		
		Double sellingPrice = 1.0;
		Double cost = quantity * sellingPrice;
		
		sellingPrice = product.getPrice();
		
		cost = quantity * sellingPrice;
		
		logger.debug("checking subscriber has enough amount in wallet");
		Wallet wallet = getWallet(subscriber);
		Double walletBalance = wallet.getBalance();
		throwMsg(walletBalance < cost, "You do not have sufficient balance to complete transaction.");
		logger.debug("now creating order");
		String orderId = UUID.randomUUID().toString();
		Order order = new Order(orderId, cost, OrderStatus.PENDING, request.clientOs, EntityStatus.ACTIVE, subscriber);
		response = modelApi.putOrder(order);
		throwMsg(!hasData(response), "Order could not be created");
		order = (Order) getData(response);
		logger.debug("now creating subscription");
		Subscription subscription = new Subscription(order, product, quantity, null, cost, EntityStatus.ACTIVE);
		response = modelApi.putSubscription(subscription);
		throwMsg(!hasData(response), "Subscription could not be created");
		String transactionId = generateTransactionId(subscriber);
		String transactionMessage = String.format("Airtime retail to %s with %s %s.", phoneNumber, operatorMain, quantity);
		WalletTransaction transaction = walletTransaction(subscriber, wallet, (-1) * cost, transactionMessage, PaymentGateway.WALLET, request.mobileNetworkOperator.toString(), WalletTransactionType.AIRTIME, null);
		wallet = walletCrud.findOne(wallet.getId());
		
		product.setStockCount(product.getStockCount() - quantity);
		response = modelApi.putProduct(product);
		throwMsg(!hasData(response), "Could not update stock count");
		DeliverOrderRq dRq = new DeliverOrderRq();
		dRq.subscription = subscription;
		dRq.phoneNumber = phoneNumber;
		dRq.walletTransaction = transaction;
		dRq.order = order;
	    
		VendorApiLog rp = vendorApi.deliverOrderRequest(dRq, operatorMain, type == 1 ? false : true, false);
		transaction.setReference(rp.getAuditId());
		
		try {
			modelApi.putWalletTransaction(transaction);
		} catch (Exception e) {
			logger.error("Could not log mobifin transaction", e);
		}
		
		//check if there's need for REFER-ring the value recipient
		if(modelApi.getSubscriberByPhonenumber(phoneNumber, null).size <= 0) {
			List<Refer> refers = (List<Refer>) getListData(modelApi.getRefer(phoneNumber, EntityStatus.INACTIVE));
			
			if(refers != null && refers.size() <= 0) {
				ReferSubscriberRq referSubscriberRq = new ReferSubscriberRq();
				referSubscriberRq.receiverPhoneNumber = phoneNumber;
				referSubscriberRq.amount = 0.00;
				referSubscriberRq.phoneNumber = request.phoneNumber;
				referSubscriberRq.pin = request.pin;
				referSubscriberRq.clientId = request.clientId;
				referSubscriberRq.clientOs = request.clientOs;
				referSubscriberRq.version = request.version;
				referSubscriberRq.clientSecret = request.clientSecret;
				referWithNoException(referSubscriberRq);
			}
		}		
		
		if(transaction.getDescription().contains("ETISALAT"))
			transaction.setDescription(transaction.getDescription().replace("ETISALAT", "9MOBILE"));
		
		Template template = velocityEngine.getTemplate("templates/confirm_order.html");
	    VelocityContext model = new VelocityContext();
	    model.put("user", subscriber);
		model.put("order", order);
		model.put("transaction", transaction);
		model.put("wallet", wallet);
		model.put("product", product);
		model.put("appName", Config.APP_NAME);
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		
	    sendTransactionAlert(subscriber, wallet, transaction);
		
	    String format = "Received %s %s %s from %s. Txn ref: %s. %s. Powered by Demus Mayor";
	    
	    String receivedFrom = wallet.getWalletId();
	    
	    String smsAlert = String.format(format, priceFormat.format(quantity * 1.00), operatorMain, "AIRTIME", receivedFrom, transaction.getTransactionId(), formatDate.format(transaction.getCreateDate()));
	    
	    smsService.send(phoneNumber, smsAlert, smsLabel);
	    
		return success(String.format("%s Your current wallet balance is %s. Txn #: %s", transactionMessage, transaction.getPrintBalance(), transactionId), transaction);
	}
	
	/**
	 * 
	 * This method is for vending airtime/data.
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	//@PreAuthorize ("hasAnyRole('ADMINISTRATOR') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse rechargeWeb(RechargeRetailWebRq request, boolean isTest) throws ApiException {
		request.clientOs = DeviceOs.WEB;
		
		//Inject ADMIN
		request.phoneNumber = PHONE;
		request.pin = PIN;
		
		validateRequest(request);
		
		Subscriber subscriber = getSubscriber(request);
		throwCannotTransact(subscriber);
		MobileNetworkOperator mobileNetworkOperator = request.mobileNetworkOperator;
		required("Mobile Network", mobileNetworkOperator);
		throwMsg(!mobileNetworkOperator.name().equals("GLO"), "This service is not yet available. Thank you for choosing Demus Mayor.");
		String phoneNumber = request.receiverNumber;
		required("Phone number", phoneNumber);
		String payStackTransactionId = request.payStackTransactionRef;
		required("Paystack transaction reference", payStackTransactionId);
		Integer quantity = request.amount;
		required("Amount", quantity);
		Integer type = request.type;
		required("Recharge type: 1 - Airtime and 2 - Data", type);
		
		//response object
		ApiResponse response;
		
		//Decline purchase if transaction ref is already used
		WalletTransaction transactionWithPaystackRef = walletTransactionCrud.findByPaystackTransactionRef(payStackTransactionId);
		throwMsg(transactionWithPaystackRef != null, "Transaction with Ref: " + payStackTransactionId + " has already been previously fulfilled.");
		
		//Verify Paystack transaction
		PaystackVerificationResponse paystackVerificationResponse = verifyPayStackTransaction(subscriber, request.payStackTransactionRef, isTest, 0);
		Double amount = Double.parseDouble(String.valueOf(paystackVerificationResponse.getAmount())) / 100.0;
		throwMsg(amount <= 0.0, "Invalid amount.");
		throwMsg(amount != Double.parseDouble(String.valueOf(quantity)), "Invalid amount, does not match.");
		
		String operatorMain = "";
		switch (request.mobileNetworkOperator) {
			case GLO:
				operatorMain = "GLO";
				break;
			case AIRTEL:
				operatorMain = "AIRTEL";
				break;
			case ETISALAT:
				operatorMain = "9MOBILE";
				break;
			case VISAFONE:
				operatorMain = "SMILE";
				break;
			case MTN:
				operatorMain = "MTN";
				break;
			case GENERIC:
				operatorMain = "GENERIC";
				break;
			default:
				operatorMain = "9MOBILE";
				break;
		}
		
		required("Amount", quantity);
		String pin = request.pin;
		required("Pin", pin);
		throwMsg(!subscriber.matchPin(pin), "Internal server error: pin");
		logger.info("Api data validation passed");
		
		Product product = productCrud.findByProductCodeAndCategoryCategoryCode("90009", "WEB", EntityStatus.ACTIVE);
		throwMsg(product == null || !product.getEntityStatus().equals(EntityStatus.ACTIVE), "Product not available at the moment");
		Integer minPurchaseQuantity =  product.getMinPurchaseQuantity();
		throwMsg(quantity < minPurchaseQuantity && !isTest, "Minimum purchase quantity is " + minPurchaseQuantity);
		
		Double sellingPrice = 1.0;
		Double cost = quantity * sellingPrice;
		sellingPrice = product.getPrice();
		cost = quantity * sellingPrice;
		
		Wallet wallet = getWallet(subscriber);
		
		logger.info("now creating order");
		String orderId = UUID.randomUUID().toString();
		Order order = new Order(orderId, cost, OrderStatus.PENDING, request.clientOs, EntityStatus.ACTIVE, subscriber);
		response = modelApi.putOrder(order);
		throwMsg(!hasData(response), "Order could not be created");
		order = (Order) getData(response);
		
		logger.info("now creating subscription");
		Subscription subscription = new Subscription(order, product, quantity, null, cost, EntityStatus.ACTIVE);
		response = modelApi.putSubscription(subscription);
		throwMsg(!hasData(response), "Subscription could not be created");
		
		String transactionId = generateTransactionId(subscriber);
		String transactionMessage = String.format((type == 1 ? "Airtime" : "Data") + " retail to %s with %s %s.", phoneNumber, operatorMain, quantity);
		WalletTransaction transaction = walletTransaction2(subscriber, wallet, (-1) * cost, transactionMessage, PaymentGateway.PAYSTACK, request.mobileNetworkOperator.toString(), WalletTransactionType.WEB_RECHARGE, payStackTransactionId, null);
		
		product.setStockCount(product.getStockCount() - quantity);
		response = modelApi.putProduct(product);
		throwMsg(!hasData(response), "Could not update stock count");
		
		DeliverOrderRq dRq = new DeliverOrderRq();
		dRq.subscription = subscription;
		dRq.phoneNumber = phoneNumber;
		dRq.walletTransaction = transaction;
		dRq.order = order;
	    
		VendorApiLog rp = vendorApi.deliverOrderRequest(dRq, operatorMain, type == 1 ? false : true, isTest);
		transaction.setReference(rp.getAuditId());
		
		try {
			modelApi.putWalletTransaction(transaction);
		} catch (Exception e) {
			logger.error("Could not log transaction", e);
		}
		
		if(transaction.getDescription().contains("ETISALAT"))
			transaction.setDescription(transaction.getDescription().replace("ETISALAT", "9MOBILE"));
		
	    String format = "Received %s %s %s from %s. Txn ref: %s. %s. Powered by Demus Mayor";
	    String receivedFrom = wallet.getWalletId();
	    String smsAlert = String.format(format, priceFormat.format(quantity * 1.00), operatorMain, (type == 1 ? "AIRTIME" : "DATA"), receivedFrom, transaction.getTransactionId(), formatDate.format(transaction.getCreateDate()));
	    smsService.send(phoneNumber, smsAlert, smsLabel);
	    
	    transaction.setBalanceAfterTransaction(0.0);
	    
		return success(String.format("%s Txn #: %s", transactionMessage, transactionId), transaction);
	}
	
	@Autowired
	private EmailService emailService;
	
	private void throwCannotTransact (Subscriber subscriber) throws ApiException {
		throwMsg(isNull(subscriber), "subscriber not found");
		throwInvalidState(subscriber.getTags().contains(SubscriberTag.PHONENUMBER_EXPIRED), "Phone number not confirmed");
		throwInvalidState(subscriber.getTags().contains(SubscriberTag.EMAIL_EXPIRED), "Email not confirmed");
		throwInvalidState(subscriber.getTags().contains(SubscriberTag.PIN_EXPIRED), "Your pin has expired");
		throwInvalidState(subscriber.getTags().contains(SubscriberTag.PASSWORD_EXPIRED), "Your password has expired");
	}
	
	//@TODO Implement user security
	private Subscriber getSubscriber (ApiRequest request) throws ApiException {
		String subscriberId = request.phoneNumber;
		return getSubscriber(subscriberId);
	}
	
	private Subscriber getSubscriber (String phoneNumber) throws ApiException {
		required("Subscriber Phone Number", phoneNumber);
		ApiResponse response = modelApi.getSubscriberByPhonenumber(phoneNumber, null);
		if (!hasData(response)) {
			response = modelApi.getSubscriberByEmail(phoneNumber);
			throwMsg(!hasData(response), "Subscriber not found");
		}
		return (Subscriber) getData(response);
	}
	
	private Subscriber matchSubscriber (String phoneNumber) throws ApiException {
		required("Subscriber Email or Phone Number", phoneNumber);
		ApiResponse response = modelApi.getSubscriberByPhonenumber(phoneNumber, null);
		if (!hasData(response)) {
			response = modelApi.getSubscriberByEmail(phoneNumber);
		} 
		throwMsg(!hasData(response), "Subscriber not found");
		return (Subscriber) getData(response);
	}
	
	private Wallet getWallet (Subscriber subscriber) throws ApiException {
		return getWallet(subscriber.getPhoneNumber());
	}
	
	@Override
	public ApiResponse listProducts(SearchFilterableRq request) throws ApiException {
		List<Product> products = null;
		products = productCrud.findAll();
		
		logger.debug("found " + products.size() + " matching products");
		return success("", products); 
	}

	@Override
	public ApiResponse listTransactions(DateFilterableRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		Integer page = request.page;
		required("Page", page);
		Integer size = request.size;
		required("Size", size);
		Date startDate = request.startDate;
		Date endDate = request.endDate;
		List<WalletTransaction> transactions = null;
		if (isNull(startDate) || isNull(endDate)) {
			transactions = walletTransactionCrud.findByWallet(subscriber.getWallet(), new PageRequest(page, size));
		}  else {
			logger.debug(request.endDate + " " + request.startDate);
			if (isNull(startDate)) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, -30);
				startDate = calendar.getTime();
			} 
			if (isNull(endDate))
				endDate = new Date();
			required("Start date", startDate);
			required("End date", endDate);
			ApiResponse response = modelApi.getWalletTransaction(subscriber, startDate, endDate, page, size);
			throwMsg(!hasData(response), "No transactions recorded on this account");
			transactions = (List<WalletTransaction>) getListData(response);
		}
		
		return success("", transactions);
	}
	
	@Override
	public ApiResponse listMessages(DateFilterableRq request) throws ApiException {
		validateRequest(request);
		List<PushMessage> pushMessages = null;
		
		pushMessages = pushCrud.findAll();
		
		throwMsg(pushMessages.isEmpty(), "No messages has been pushed yet");
		
		return success("", pushMessages);
	}

	/**
	 * Get transaction
	 */
	@Override
	public ApiResponse getTransaction(GetStringRq request) throws ApiException {
		validateRequest(request);
		required("request", request);
		String transactionId = request.id;
		required("Transaction Id", request.id);
		ApiResponse response = modelApi.getWalletTransaction(transactionId, null);
		throwMsg(!hasData(response), "transaction not found");
		
		return success("", getData(response));
	}
	
	private static final String INTERSWITCH_LOG_FORMAT = "ISW-%s";
	
	@Override
	public ApiResponse reverseDeposit (DepositRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request.walletId);
		String transactionToReverseRefrence = (String.format(INTERSWITCH_LOG_FORMAT, request.originalPaymentLogId));
		String logReference = String.format(INTERSWITCH_LOG_FORMAT, request.paymentLogId);
		ApiResponse response = modelApi.getWalletTransactionByRefrence(logReference);
		logger.debug(new Gson().toJson(request));
		WalletTransaction existingTransaction;
		if (hasData(response)) {
			return success("Duplicate transaction", getData(response));
		} else {
			existingTransaction = (WalletTransaction) getData(modelApi.getWalletTransactionByRefrence(transactionToReverseRefrence));
			throwMsg(existingTransaction == null, "No existing transaction to reverse");
		}
		Double amount = Math.abs(request.amount);
		Double transactionAmount = Math.abs(existingTransaction.getAmount());
		throwMsg(transactionAmount.compareTo(amount) != 0, "Payment log id " + request.paymentLogId + " not mapped to the right transaction value");
		Wallet wallet = getWallet(request.walletId);
		Double balance = wallet.getBalance() + request.amount;
		String transactionId = generateTransactionId(subscriber);
		// Used reversal amount typically minus
		walletCrud.updateBalance(request.walletId, request.amount);
		wallet = walletCrud.findByWalletId(request.walletId);
		logger.debug("Reversed amount on wallet for " + wallet.getWalletId());
		WalletTransaction walletTransaction = new WalletTransaction(transactionId, wallet, request.amount, balance, WalletTransactionType.REVERSAL, request.paymentGateway, null, null, WalletTransactionStatus.REVERSED, EntityStatus.ACTIVE);
		walletTransaction.setReference(String.format(INTERSWITCH_LOG_FORMAT, request.paymentLogId.toString()));
		walletTransaction.setDescription("Reversal of transaction " + existingTransaction.getTransactionId());
		response = modelApi.putWalletTransaction(walletTransaction);
		throwMsg(!hasData(response), "Could not create transaction");
		walletTransaction = (WalletTransaction) getData(response);
		logger.debug("Added reversal transaction for payment log id " + request.paymentLogId);
		walletTransaction = (WalletTransaction) getData(response);
		existingTransaction.setTransactionStatus(WalletTransactionStatus.REVERSED);
		response = modelApi.putWalletTransaction(existingTransaction);
		throwMsg(!hasData(response), "Could not update earlier transaction");
		logger.debug("Updated existing transaction status to reversed");
		sendTransactionAlert(subscriber, wallet, walletTransaction);
		return success("Payment reversed", walletTransaction);
	}
	
	@Autowired
	WalletCrud walletCrud;

	/**
	 * This method allows a subscriber create a credit transaction
	 * Creates a deposit transaction and wai2ts for payment gateway to authorize payment
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse deposit(DepositRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request.walletId);
		throwCannotTransact(subscriber);
		String refrence = String.format(INTERSWITCH_LOG_FORMAT, request.paymentLogId);
		ApiResponse response = modelApi.getWalletTransactionByRefrence(refrence);
		if (hasData(response)) {
			return success("Duplicate transaction", getData(response));
		}
		Double amount = request.amount;
		required("Amount", amount);
		PaymentGateway paymentGateway = request.paymentGateway;
		required("Payment Gateway", paymentGateway);
		String walletId = request.walletId;
		logger.debug("making a deposit for wallet id: " + walletId);
		Wallet wallet = getWallet(walletId);
		String displayAmount = priceFormat.format(amount);
		WalletTransaction walletTransaction = walletTransaction(subscriber, wallet, amount, String.format("Deposit via quickteller with ref: %s. Amount: %s", request.paymentLogId, displayAmount), paymentGateway, refrence, WalletTransactionType.QUICKTELLER, null);
		sendTransactionAlert(subscriber, wallet, walletTransaction);
		return success("", walletTransaction);
	}
	
	/**
	 * Method for listing a subscribers card auth tokens
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("(#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse listCardAuthTokens(ApiRequest request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		ApiResponse response = modelApi.getPaystackAuthCodes(subscriber);
		throwMsg(!hasData(response), "Subscriber has no cards stored");
		List<PaystackAuthorizationToken> authorizationCardTokens = (List<PaystackAuthorizationToken>) getListData(response);
		return success("Authorization card tokens successfully retrieved", authorizationCardTokens);
	}
	
	/**
	 * This method allows a subscriber create a credit transaction
	 * Creates a deposit transaction by charging an authorization code through paystack's API
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse depositPayStackAuth(PayStackDepositRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		throwCannotTransact(subscriber);
		
		String pin = request.pin;
		required("Pin", pin);
		throwMsg(!subscriber.matchPin(pin), "Pin is incorrect");
		
		long authId = request.authId;
		throwMsg(authId <= 0, "Invalid auth Id.");
		
		PaystackAuthorizationToken paystackAuthorizationToken = paystackAuthorizationTokenCrud.findById(authId);
		
		Integer amount = (int) (request.amount * 100);
		required("Amount", amount);
		throwMsg(amount <= 0.0, "Invalid amount.");
		
		PaystackAuthCodeChargeResponse paystackAuthCodeChargeResponse = chargeAuthorizationToken(paystackAuthorizationToken.getAuthorizationCode(), subscriber.getEmail(), amount, 0);
		if(paystackAuthCodeChargeResponse.getIsSuccess()) {
			PaymentGateway paymentGateway = PaymentGateway.PAYSTACK;
			required("Payment Gateway", paymentGateway);
			Wallet wallet = getWallet(subscriber);
			logger.debug("Making a paystack deposit for wallet id: " + wallet.getWalletId());
			String displayAmount = priceFormat.format(request.amount);
			String reference = paystackAuthCodeChargeResponse.getReference();
			WalletTransaction walletTransaction = walletTransaction(subscriber, wallet, request.amount, String.format("Deposit via Paystack with ref: %s. Amount: %s", reference, displayAmount), paymentGateway, reference, WalletTransactionType.PAYSTACK, null, paystackAuthorizationToken.getAuthorizationCode());
			sendTransactionAlert(subscriber, wallet, walletTransaction);
			return success("", walletTransaction);
		}
		else
			return success("Could not complete transaction.", null);
	}
	
	/**
	 * This method allows a subscriber create a credit transaction
	 * Creates a deposit transaction by charging last used authorization code through paystack's API
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse depositPayStackAuthLastUsedCardToken(PayStackDepositRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		throwCannotTransact(subscriber);
		
		String pin = request.pin;
		required("Pin", pin);
		throwMsg(!subscriber.matchPin(pin), "Pin is incorrect");
		
		Wallet wallet = getWallet(subscriber);
		WalletTransaction wTransaction = walletTransactionCrud.findTop1ByWalletAndPaymentGatewayOrderByIdDesc(wallet, PaymentGateway.PAYSTACK);
		
		PaystackAuthorizationToken paystackAuthorizationToken;
		if(wTransaction == null)
			paystackAuthorizationToken = paystackAuthorizationTokenCrud.findTop1BySubscriberOrderByIdDesc(subscriber);
		else {
			if(wTransaction.getPaymentAuthorizationToken() == null || wTransaction.getPaymentAuthorizationToken().isEmpty())
				paystackAuthorizationToken = paystackAuthorizationTokenCrud.findTop1BySubscriberOrderByIdDesc(subscriber);
			else
				paystackAuthorizationToken = paystackAuthorizationTokenCrud.findByAuthorizationCode(wTransaction.getPaymentAuthorizationToken());
		}
		
		throwMsg(paystackAuthorizationToken == null, "Pay with your card on Paystack channel to activate FAST LOAD.");
		
		Integer amount = (int) (request.amount * 100);
		required("Amount", amount);
		throwMsg(amount <= 0.0, "Invalid amount.");
		
		PaystackAuthCodeChargeResponse paystackAuthCodeChargeResponse = chargeAuthorizationToken(paystackAuthorizationToken.getAuthorizationCode(), subscriber.getEmail(), amount, 0);
		if(paystackAuthCodeChargeResponse.getIsSuccess()) {
			PaymentGateway paymentGateway = PaymentGateway.PAYSTACK;
			required("Payment Gateway", paymentGateway);
		    
			logger.debug("Making a paystack deposit for wallet id: " + wallet.getWalletId());
			
			String displayAmount = priceFormat.format(request.amount);
			String reference = paystackAuthCodeChargeResponse.getReference();
			WalletTransaction walletTransaction = walletTransaction(subscriber, wallet, request.amount, String.format("Deposit via Paystack with ref: %s. Amount: %s", reference, displayAmount), paymentGateway, reference, WalletTransactionType.PAYSTACK, null);
			sendTransactionAlert(subscriber, wallet, walletTransaction);
			return success("", walletTransaction);
		}
		else
			return success("Could not complete transaction." + paystackAuthorizationToken.getAuthorizationCode(), null);
	}
	
	/**
	 * This method allows a subscriber create a credit transaction
	 * Creates a deposit transaction by verifying through paystack's API
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse depositPayStack(PayStackDepositRq request) throws ApiException {
		validateRequest(request);
		
		Subscriber subscriber = getSubscriber(request);
		throwCannotTransact(subscriber);
		
		String refrence = request.tranxRef;
		ApiResponse response = modelApi.getWalletTransactionByRefrence(refrence);
		if (hasData(response))
			return success("Duplicate transaction", getData(response));
		
		PaystackVerificationResponse paystackVerificationResponse = verifyPayStackTransaction(subscriber, refrence, false, 0);
		Double amount = Double.parseDouble(String.valueOf(paystackVerificationResponse.getAmount())) / 100.0;
		required("Amount", amount);
		throwMsg(amount <= 0.0, "Invalid reference code.");
		
		PaymentGateway paymentGateway = PaymentGateway.PAYSTACK;
		required("Payment Gateway", paymentGateway);
		
		Wallet wallet = getWallet(subscriber);
		logger.debug("Making a paystack deposit for wallet id: " + wallet.getWalletId());
		
		String displayAmount = priceFormat.format(amount);
		WalletTransaction walletTransaction = walletTransaction(subscriber, wallet, amount, String.format("Deposit via Paystack with ref: %s. Amount: %s", request.tranxRef, displayAmount), paymentGateway, refrence, WalletTransactionType.PAYSTACK, null, paystackVerificationResponse.getAuthCode());
		sendTransactionAlert(subscriber, wallet, walletTransaction);
		return success("", walletTransaction);
	}
	
	private PaystackVerificationResponse verifyPayStackTransaction(Subscriber subscriber, String tranxRef, boolean isTest, int tries) {
		PaystackVerificationResponse paystackVerificationResponse = new PaystackVerificationResponse();
		paystackVerificationResponse.setAmount(0);
		
		Response response = null;
		
		try {
			OkHttpClient client = new OkHttpClient();
			
			HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.paystack.co/transaction/verify/" + tranxRef).newBuilder();
			String url = urlBuilder.build().toString();
			
			logger.debug("PayStack URL: " + url);

			Request request = new Request.Builder()
					.url(url)
					.header("Authorization", "Bearer " + (isTest ? payStackSecretTest : payStackSecret))
					.build();
			
			response = client.newCall(request).execute();
			
			logger.debug("PayStack Response Code: " + response.code());
	    	logger.debug("PayStack Response Message: " + response.message());
	    	logger.debug("PayStack Response Body: " + response.body());
			
			if(response.code() == 200) {
				String jsonObj = response.body().string();
				
				JSONParser parser = new JSONParser();
	            JSONObject jsonObject = (JSONObject) parser.parse(jsonObj);
	           
	            logger.debug("Raw PayStack Response: " + jsonObj);
	           
	            JSONObject dataObject = (JSONObject) jsonObject.get("data");
	           
	            if (((String) dataObject.get("status")).equals("success")) {
	            		paystackVerificationResponse.setAmount(((Long) dataObject.get("amount")).longValue());
	            		
	            		//save transaction ref
	            		PaystackAuthorizationToken paystackAuthorizationToken = new PaystackAuthorizationToken();
	        	   		
	        	   		//save authorization object for subscriber
	        	   		JSONObject authObject = (JSONObject) dataObject.get("authorization");
	        	   		
	        	   		paystackVerificationResponse.setAuthCode((String) authObject.get("authorization_code"));
	        	   		
	        	   		if((Boolean) authObject.get("reusable")) {
		        	   		paystackAuthorizationToken.setAuthorizationCode((String) authObject.get("authorization_code"));
		        	   		paystackAuthorizationToken.setCardType((String) authObject.get("card_type"));
		        	   		paystackAuthorizationToken.setLastFour((String) authObject.get("last4"));
		        	   		paystackAuthorizationToken.setExpMonth((String) authObject.get("exp_month"));
		        	   		paystackAuthorizationToken.setExpYear((String) authObject.get("exp_year"));
		        	   		paystackAuthorizationToken.setBin((String) authObject.get("bin"));
		        	   		paystackAuthorizationToken.setBank((String) authObject.get("bank"));
		        	   		paystackAuthorizationToken.setBrand((String) authObject.get("brand"));
		        	   		paystackAuthorizationToken.setChannel((String) authObject.get("channel"));
		        	   		paystackAuthorizationToken.setSignature((String) authObject.get("signature"));
		        	   		paystackAuthorizationToken.setReusable((Boolean) authObject.get("reusable"));
		        	   		paystackAuthorizationToken.setCountryCode((String) authObject.get("country_code"));
		        	   		paystackAuthorizationToken.setSubscriber(subscriber);
		        	   		paystackAuthorizationToken.setDateCreated(Calendar.getInstance(TimeZone.getTimeZone("Africa/Lagos")).getTime());
		        	   		
		        	   		//insert/save into DB
		        	   		paystackAuthorizationTokenCrud.save(paystackAuthorizationToken);
	        	   		}
	        	   			
	            }
			}
		}
		catch(Exception e) {
		    	final StringWriter sw = new StringWriter();
	    		final PrintWriter pw = new PrintWriter(sw, true);
		    e.printStackTrace(pw);
		    
		    String err = sw.getBuffer().toString();
		    
	    		logger.debug("Could not complete paystack transaction verification: " + e.getMessage() + " > " + err);
			
	        	if (tries <= MAX_CONNECTIONS)
	            verifyPayStackTransaction(subscriber, tranxRef, isTest, tries + 1);
	    }
		finally {
			if(response != null)
				response.body().close();
		}
		
		return paystackVerificationResponse;
	}
	
	private PaystackAuthCodeChargeResponse chargeAuthorizationToken(String authCode, String email, long amount, int tries) {
		Response response = null;
		
		PaystackAuthCodeChargeResponse paystackAuthCodeChargeResponse = new PaystackAuthCodeChargeResponse();
		
		try {
			OkHttpClient client = new OkHttpClient();
			
			HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.paystack.co/transaction/charge_authorization").newBuilder();
			String url = urlBuilder.build().toString();
			
			logger.debug("PayStack Auth Charge URL: " + url);
			
			final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
			String strJsonBody = "{" +
					                  	"\"authorization_code\": \"" + authCode + "\"," +
					                  	"\"email\": \"" + email + "\"," +
					                  	"\"amount\": " + amount + "" +
					             "}";
			RequestBody body = RequestBody.create(JSON, strJsonBody);

			Request request = new Request.Builder()
										.url(url)
										.header("Authorization", "Bearer " + payStackSecret)
										.post(body)
										.build();
			
			response = client.newCall(request).execute();
			
			logger.debug("PayStack Auth Charge Response Code: " + response.code());
	    	    logger.debug("PayStack Auth Charge Response Message: " + response.message());
	    	    logger.debug("PayStack Auth Charge Response Body: " + response.body());
			
			if(response.code() == 200) {
				String jsonObj = response.body().string();
				
				JSONParser parser = new JSONParser();
	            JSONObject jsonObject = (JSONObject) parser.parse(jsonObj);
	           
	            logger.debug("Raw PayStack Auth Charge Response: " + jsonObj);
	           
	            JSONObject dataObject = (JSONObject) jsonObject.get("data");
	           
	            if (((String) dataObject.get("status")).equals("success")) {
	        	   		paystackAuthCodeChargeResponse.setIsSuccess(true);
	        	   		paystackAuthCodeChargeResponse.setReference((String) dataObject.get("reference"));
	            }
			}
		}
		catch(Exception e) {
		    	final StringWriter sw = new StringWriter();
	    		final PrintWriter pw = new PrintWriter(sw, true);
		    e.printStackTrace(pw);
		    
		    String err = sw.getBuffer().toString();
		    
	    		logger.debug("Could not complete paystack auth charge: " + e.getMessage() + " > " + err);
			
	        	if (tries <= MAX_CONNECTIONS)
	        		chargeAuthorizationToken(authCode, email, amount, tries + 1);
	    }
		finally {
			if(response != null)
				response.body().close();
		}
		
		return paystackAuthCodeChargeResponse;
	}
	
	/**
	 * This method allows a subscriber create a credit transaction
	 * Creates a deposit transaction by verifying through paystack's API
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse depositEcleverPin(EcleverPinDepositRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		throwCannotTransact(subscriber);
		
		String cardPin = request.cardPin;
		ApiResponse response = modelApi.getWalletTransactionByRefrence(cardPin);
		
		if (hasData(response)) {
			return success("Duplicate transaction", getData(response));
		}
		
		if(request.redeemType.equals("AIRTIME")) {
			String[] returnedValues = redeemCardPin(request, subscriber.getPhoneNumber(), 0);
			
			Double amount = Double.parseDouble(returnedValues[0]);
			
			required("Amount", amount);
			throwMsg(amount <= 0.0, returnedValues[1]);
			
			PaymentGateway paymentGateway = PaymentGateway.ECLEVER_PIN;
			required("Payment Gateway", paymentGateway);
			
			Wallet wallet = getWallet(subscriber);
			logger.debug("Making a paystack deposit for wallet id: " + wallet.getWalletId());
			
			String displayAmount = priceFormat.format(amount);
			WalletTransaction walletTransaction = walletTransaction(subscriber, wallet, amount, String.format("Deposit via Eclever Card with ref: %s. Amount: %s", cardPin, displayAmount), paymentGateway, cardPin, WalletTransactionType.ECLEVER_PIN, null);
			
			sendTransactionAlert(subscriber, wallet, walletTransaction);
			
			return success("Eclever PIN redeemed successfully.", walletTransaction);
		}
		else
			return success("Error: Redeem type is currently unavailable.", null);
	}
	
	private String[] redeemCardPin(EcleverPinDepositRq req, String msisdn, int tries) {
		String[] returnedValues = new String[2];
		
		Double amount = 0.0;
		String message = "An error occured while trying to redeem PIN";
		
		Response response = null;
		
		try {
			final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
			
			OkHttpClient client = new OkHttpClient();
			
			HttpUrl.Builder urlBuilder = HttpUrl.parse("http://dev.eclevermobile.com:8080/agents/redeem").newBuilder();
			String url = urlBuilder.build().toString();
			
			logger.debug("Eclever-Card-PIN URL: " + url);
			
			String strJsonBody = "{" +
					                  "\"pin\": \"" + req.cardPin + "\"," +
					                  "\"msisdn\": \"" + msisdn + "\"," +
					                  "\"transactionId\": \"" + UUID.randomUUID().toString() + "\"," +
					                  "\"channelId\": \"" + req.redeemType + "\"" +
					             "}";
			
			RequestBody body = RequestBody.create(JSON, strJsonBody);

			Request request = new Request.Builder()
					.url(url)
					.post(body)
					.header("X-Authorization", "DML:5754add6-716b-4d23-b209-0db51ba770e7")
					.build();
			
			response = client.newCall(request).execute();
			
			logger.debug("Eclever-Card-PIN Response Code: " + response.code());
	    	    logger.debug("Eclever-Card-PIN Response Message: " + response.message());
	    	    logger.debug("Eclever-Card-PIN Response Body: " + response.body());
			
			if(response.code() == 200) {
				String jsonObj = response.body().string();
				
				JSONParser parser = new JSONParser();
	            JSONObject jsonObject = (JSONObject) parser.parse(jsonObj);
	            
	            message = ((String) jsonObject.get("message"));
	           
	            logger.debug("Raw Eclever-Card-PIN Response: " + jsonObj);
	            
	            if (((String) jsonObject.get("code")).equals("00")) {
	            		JSONObject dataObject = (JSONObject) jsonObject.get("data");
	            		
	        	   		amount = (Double) dataObject.get("value");
	            }
			}
		}
		catch(Exception e) {
		    	final StringWriter sw = new StringWriter();
	    		final PrintWriter pw = new PrintWriter(sw, true);
		    e.printStackTrace(pw);
		    
		    String err = sw.getBuffer().toString();
		    
	    		logger.debug("Could not complete eclever pin transaction verification: " + e.getMessage() + " > " + err);
			
	        	if (tries <= MAX_CONNECTIONS)
	            redeemCardPin(req, msisdn, tries + 1);
	    }
		finally {
			if(response != null)
				response.body().close();
		}
		
		returnedValues[0] = String.valueOf(amount);
		returnedValues[1] = message;
		
		return returnedValues;
	}
	
	private void sendTransactionAlert (Subscriber subscriber, Wallet wallet, WalletTransaction transaction) throws ApiException {
		if(transaction.getDescription().contains("ETISALAT"))
			transaction.setDescription(transaction.getDescription().replace("ETISALAT", "9MOBILE"));
		
		Template template = velocityEngine.getTemplate("templates/transaction.html");
	    VelocityContext model = new VelocityContext();
	    model.put("user", subscriber);
		model.put("wallet", wallet);
		model.put("transaction", transaction);
		model.put("appName", Config.APP_NAME);
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		emailService.sendEmail(subscriber.getEmail(), "Transaction Alert: " + (transaction.isCredit() ? "CREDIT" : "DEBIT"), Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
	}
	
	private Wallet getWallet(String walletId) throws ApiException {
		ApiResponse response = modelApi.getWallet(walletId, null);
		throwMsg(!hasData(response), "Wallet not found");
		return (Wallet) getData(response);
	}
	
	@Override
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR', 'SUPPORT') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse checkBalance(ApiRequest request) throws ApiException {
		validateRequest(request);
		/**String pin = getPin(request);
		throwMsg(!pin.equals(subscriber.getPin()), "Authorization pin not valid");**/
		Subscriber subscriber = getSubscriber(request);
		Wallet wallet = getWallet(subscriber);
		logger.debug("Wallet returned");
		return success("Successfully retrieved your wallet balance", wallet);
	}
	
	/**
	 * This method is a convenience method to get the pin include in an authenticated request
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@SuppressWarnings("unused")
	private String getPin (AuthenticatedRq request) throws ApiException {
		String pin = request.pin;
		required("Pin", pin);
		return pin;
	}
	
	private Double getCashBackCharge (Double amount) {
		Double charge = 0.00;
		platformConfiguration = platformConfigurationCrud.findAll().get(0);
		
		if (amount >= platformConfiguration.getMinimumAmountForCashBackFixedCharge() && amount <= platformConfiguration.getMaximumAmountForCashBackFixedCharge()) {
			charge = platformConfiguration.getCashBackFixedCharge();
			logger.debug("Applying fixed charge");
		}		
		else if (amount >= platformConfiguration.getMinimumAmountForCashbackPercentageCharge() && amount <= platformConfiguration.getMaximumAmountForCashbackPercentageCharge()) {
			charge = (amount * (platformConfiguration.getCashBackPercentageCharge()));
			logger.debug("Applying percentage amount to become " + amount);
		}
		
		return charge;
	}

	/**
	 * Subscriber must have sufficient funds in account to into bank wallet
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("(#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse updatePrimaryBank(BankAccountRq request) throws ApiException {
		validateRequest(request);
		
		Subscriber subscriber = getSubscriber(request);
		throwCannotTransact(subscriber);
		String pin = request.pin;
		required("Pin", pin);
		throwMsg(notValidPin(subscriber, pin), "Pin is incorrect");
		String bankOperatorCode = request.bankOperatorCode;
		required("Bank", bankOperatorCode);
		String accountNumber = request.accountNumber;
		required("Account Number", accountNumber);
		required("Primary account?", request.isPrimary);
		//account type
		BankAccountType bankAccountType = request.bankAccountType;
		required("Bank Account Type CURRENT OR SAVINGS", bankAccountType);
		
		if(request.isPrimary) {
			ApiResponse response = modelApi.getBankAccount(subscriber);
			List<BankAccount> bankAccounts = (List<BankAccount>) getListData(response);
			
			if(bankAccounts != null) {
				for (BankAccount bankAccount : bankAccounts) {
					bankAccount.setIsPrimary(false);
					modelApi.putBankAccount(bankAccount);
				}
			}
		}
		
		ApiResponse response = modelApi.getBankOperator(bankOperatorCode);
		throwMsg(!hasData(response), "Bank Operator not found");
		BankOperator operator = (BankOperator) getData(response);
		response = modelApi.getBankAccount(operator, accountNumber, subscriber);
		BankAccount bankAccount = null;
		
		String msg = "";
		if (hasData(response)) {
			bankAccount = (BankAccount) getData(response);
			bankAccount.setIsPrimary(request.isPrimary);
			bankAccount.setType(bankAccountType);
			msg = "Bank account updated successfully.";
		}
		else {
			bankAccount = new BankAccount(accountNumber, operator, subscriber, bankAccountType, request.isPrimary);
			msg = "Bank account added successfully!";
		}
		
		logger.debug(msg);
		modelApi.putBankAccount(bankAccount);
		
		response = modelApi.getBankAccount(subscriber);
		List<BankAccount> bankAccounts = (List<BankAccount>) getListData(response);
		
		return success(msg, bankAccounts);
	}

	@Override
	public ApiResponse listBanks(SearchFilterableRq request) throws ApiException {
		validateRequest(request);
		ApiResponse response = modelApi.bankOperators(new Filter(null, EntityStatus.ACTIVE, 0, 1000));
		throwMsg(!hasData(response), "Demus Mayor has not registered any Bank Accounts");
		List<BankOperator> operators = (List<BankOperator>) getListData(response);
		return success("Banks successfully retrieved", operators);
	}	

	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("(#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse listMyBankAccounts(ApiRequest request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		ApiResponse response = modelApi.getBankAccount(subscriber);
		throwMsg(!hasData(response), "Demus Mayor has not registered any Bank Accounts");
		List<BankAccount> operators = (List<BankAccount>) getListData(response);
		return success("Bank accounts successfully retrieved", operators);
	}

	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("(#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse changePassword(ChangePasswordRq request) throws ApiException {
		validateRequest(request);
		String oldPassword = request.oldPassword;
		required("Old Password", oldPassword);
		String newPassword = request.newPassword;
		required("New Password", newPassword);
		String confirmPassword = request.confirmPassword;
		required("Confirm Password", confirmPassword);
		Subscriber subscriber = getSubscriber(request);
		throwMsg(!subscriber.matchPassword(oldPassword), "Old password is incorrect");
		throwMsg(!newPassword.equals(confirmPassword), "New password does not match confirmation");
		throwMsg(newPassword.length() < 6, "Password must be at least 6 characters");
		subscriber.setPassword(newPassword);
		subscriber.getTags().remove(SubscriberTag.PASSWORD_EXPIRED);
		ApiResponse response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Server could not update your account");
		sendGeneralMessage(subscriber, "Password change", "Your password change request was successful at " + new Date().toString());
		return success("", getData(response));
	}
	
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("(#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse changePin (SetPinRq request) throws ApiException {
		validateRequest(request);
		String oldPin = request.oldPin;
		required("Old Pin", oldPin);
		String pin = request.pin;
		validatePin(pin);
		Subscriber subscriber = getSubscriber(request);
		//throwMsg(!oldPin.equals(subscriber.getPin()), "Old pin is incorrect");
		throwMsg(!subscriber.matchPin(oldPin), "Old pin is incorrect");
		subscriber.setPin(pin);
		subscriber.getTags().remove(SubscriberTag.PIN_EXPIRED);
		ApiResponse response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not update your account");
		subscriber = (Subscriber) getData(response);
		sendGeneralMessage(subscriber, "Pin change", "Your pin change request was successful at " + new Date().toString());
		return success("You have successfully changed your 4-digit Secret PIN.", subscriber);
	}
	
	private void validatePin (String pin) throws ApiException {
		required("Pin", pin);
		try {
			Integer.parseInt(pin);
		} catch (Exception e) {
			throwMsg(true, "Pin can only contain numbers");
		}
		throwMsg(pin.toString().length() != 4, "Pin must be 4 numbers");
		char[] numbers = pin.toString().toCharArray(); 
		boolean allSame = true;
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] != numbers[0])
				allSame = false;
		}
		throwMsg(allSame, "Please choose a secure Pin. All numbers cannot be the same.");
	}
	
	/**
	 * Client needs to check that provided password is equal to Basic Authentication password
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR', 'SUPPORT') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse resetPin(ResetPinRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		String newPin = request.newPin;
		if (newPin == null) {
			newPin = generateShortToken(4);
			subscriber.setPin(newPin);
			subscriber.getTags().add(SubscriberTag.PIN_EXPIRED);
			smsService.send(subscriber.getPhoneNumber(), "Your EcleverMobile temporary PIN is: " + newPin, smsLabel);
			sendGeneralMessage(subscriber, "PIN Reset", "Your EcleverMobile temporary PIN is: " + newPin + ". Do change this once you log into your account.");
		} else {
			validatePin(newPin);
			subscriber.getTags().remove(SubscriberTag.PIN_EXPIRED);
			subscriber.setPin(newPin);
		}
		ApiResponse response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not update your acccount");
		subscriber = (Subscriber) getData(response);
		return success("Pin reset successfully. Please check your email or phone for new Pin", subscriber);
	}
	
	/**
	 * Client needs to check that provided password is equal to Basic Authentication password
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR', 'SUPPORT') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse resetPassword(ResetPasswordRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		String newPassword = new RandomString(8).nextString();
		subscriber.setPassword(newPassword);
		subscriber.getTags().add(SubscriberTag.PASSWORD_EXPIRED);
		ApiResponse response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not update your acccount");
		subscriber = (Subscriber) getData(response);
		sendGeneralMessage (subscriber, "Password Reset", "Your temporary EcleverMobile password is : <b>" + newPassword + "</b>");
		return success("Password reset successfully", newPassword);
	}
	
	private void sendGeneralMessage (Subscriber user, String subject, String message) throws ApiException {
		Template template = velocityEngine.getTemplate("templates/general.html");
	    VelocityContext model = new VelocityContext();
	    model.put("user", user);
		model.put("appName", Config.APP_NAME);
		model.put("message", message);
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		emailService.sendEmail(user.getEmail(), subject, Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
	}

	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR', 'SUPPORT')")
	public ApiResponse migratePhoneNumber(MigratePhoneNumberRq request) throws ApiException {
		validateRequest(request);
		String pin = request.pin;
		Subscriber subscriber = getSubscriber(request);
		throwMsg(notValidPin(subscriber, pin), "Pin is incorrect");
		String phoneNumber = request.newPhoneNumber;
		required("New Phone Number", phoneNumber);
		MobileNetworkOperator mobileNetworkOperator = request.mobileNetworkOperator;
		required("Mobile Network Operator", mobileNetworkOperator);
		phoneNumber = phoneNumber.trim();
		Wallet wallet = getWallet(subscriber);
		ApiResponse response = modelApi.getSubscriberByPhonenumber(phoneNumber, null);
		throwMsg(hasData(response), "Phone Number already exists");
		response = modelApi.getWallet(phoneNumber, null);
		throwMsg(hasData(response), "Wallet with Phone Number already exists");
		wallet.setWalletId(phoneNumber);
		wallet.setMobileNetworkOperator(mobileNetworkOperator);
		response = modelApi.putWallet(wallet);
		throwMsg(!hasData(response), "Could not update wallet");
		subscriber.setPhoneNumber(phoneNumber);
		response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not save subscriber");
		return success("Account and Wallet successfully migrated", subscriber);
	}

	/**
	 * 
	 * This method basically updates a subscribers profile with any field that has been set 
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR', 'SUPPORT') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse updateSubscriber(RegisterationRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		String fname = request.firstName;
		if (!isNull(fname)) {
			logger.debug("changing first name");
			String name = subscriber.getName();		
			name = name == null ? "" : name;
			String[] firstLast = name.split(" ");
			if (firstLast.length == 2) {
				name = fname + " " + firstLast[1];
				subscriber.setName(name);
			} else if (firstLast.length == 1) {
				name = fname;
				logger.debug(subscriber.getPhoneNumber() + " has no last name");
			}
		}
		String lname = request.lastName;
		if (!isNull(lname)) {
			logger.debug("changing last name");
			String name = subscriber.getName();		
			name = name == null ? "" : name;
			String[] firstLast = name.split(" ");
			if (firstLast.length == 2) {
				name = firstLast[0] + " " + lname;
				subscriber.setName(name);
			} else if (firstLast.length == 1) {
				name = lname;
				logger.debug(subscriber.getPhoneNumber() + " has no last name");
			}
		}
		Gender gender = request.gender;
		if (!isNull(gender)) {
			logger.debug("changing gender");
			subscriber.setGender(gender);
		}
		String dateOfBirth = request.dateOfBirth;
		if (!isNull(dateOfBirth)) {
			logger.debug("changing date of birth");
			checkDateOfBirth(dateOfBirth);
			subscriber.setDateOfBirth(dateOfBirth);
		}
		MobileNetworkOperator mobileNetworkOperator = request.mobileNetworkOperator;
		if (!isNull(mobileNetworkOperator)) {
			Wallet wallet = getWallet(subscriber);
			wallet.setMobileNetworkOperator(mobileNetworkOperator);
		}
		ApiResponse response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not update your account");
		return success("Demus Mayor profile updated!", getData(response));
	}
	
	private void checkDateOfBirth(String date) throws ApiException {
		throwMsg(isNull(date), "Date of Birth cannot be empty");
		throwMsg(date.split("-").length != 3, "Valid date format is DD-MM-YYYY");
	}
	
	/**
	 * 
	 * This method is used to update a user's device with the new push notification key
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR', 'SUPPORT') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse updateDevice(UpdateDeviceRq request) throws ApiException {
		validateRequest(request);
		String clientId = request.clientId;
		throwMsg(isNull(clientId), "Client Id cannot be empty");
		DeviceOs deviceOs = request.clientOs;
		throwMsg(isNull(deviceOs), "Client Operating System cannot be empty");
		Subscriber subscriber = getSubscriber(request);
		ApiResponse response = modelApi.getDevice(clientId, deviceOs, subscriber.getId());
		throwMsg(!hasData(response), "Could not find device with client id and os");
		Device device = (Device) getData(response);
		
		String pushNotificationKey = request.pushNotificationKey;
		if (!isNull(pushNotificationKey))
			device.setPushNotificationKey(pushNotificationKey);
		
		response = modelApi.putDevice(device);
		throwMsg(!hasData(response), "Could not update device");
		
		return success("Device udpated", getData(response));
	}

	/**
	 * This method is used to change a user's email
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR', 'SUPPORT') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse changeEmail(ChangeEmailRq request) throws ApiException {
		validateRequest(request);
		String pin = request.pin;
		Subscriber subscriber = getSubscriber(request);
		throwMsg(notValidPin(subscriber, pin), "Pin is incorrect");
		String email = request.email;
		required("Email", email);
		throwMsg(!EmailValidator.getInstance().isValid(email), "Email provided is not valid");
		throwMsg(hasData(modelApi.getSubscriberByEmail(email)), "Email already exists");
		subscriber.setEmail(email);
		ApiResponse response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not update account");
		return success("Account Updated", getData(response));
	}
	
	private boolean notValidPin (Subscriber subscriber, String risky) {
		//return !subscriber.getPin().equals(risky);
		return !subscriber.matchPin(risky);
	}

	/**
	 * 
	 * This method is used to transfer wallet value from one account to another
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse transfer(TransferRq request) throws ApiException {
		validateRequest(request);
		Subscriber sender = getSubscriber(request);
		throwCannotTransact(sender);
		Double amount = request.amount;
		required("Transfer amount", amount);
		throwMsg(amount <= 0, "Amount must be greater than zero");
		String phoneNumber = request.receiverPhoneNumber;
		required("Phone Number", phoneNumber);
		String pin = request.pin;
		required("Pin", pin);
		throwMsg(!sender.matchPin(pin), "Pin is incorrect");
		
		ApiResponse response = modelApi.getSubscriberByPhonenumber(phoneNumber, EntityStatus.ACTIVE);
		throwMsg(!hasData(response), "Demus Mayor subscriber not found with phone number: " + phoneNumber);
		Subscriber receiver = (Subscriber) getData(response);
		throwMsg(receiver.getEntityStatus() != EntityStatus.ACTIVE, "Sorry the receiver is not able to receive funds at the moment");
		
		Wallet receiverWallet = getWallet(receiver);
		Wallet senderWallet = getWallet(sender);
		Double oSenderWalletBalance = senderWallet.getBalance();
		throwMsg(amount > oSenderWalletBalance, "Wallet has insuffient balance");
		
		String sendTransactionDesc = String.format("Transfered NGN %s to %s", amount, receiverWallet.getWalletId());
		WalletTransaction debitTransaction = walletTransaction(sender, senderWallet, (-1) * amount, sendTransactionDesc, PaymentGateway.WALLET, null, WalletTransactionType.SHARE_STOCK, null);
		
		Double nSenderWalletBalance = oSenderWalletBalance - amount;
		logger.debug("sender wallet new balance is " + nSenderWalletBalance);
		
		String recvTransactionDesc = String.format("Received NGN %s from %s", amount, senderWallet.getWalletId());
		WalletTransaction creditTransaction = walletTransaction(receiver, receiverWallet, amount, recvTransactionDesc, PaymentGateway.WALLET, null, WalletTransactionType.RECEIVE_STOCK, debitTransaction);
		
		Template template = velocityEngine.getTemplate("templates/stock-transfer.html");
	    VelocityContext model = new VelocityContext();
	    model.put("user", sender);
	    model.put("receiver", receiver);
		model.put("wallet", senderWallet);
		model.put("transaction", debitTransaction);
		model.put("appName", Config.APP_NAME);
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		emailService.sendEmail(sender.getEmail(), Config.WALLET_TRANSFER, Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
		sendTransactionAlert(sender, senderWallet , debitTransaction);
		
		template = velocityEngine.getTemplate("templates/stock-credit.html");
		model = new VelocityContext();
	    model.put("user", receiver);
	    model.put("sender", sender);
		model.put("wallet", receiverWallet);
		model.put("transaction", creditTransaction);
		model.put("appName", Config.APP_NAME);
	    stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		emailService.sendEmail(receiver.getEmail(), Config.CREDIT_MSG, Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
		sendTransactionAlert(receiver, receiverWallet , creditTransaction);
		
		return success("Transfer successful", debitTransaction);
	}
	
	/**
	 * This method is used to transfer some money to an unregistered subscribers account
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("(#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse refer(ReferSubscriberRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		throwCannotTransact(subscriber);
		String phoneNumber = request.receiverPhoneNumber;
		required("Phone Number", phoneNumber);
		validatePhoneNumber(phoneNumber);
		throwMsg(modelApi.getSubscriberByPhonenumber(phoneNumber, null).size > 0, "You can't refer an already registered user.");
		List<Refer> refers = (List<Refer>) getListData(modelApi.getRefer(phoneNumber, EntityStatus.INACTIVE));
		throwMsg (refers != null && refers.size() > 0,  "Phone number already referred to Demus Mayor");
		Double amount = request.amount;
		required("Amount", amount);
		throwMsg(amount < Config.MIN_REFERRAL_AMOUNT, "Amount too small. Minimum referral amount is " + Config.MIN_REFERRAL_AMOUNT);
		String pin = request.pin;
		required("Pin", pin);
		throwMsg(!subscriber.matchPin(pin), "Pin is incorrect");
		Wallet wallet = getWallet(subscriber);
		Double oBalance = wallet.getBalance();
		throwMsg(amount > oBalance, "Your account does not have sufficient balance to complete referral");
		WalletTransaction transaction = walletTransaction(subscriber, wallet, -amount, String.format("Referral credit transfer to %s. Amount %s", phoneNumber, priceFormat.format(amount)), PaymentGateway.WALLET, phoneNumber, WalletTransactionType.REFER, null);
		Refer refer = new Refer(subscriber, phoneNumber, transaction, EntityStatus.INACTIVE);
		ApiResponse response = modelApi.putRefer(refer);
		throwMsg(!hasData(response), "Could not create referral successfully");
		
		if(transaction.getDescription().contains("ETISALAT"))
			transaction.setDescription(transaction.getDescription().replace("ETISALAT", "9MOBILE"));
		
		Template template = velocityEngine.getTemplate("templates/refer.html");
	    VelocityContext model = new VelocityContext();
	    model.put("user", subscriber);
	    model.put("phoneNumber", phoneNumber);
		model.put("wallet", wallet);
		model.put("transaction", transaction);
		model.put("appName", Config.APP_NAME);
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
	    sendTransactionAlert(subscriber, wallet, transaction);
		emailService.sendEmail(subscriber.getEmail(), Config.REFER, Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
		String smsAlert = String.format("%s referred you to Demus Mayor with %s. To access funds click to install Demus Mayor App at %s. %s", subscriber.getName(), priceFormat.format(amount), playStoreUrl, formatDate.format(transaction.getCreateDate()));
		smsService.send(phoneNumber, smsAlert, smsLabel);
		
		return success("You have successfully refered " + phoneNumber, transaction);
	}
	
	public void referWithNoException(ReferSubscriberRq request) {
		try {
			validateRequest(request);
			Subscriber subscriber = getSubscriber(request);
			throwCannotTransact(subscriber);
			String phoneNumber = request.receiverPhoneNumber;
			required("Phone Number", phoneNumber);
			validatePhoneNumber(phoneNumber);
			throwMsg(modelApi.getSubscriberByPhonenumber(phoneNumber, null).size > 0, "You can't refer an already registered user.");
			List<Refer> refers = (List<Refer>) getListData(modelApi.getRefer(phoneNumber, EntityStatus.INACTIVE));
			throwMsg (refers != null && refers.size() > 0,  "Phone number already referred to Demus Mayor");
			Double amount = request.amount;
			required("Amount", amount);
			throwMsg(amount < Config.MIN_REFERRAL_AMOUNT, "Amount too small. Minimum referral amount is " + Config.MIN_REFERRAL_AMOUNT);
			String pin = request.pin;
			required("Pin", pin);
			throwMsg(!subscriber.matchPin(pin), "Pin is incorrect");
			Wallet wallet = getWallet(subscriber);
			Double oBalance = wallet.getBalance();
			throwMsg(amount > oBalance, "Your account does not have sufficient balance to complete referral");
			WalletTransaction transaction = walletTransaction(subscriber, wallet, -amount, String.format("Referral credit transfer to %s. Amount %s", phoneNumber, priceFormat.format(amount)), PaymentGateway.WALLET, phoneNumber, WalletTransactionType.REFER, null);
			Refer refer = new Refer(subscriber, phoneNumber, transaction, EntityStatus.INACTIVE);
			ApiResponse response = modelApi.putRefer(refer);
			throwMsg(!hasData(response), "Could not create referral successfully");
			
			if(transaction.getDescription().contains("ETISALAT"))
				transaction.setDescription(transaction.getDescription().replace("ETISALAT", "9MOBILE"));
			
			Template template = velocityEngine.getTemplate("templates/refer.html");
		    VelocityContext model = new VelocityContext();
		    model.put("user", subscriber);
		    model.put("phoneNumber", phoneNumber);
			model.put("wallet", wallet);
			model.put("transaction", transaction);
			model.put("appName", Config.APP_NAME);
		    StringWriter stringWriter = new StringWriter();
		    template.merge(model, stringWriter);
		    sendTransactionAlert(subscriber, wallet, transaction);
			emailService.sendEmail(subscriber.getEmail(), Config.REFER, Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
			String smsAlert = String.format("%s referred you to Demus Mayor with %s. To access funds click to install Demus Mayor App at %s. %s", subscriber.getName(), priceFormat.format(amount), playStoreUrl, formatDate.format(transaction.getCreateDate()));
			smsService.send(phoneNumber, smsAlert, smsLabel);
		}
		catch (Exception e) {
			
		}
	}

	private void validatePhoneNumber(String phoneNumber) throws ApiException {
		throwMsg(phoneNumber == null || phoneNumber.length() != 11, "phone number is not valid");
	}

	private String generateTransactionId(Subscriber subscriber) {
		return String.format("%s-%s-%s", TRANSACTION_PREFIX, subscriber.getId(),generateShortToken(9));
	}

	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse forgotPassword(ForgotRq request) throws ApiException {
		Subscriber subscriber = matchSubscriber(request.emailOrPhoneNumber);
		String token = new RandomString(8).nextString();
		ActivationToken activationToken = new ActivationToken(subscriber, token, ActivateTokenType.RESET_PASSWORD);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		activationToken.setExpiryDate(calendar.getTime());
		ApiResponse response = modelApi.putActivationToken(activationToken);
		throwMsg(!hasData(response), "Could not save token");
		sendGeneralMessage (subscriber, "Forgot Password?", "Your password reset token is : <b>" + token + "</b> <br>Use this token to reset your password on the Forgot Password App page. This token will expire on " + calendar.getTime());
		return success("Password reset successfully", "");
	}
	
	@Override
	@Transactional (rollbackOn = ApiException.class)
	public ApiResponse confirmForgotPassword(ConfirmPasswordResetRq request) throws ApiException {
		Subscriber subscriber = matchSubscriber(request.emailOrPhoneNumber);
		String accessCode = request.token;
		required("Access Code", accessCode);
		ApiResponse response = modelApi.getActivationToken(subscriber, accessCode, ActivateTokenType.RESET_PASSWORD);
		throwMsg(!hasData(response), "Token not found or has expired");
		ActivationToken activationToken = (ActivationToken) getData(response);
		String nPassword = request.newPassword;
		required("New Password", nPassword);
		String cPassword = request.confirmPassword;
		required("Confirm Password", cPassword);
		throwMsg(!cPassword.equals(nPassword), "Your passwords do not match");
		subscriber.setPassword(nPassword);
		subscriber.getTags().remove(SubscriberTag.PASSWORD_EXPIRED);
		response = modelApi.putSubscriber(subscriber);
		throwMsg(!hasData(response), "Could not save subscriber");
		sendGeneralMessage (subscriber, "Password Reset Successful", "Your password was successfully reset on " + new Date());
		modelApi.deleteActivationToken(Arrays.asList(activationToken.getId()));
		return success("Password reset successfully", "");
	}
	
	static Gson gson = new Gson();
	
	@Value ("${com.demus.company.email}")
	private String adminEmail;
	
	private Double getPinLoadCharge (Double amount) {
		Double platformCharge = 0.00;
		platformConfiguration = platformConfigurationCrud.findAll().get(0);
		if (amount >= platformConfiguration.getMinimumAmountForPinLoaderFixedCharge() && amount <= platformConfiguration.getMaximumAmountForPinLoaderFixedCharge()) {
			platformCharge = platformCharge + platformConfiguration.getPinLoaderFixedCharge();
		}
		if (amount >= platformConfiguration.getMinimumAmountForPinLoaderPercentageCharge() && amount <= platformConfiguration.getMaximumAmountForPinLoaderPercentageCharge()) {
			platformCharge = platformCharge + (amount * (platformConfiguration.getPinLoaderPercentageCharge()));
		}
		return platformCharge;
	}
	
	@Autowired
	Company company;
	
	@Autowired
	WalletTransactionCrud walletTransactionCrud;
	
	@Autowired
	PushMessageCrud pushCrud;
	
	private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	

	private NumberFormat priceFormat = new DecimalFormat("NGN ##,###.00");

	private Double getAccountStatementCharge (Date startDate, Date endDate) throws ApiException {
		platformConfiguration = platformConfigurationCrud.findAll().get(0);	
		Days d = Days.daysBetween(new DateTime(startDate), new DateTime(endDate));
		int days = d.getDays();
		if (days < 32) {
			return platformConfiguration.getAccStatementChargeOneMonth();
		} else if (days >= 32 && days <= 93) {
			return platformConfiguration.getAccStatementChargeThreeMonth();
		} else if (days > 93) {
			return platformConfiguration.getAccStatementSixMonthCharge();
		}
		throwMsg(true, "Your Account Statement date range could not be charged. Choose 1 month, 3 months, or 6 months");
		return null;
	}
	
	@Override
	public ApiResponse requestAccountStatement(AccountStatementRequest request) throws ApiException {
		Subscriber subscriber = getSubscriber(request.phoneNumber);
		required("Start Date", request.startDate);
		required("End Date", request.endDate);
		Date startDate = request.startDate;
		Date endDate = request.endDate;
		List<WalletTransaction> transactions = walletTransactionCrud.accountStatement(startDate, endDate, subscriber.getWallet());
		throwMsg(transactions.size() == 0, "No transaction was recorded for the period");
		Double platformCharge = getAccountStatementCharge(startDate, endDate);
		if (platformCharge >= 2 * Double.MIN_VALUE) {
			Wallet wallet = subscriber.getWallet();
			throwMsg(wallet.getBalance() - platformCharge < 0, "Insufficient funds. Your minimum balance will be exceeded. We cannot proceed with this request.");
			WalletTransaction accountStatementRequest = walletTransaction(subscriber, wallet, -platformCharge, "Account statement request charge" , PaymentGateway.WALLET, null, WalletTransactionType.ACCOUNT_STATEMENT, null);
			sendTransactionAlert(subscriber, wallet, accountStatementRequest);
		}
		AccountStatement statement = new AccountStatement();
		Double debitSum = 0.00;
		Double creditSum = 0.00;
		Double commissionSum = 0.00;
		Double openingBalance = 0.00;
		Double closingBalance = 0.00;
		
		List<AccountStatementTransaction> statementTransactions = new ArrayList<AccountStatementTransaction>();
 		for (int i = 0; i < transactions.size(); i++) {
 			WalletTransaction transaction = transactions.get(i);
 			Double amount = transaction.getAmount();
			if (amount < 0) {
				debitSum = debitSum + amount;
			} else {
				creditSum = creditSum + amount;
			}
			if (transaction.getType() == WalletTransactionType.COMMISSION) {
				commissionSum = commissionSum + amount;
			}
			if (i == 0) {
				openingBalance = openingBalance + transaction.getBalanceAfterTransaction();
			}
			if (i == transactions.size() - 1) {
				closingBalance = closingBalance + transaction.getBalanceAfterTransaction();
			}
			AccountStatementTransaction statementTransaction = new AccountStatementTransaction(transaction);
			statementTransaction.setBalanceAfterTransaction(priceFormat.format(transaction.getBalanceAfterTransaction()));
			statementTransaction.setCreatedDate(formatDate.format(transaction.getCreatedDate().toDate()));
			if (transaction.getType() != WalletTransactionType.COMMISSION) {
				statementTransaction.setCreditAmount(transaction.getAmount() >= 0 ? priceFormat.format(transaction.getAmount()) : "-");
				statementTransaction.setCommissionAmount("-");
			} else {
				statementTransaction.setCreditAmount("-");
				statementTransaction.setCommissionAmount(priceFormat.format(transaction.getAmount()));
			}
			// Multiple by minus to clear minus
			statementTransaction.setDebitAmount(transaction.getAmount() < 0 ? priceFormat.format(-transaction.getAmount()) : "-");
			statementTransaction.setDescription(transaction.getDescription());
			statementTransaction.setTransactionId(transaction.getTransactionId());
			statementTransactions.add(statementTransaction);
		}
 		statement.setClosingBalance(priceFormat.format(closingBalance));
 		statement.setStartDate(formatDate.format(startDate));
 		statement.setEndDate(formatDate.format(endDate));
 		statement.setCommissionSum(priceFormat.format(commissionSum));
 		statement.setCreditSum(priceFormat.format(creditSum));
 		statement.setDebitSum(priceFormat.format(-debitSum));
 		statement.setOpeningBalance(priceFormat.format(openingBalance));
 		sendStatementOfAccount(subscriber, statement, statementTransactions);
		return success("Your statement of account request will be available in your registered email shortly", null);
	}

	private void sendStatementOfAccount (Subscriber subscriber, AccountStatement statement, List<AccountStatementTransaction> transactions) throws ApiException {
		Template template = velocityEngine.getTemplate("templates/statement.html");
	    VelocityContext model = new VelocityContext();
	    model.put("now", formatDate.format(new Date()));
	    model.put("subscriber", subscriber);
	    model.put("statement", statement);
		model.put("transactions", transactions);
		model.put("company", company);
		model.put("appName", Config.APP_NAME);
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		emailService.sendEmail(subscriber.getEmail(), "Statement of Account", Config.CC_TRANSACTION, stringWriter.toString(), new HashMap<String, String>());
	}

	@Override
	public ApiResponse confirmOrder(String startDate, String endDate, String productCode, Integer quantity, Double amount, WalletTransactionType type) throws ApiException {
		throwMsg(type == null, "Transaction type is required");
		String message = "Review transaction details";
		switch (type) {
			case AIRTIME:
				throwMsg(productCode == null, "Product code is required");
				throwMsg(quantity == null, "Retail quantity is required");
				ApiResponse response = modelApi.getProduct(productCode, EntityStatus.ACTIVE);
				throwMsg(!hasData(response), "Product not available at the moment");
				Product product = (Product) getData(response);
				Double sellingPrice = product.getPrice();
				Double cost = quantity * sellingPrice;
				return success(message, new PreConfirmTransaction(priceFormat.format(cost), priceFormat.format(0.00)));
			case CASHBACK:
				throwMsg(amount == null, "Cashback amount is required");
				return success(message, new PreConfirmTransaction(priceFormat.format(amount), priceFormat.format(getCashBackCharge(amount))));
			case PINLOAD:
				throwMsg(amount == null, "Pin amount is required");
				return success(message, new PreConfirmTransaction(priceFormat.format(amount), priceFormat.format(getPinLoadCharge(amount))));
			case ACCOUNT_STATEMENT:
				try {
					required("Start Date", startDate);
					required("End Date", endDate);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Double accountStatementCharge = getAccountStatementCharge (formatter.parse(startDate), formatter.parse(endDate));
					return success(message, new PreConfirmTransaction(priceFormat.format(0.00), priceFormat.format(accountStatementCharge)));	
				} catch (ParseException e) {
					throwMsg(true, "Could not get charges due to incorrect date format. Use yyyy-MM-dd");
				}
			case COMMISSION:
				break;
			case PINLOAD_CHARGE:
				break;
			case QUICKTELLER:
				break;
			case RECEIVE_STOCK:
				break;
			case REFER:
				throwMsg(amount == null, "Refer amount is required");
				return success(message, new PreConfirmTransaction(priceFormat.format(amount), priceFormat.format(0.00)));
			case REVERSAL:
				break;
			case SHARE_STOCK:
				throwMsg(amount == null, "Share stock amount is required");
				return success(message, new PreConfirmTransaction(priceFormat.format(amount), priceFormat.format(0.00)));
			default:
				break;
		}
		return null;
	}

	@Override
	@Transactional (rollbackOn = ApiException.class)
	@PreAuthorize ("hasAnyRole('ADMINISTRATOR') or (#request.phoneNumber == principal.subscriber.phoneNumber)")
	public ApiResponse data(DataRetailRq request) throws ApiException {
		validateRequest(request);
		Subscriber subscriber = getSubscriber(request);
		throwCannotTransact(subscriber);
		
		String phoneNumber = request.receiverNumber;
		required("Phone number", phoneNumber);
		
		//response object
		ApiResponse response;
		
		String productCode = request.productCode;
		required("Data product code", productCode);
		Integer quantity = 1;
		required("Amount", quantity);
		String pin = request.pin;
		required("Pin", pin);
		throwMsg(!subscriber.matchPin(pin), "Pin is incorrect");
		MobileNetworkOperator mobileNetworkOperator = request.mobileNetworkOperator;
		required("Mobile Network", mobileNetworkOperator);
		throwMsg(!mobileNetworkOperator.name().equals("GLO"), "This service is not yet available. Thank you for choosing Demus Mayor.");
		logger.debug("Api data validation passed");
		
		response = modelApi.getProduct(productCode, EntityStatus.ACTIVE);
		throwMsg(!hasData(response), "Product not available at the moment");
		Product product = productCrud.findByProductCodeAndCategoryCategoryCode(productCode, "DATA", EntityStatus.ACTIVE);
		throwMsg(product == null || !product.getEntityStatus().equals(EntityStatus.ACTIVE), "Product not available at the moment");
		throwMsg(quantity > product.getStockCount(), "Product sold out. Please try again later.");
		Integer minPurchaseQuantity =  product.getMinPurchaseQuantity();
		throwMsg(quantity < minPurchaseQuantity, "Minimum purchase quanity is " + minPurchaseQuantity);
		
		Double sellingPrice = product.getPrice();
		Double cost = quantity * sellingPrice;
		
		logger.debug("checking subscriber has enough amount in wallet");
		Wallet wallet = getWallet(subscriber);
		Double walletBalance = wallet.getBalance();
		throwMsg(walletBalance < cost, "You do not have sufficient balance to complete transaction.");
		logger.debug("now creating order");
		String orderId = UUID.randomUUID().toString();
		Order order = new Order(orderId, cost, OrderStatus.PENDING, request.clientOs, EntityStatus.ACTIVE, subscriber);
		response = modelApi.putOrder(order);
		throwMsg(!hasData(response), "Order could not be created");
		order = (Order) getData(response);
		logger.debug("now creating subscription");
		Subscription subscription = new Subscription(order, product, quantity, null, cost, EntityStatus.ACTIVE);
		response = modelApi.putSubscription(subscription);
		throwMsg(!hasData(response), "Subscription could not be created");
		String transactionId = generateTransactionId(subscriber);
		String transactionMessage = String.format("Data recharge retail for %s to %s.", product.getName(), phoneNumber);
		WalletTransaction transaction = walletTransaction(subscriber, wallet, (-1) * cost, transactionMessage, PaymentGateway.WALLET, request.mobileNetworkOperator.toString(), WalletTransactionType.DATA, null);
		wallet = walletCrud.findOne(wallet.getId());
		
		product.setStockCount(product.getStockCount() - quantity);
		response = modelApi.putProduct(product);
		throwMsg(!hasData(response), "Could not update stock count");
		DeliverOrderRq dRq = new DeliverOrderRq();
		dRq.subscription = subscription;
		dRq.phoneNumber = phoneNumber;
		dRq.walletTransaction = transaction;
		dRq.order = order;
		
		String operatorMain = "";
	    switch (request.mobileNetworkOperator) {
			case GLO:
				operatorMain = "GLO";
				break;
			case AIRTEL:
				operatorMain = "AIRTEL";
				break;
			case ETISALAT:
				operatorMain = "9MOBILE";
				break;
			case VISAFONE:
				operatorMain = "SMILE";
				break;
			case MTN:
				operatorMain = "MTN";
				break;
			case GENERIC:
				operatorMain = "GENERIC";
				break;
			default:
				operatorMain = "9MOBILE";
				break;
		}
	    
		VendorApiLog rp = vendorApi.deliverOrderRequest(dRq, operatorMain, true, false);
		transaction.setReference(rp.getAuditId());
		try {
			modelApi.putWalletTransaction(transaction);
		} catch (Exception e) {
			logger.error("Could not log mobifin transaction", e);
		}
		
		if(transaction.getDescription().contains("ETISALAT"))
			transaction.setDescription(transaction.getDescription().replace("ETISALAT", "9MOBILE"));
		
		Template template = velocityEngine.getTemplate("templates/confirm_order.html");
	    VelocityContext model = new VelocityContext();
	    model.put("user", subscriber);
		model.put("order", order);
		model.put("transaction", transaction);
		model.put("wallet", wallet);
		model.put("product", product);
		model.put("appName", Config.APP_NAME);
	    StringWriter stringWriter = new StringWriter();
	    template.merge(model, stringWriter);
		
	    sendTransactionAlert(subscriber, wallet, transaction);
		
	    String format = "Received %s from %s. Txn ref: %s at %s. Powered by Demus Mayor";
	    
	    String productNameMain = "";
	    switch (request.mobileNetworkOperator) {
			case GLO:
				productNameMain = product.getName();
				break;
			case AIRTEL:
				productNameMain = product.getName();
				break;
			case ETISALAT:
				productNameMain = product.getName().replace("ETISALAT", "9MOBILE");
				break;
			case VISAFONE:
				productNameMain = product.getName();
				break;
			case MTN:
				productNameMain = product.getName();
				break;
			case GENERIC:
				productNameMain = product.getName();
				break;
			default:
				productNameMain = "9MOBILE";
				break;
		}
	    
	    String receivedFrom = wallet.getWalletId();
	    
	    String smsAlert = String.format(format, productNameMain, receivedFrom, transaction.getTransactionId(), formatDate.format(transaction.getCreateDate()));
	    
	    smsService.send(phoneNumber, smsAlert, smsLabel);
	    
		return success(String.format("%s Your current wallet balance is %s. Txn #: %s", transactionMessage, transaction.getPrintBalance(), transactionId), transaction);
	}
}
