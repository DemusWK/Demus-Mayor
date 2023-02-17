/**
 * 
 */
package com.demus.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demus.api.ex.ApiAccessDeniedException;
import com.demus.api.ex.ApiAuthenticationException;
import com.demus.api.ex.ApiException;
import com.demus.api.ex.ClientExpiredException;
import com.demus.api.ex.InvalidStateException;
import com.demus.api.io.ApiRequest;
import com.demus.api.io.ApiResponse;
import com.demus.api.io.ApiResponseStatus;
import com.demus.api.system.Config;
import com.demus.entity.ClientStatus;
import com.demus.entity.DeviceOs;
import com.demus.entity.WalletTransactionType;
import com.demus.model.AccountStatementRequest;
import com.demus.model.BankAccountRq;
import com.demus.model.ChangeEmailRq;
import com.demus.model.ChangePasswordRq;
import com.demus.model.ConfirmDataRq;
import com.demus.model.ConfirmPasswordResetRq;
import com.demus.model.DataRetailRq;
import com.demus.model.DateFilterableRq;
import com.demus.model.DepositRq;
import com.demus.model.EcleverPinDepositRq;
import com.demus.model.ForgotRq;
import com.demus.model.GenerateTokenRq;
import com.demus.model.GetStringRq;
import com.demus.model.LoginRq;
import com.demus.model.MigratePhoneNumberRq;
import com.demus.model.PayStackDepositRq;
import com.demus.model.RechargeRetailRq;
import com.demus.model.RechargeRetailWebRq;
import com.demus.model.ReferSubscriberRq;
import com.demus.model.RegisterationRq;
import com.demus.model.ResetPasswordRq;
import com.demus.model.ResetPinRq;
import com.demus.model.SearchFilterableRq;
import com.demus.model.SetPinRq;
import com.demus.model.SubscriberModel;
import com.demus.model.TransferRq;
import com.demus.model.UpdateDeviceRq;

/**
 * Public Api Interface for all operations
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@RestController
public class Api {
	
	@Autowired
	SubscriberModel subscriberModel;
	
	private Logger logger = LoggerFactory.getLogger(Api.class);
	
	
	@RequestMapping (value = "/secure/person/profile", method = RequestMethod.GET) 
	public ApiResponse confirmSubscriber (@RequestParam String id) {
		try {
			return subscriberModel.confirmSubscriber(id);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping (value = "/secure/person/confirmOrder", method = RequestMethod.GET) 
	public ApiResponse confirmCharge (@RequestParam (required = false) String startDate, @RequestParam(required = false) String endDate, @RequestParam (required = false) String productCode, @RequestParam (required = false) Integer quantity, @RequestParam (required = false) Double amount, @RequestParam (required = false) WalletTransactionType type) {
		try {
			return subscriberModel.confirmOrder(startDate, endDate, productCode, quantity, amount, type);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	
	@RequestMapping(value = "/public/person/login", method = RequestMethod.POST)
	public ApiResponse login(@RequestParam("pushKey") String key, @RequestBody LoginRq request) {
		try {
			if(key != null && key != "")
				request.pushNotificationKey = key;
			
			return subscriberModel.login(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/public/person/login/web", method = RequestMethod.POST)
	public ApiResponse login(@RequestBody LoginRq request) {
		try {
			return subscriberModel.login(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/public/person/register", method = RequestMethod.POST)
	public ApiResponse register(@RequestParam("pushKey") String key, @RequestBody RegisterationRq request) {
		try {
			if(key != null && key != "")
				request.pushNotificationKey = key;
			
			return subscriberModel.register(request);
		} catch (Exception e) {
			return failure(e);
		}
		
	}
	
	@RequestMapping(value = "/public/person/register2", method = RequestMethod.POST)
	public ApiResponse register2(@RequestParam("pushKey") String key, @RequestBody RegisterationRq request) {
		try {
			if(key != null && key != "")
				request.pushNotificationKey = key;
			
			return subscriberModel.register2(request);
		} catch (Exception e) {
			return failure(e);
		}
		
	}
	
	@RequestMapping(value = "/public/person/register3", method = RequestMethod.POST)
	public ApiResponse register3(@RequestBody RegisterationRq request) {
		try {
			return subscriberModel.register3(request);
		} catch (Exception e) {
			return failure(e);
		}
		
	}
	
	@RequestMapping(value = "/public/person/register/web", method = RequestMethod.POST)
	public ApiResponse registerWeb(@RequestBody RegisterationRq request) {
		try {
			return subscriberModel.register3(request);
		} catch (Exception e) {
			return failure(e);
		}
		
	}
	
	
	@RequestMapping(value = "/secure/person/logout", method = RequestMethod.POST)
	public ApiResponse logoutUser(@RequestBody ApiRequest request) {
		try {
			return subscriberModel.logoutUser(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	
	@RequestMapping(value = "/secure/product/recharge", method = RequestMethod.POST)
	public ApiResponse recharge(@RequestBody RechargeRetailRq request) {
		try {
			return subscriberModel.recharge(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/secure/product/data_recharge", method = RequestMethod.POST)
	public ApiResponse dataRecharge(@RequestBody DataRetailRq request) {
		try {
			return subscriberModel.data(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/secure/product/recharge/web", method = RequestMethod.POST)
	public ApiResponse rechargeWeb(@RequestBody RechargeRetailWebRq request) {
		try {
			return subscriberModel.rechargeWeb(request, false);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/secure/product/recharge/web/test", method = RequestMethod.POST)
	public ApiResponse rechargeWebTest(@RequestBody RechargeRetailWebRq request) {
		try {
			return subscriberModel.rechargeWeb(request, true);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/secure/products", method = RequestMethod.POST)
	public ApiResponse listProducts(@RequestBody SearchFilterableRq request) {
		try {
			return subscriberModel.listProducts(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/secure/person/messages", method = RequestMethod.POST)
	public ApiResponse listMessages(@RequestBody DateFilterableRq request) {
		try {
			return subscriberModel.listMessages(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	
	@RequestMapping(value = "/secure/wallet/transactions", method = RequestMethod.POST)
	public ApiResponse listTransactions(@RequestBody DateFilterableRq request) {
		try {
			return subscriberModel.listTransactions(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/secure/wallet/transaction", method = RequestMethod.POST)	
	public ApiResponse getTransaction(@RequestBody GetStringRq request) {
		try {
			return subscriberModel.getTransaction(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/internal/wallet/reverse", method = RequestMethod.POST)	
	public ApiResponse reverse(@RequestBody DepositRq request) {
		try {
			return subscriberModel.reverseDeposit(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/internal/wallet/deposit", method = RequestMethod.POST)	
	public ApiResponse deposit(@RequestBody DepositRq request) {
		try {
			return subscriberModel.deposit(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/secure/wallet/deposit", method = RequestMethod.POST)	
	public ApiResponse depositPayStack(@RequestBody PayStackDepositRq request) {
		try {
			return subscriberModel.depositPayStack(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/secure/wallet/deposit_auth", method = RequestMethod.POST)	
	public ApiResponse depositPayStackAuth(@RequestBody PayStackDepositRq request) {
		try {
			return subscriberModel.depositPayStackAuth(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/secure/wallet/deposit_last_auth", method = RequestMethod.POST)	
	public ApiResponse depositPayStackAuthLastUsedCardToken(@RequestBody PayStackDepositRq request) {
		try {
			return subscriberModel.depositPayStackAuthLastUsedCardToken(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/secure/person/card_auth_tokens", method = RequestMethod.POST)
	public ApiResponse listCardAuthTokens(@RequestBody ApiRequest request) {
		try {
			return subscriberModel.listCardAuthTokens(request);
		} catch (Exception e) {
			return failure(e);
		}
		
	}
	
	@RequestMapping(value = "/secure/wallet/redeem_pin", method = RequestMethod.POST)	
	public ApiResponse redeemPin(@RequestBody EcleverPinDepositRq request) {
		try {
			return subscriberModel.depositEcleverPin(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/secure/wallet/balance", method = RequestMethod.POST)	
	public ApiResponse checkBalance(@RequestBody ApiRequest request) {
		try {
			return subscriberModel.checkBalance(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/secure/wallet/update_primary_bank", method = RequestMethod.POST)
	public ApiResponse updatePrimaryBank(@RequestBody BankAccountRq request) {
		try {
			return subscriberModel.updatePrimaryBank(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/public/banks", method = RequestMethod.POST)	
	public ApiResponse listBanks(@RequestBody SearchFilterableRq request) {
		try {
			return subscriberModel.listBanks(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	
	@RequestMapping(value = "/secure/person/banks", method = RequestMethod.POST)
	public ApiResponse listMyBankAccounts(@RequestBody ApiRequest request) {
		try {
			return subscriberModel.listMyBankAccounts(request);	
		} catch (Exception e) {
			return failure(e);
		}
		
	}

	@RequestMapping(value = "/secure/person/password", method = RequestMethod.POST)	
	public ApiResponse changePassword(@RequestBody ChangePasswordRq request) {
		try {
			return subscriberModel.changePassword(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/secure/person/pin/reset", method = RequestMethod.POST)	
	public ApiResponse resetPin(@RequestBody ResetPinRq request) {
		try {
			return subscriberModel.resetPin(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/secure/person", method = RequestMethod.POST)	
	public ApiResponse updateSubscriber(@RequestBody RegisterationRq request) {
		try {
			return subscriberModel.updateSubscriber(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/secure/person/device", method = RequestMethod.POST)	
	public ApiResponse updateDevice(@RequestBody UpdateDeviceRq request) {
		try {
			return subscriberModel.updateDevice(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/secure/wallet/refer", method = RequestMethod.POST)	
	public ApiResponse refer(@RequestBody ReferSubscriberRq request) {
		try {
			return subscriberModel.refer(request);
		} catch (Exception e) {
			return failure(e);
		}
		
	}

	@RequestMapping(value = "/secure/wallet/transfer", method = RequestMethod.POST)	
	public ApiResponse transfer(@RequestBody TransferRq request) {
		try {
			return subscriberModel.transfer(request);
		} catch (Exception e) {
			return failure(e);
		}
		
	}

	@RequestMapping(value = "/secure/person/pin", method = RequestMethod.POST)	
	public ApiResponse changePin(@RequestBody SetPinRq request) {
		try {
			return subscriberModel.changePin(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/secure/person/email", method = RequestMethod.POST)	
	public ApiResponse changeEmail(@RequestBody ChangeEmailRq request) {
		try {
			return subscriberModel.changeEmail(request);
		} catch (Exception e) {
			return failure(e);
		}
		
	}

	@RequestMapping(value = "/secure/person/phonenumber", method = RequestMethod.POST)	
	public ApiResponse migratePhoneNumber(@RequestBody MigratePhoneNumberRq request) {
		try {
			return subscriberModel.migratePhoneNumber(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/public/person/email/confirm", method = RequestMethod.GET, produces = {"text/html"})	
	public String confirmEmail( ConfirmDataRq request) {
		try {
			return subscriberModel.confirmEmail(request).message;
		} catch (Exception e) {
			return failure(e).message;
		}
		
	}

	@RequestMapping(value = "/public/person/phonenumber/confirm", method = RequestMethod.POST)	
	public ApiResponse confirmPhoneNumber(@RequestBody ConfirmDataRq request) {
		try {
			return subscriberModel.confirmPhoneNumber(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@RequestMapping(value = "/public/person/email/confirm-request", method = RequestMethod.POST)	
	public ApiResponse requestEmailToken(@RequestBody GenerateTokenRq request) {
		try {
			return subscriberModel.requestEmailToken(request);
		} catch (Exception e) {
			return failure(e);
		}
		
	}

	@RequestMapping(value = "/secure/person/phonenumber/confirm-request", method = RequestMethod.POST)	
	public ApiResponse requestSmsToken(@RequestBody GenerateTokenRq request) {
		try {
			return subscriberModel.requestSmsToken(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	
	@RequestMapping(value = "/secure/person/password/reset", method = RequestMethod.POST)
	public ApiResponse passwordReset(@RequestBody ResetPasswordRq request) {
		try {
			return subscriberModel.resetPassword(request);
		} catch (Exception e) {
			return failure(e);
		}
		
	}
	
	
	@RequestMapping(value = "/public/person/password/forgot", method = RequestMethod.POST)
	public ApiResponse forgotPassword(@RequestBody ForgotRq request) {
		try {
			return subscriberModel.forgotPassword(request);
		} catch (Exception e) {
			return failure(e);
		}
	}

	
	@RequestMapping(value = "/public/person/password/confirm", method = RequestMethod.POST)
	public ApiResponse confirmForgotPassword(@RequestBody ConfirmPasswordResetRq request) {
		try {
			return subscriberModel.confirmForgotPassword(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	private ApiResponse failure(Exception e) {
		//e.printStackTrace();
		// remove in production
		final StringWriter sw = new StringWriter();
	    final PrintWriter pw = new PrintWriter(sw, true);
	    e.printStackTrace(pw);
	    String err = sw.getBuffer().toString();
	    logger.error(e.getMessage() + " > " + err);
	    
		if (e instanceof IllegalArgumentException) {
			return new ApiResponse("Service provider not available at the moment. Please try again.!", Collections.emptyList(), 0, ApiResponseStatus.BAD_REQUEST);
		} else if (e instanceof ApiAccessDeniedException) {
			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.DENIED);
		} else if (e instanceof ConstraintViolationException) {
			return new ApiResponse("Service provider not available at the moment. Please try again.#", Collections.emptyList(), 0, ApiResponseStatus.SERVER_ERROR);
		} else if (e instanceof ClientExpiredException) {
			return new ApiResponse("Service provider not available at the moment. Please try again.$", Collections.emptyList(), 0, ApiResponseStatus.UPDATE_REQUIRED);
		} else if (e instanceof ApiAuthenticationException) {
			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.DENIED); 
		} else if (e instanceof AccessDeniedException) {
			return new ApiResponse("Service provider not available at the moment. Please try again.^", Collections.emptyList(), 0, ApiResponseStatus.DENIED); 
		} else if (e instanceof InvalidStateException) {
			return new ApiResponse("Service provider not available at the moment. Please try again.&", Collections.emptyList(), 0, ApiResponseStatus.CANNOT_TRANSACT); 
		} else if (e instanceof ApiException) {
			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.SERVER_ERROR);
		}  else if (e instanceof RuntimeException) {
			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.SERVER_ERROR);
		} else {
			return new ApiResponse("Service provider not available at the moment. Please try again.)", Collections.emptyList(), 0, ApiResponseStatus.SERVER_ERROR);
		}
	}
	
	private ApiResponse success(String message, Object object) {
		if (object == null)
			return new ApiResponse(message, Collections.emptyList(), 0, ApiResponseStatus.OK);
		else if (object instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) object;
			return new ApiResponse(message, object, list.size(), ApiResponseStatus.OK);
		} else {
			List<Object> list = Arrays.asList(object);
			return new ApiResponse(message, list, list.size(), ApiResponseStatus.OK);
		}
	}

	@RequestMapping(value = "/secure/wallet/account_statement", method = RequestMethod.POST)
	public ApiResponse requestAccountStatement(@RequestBody AccountStatementRequest request) {
		try {
			return subscriberModel.requestAccountStatement(request);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping(value = "/public/device/checkupdate", method = RequestMethod.POST)
	public ApiResponse checkUpdate(@RequestBody ApiRequest request) {
		try {
			DeviceOs clientOs = request.clientOs;
			Integer version = request.version;
			ClientStatus status = null;
			if (Config.CURRENT_VERSION.get(clientOs).equals(version)) {
				status = ClientStatus.UPDATED;
			} else if (!Config.APPROVED_VERSION.get(clientOs).contains(version)) {
				status = ClientStatus.UPDATE_AVAILABLE;
			} else {
				status = ClientStatus.EXPIRED;
			}
			Map<String, ClientStatus> response = new HashMap<String, ClientStatus>();
			response.put("status", status);
			Integer currentVersion = Config.CURRENT_VERSION.get(clientOs);
			char[] label = currentVersion.toString().toCharArray();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < label.length; i++) {
				buffer.append(label[i]);
				if (i != label.length -1) {
					buffer.append(".");
				}
			}
			return success (status.equals(ClientStatus.UPDATED) ? "Your App version is up to date" : 
				(status.equals(ClientStatus.UPDATE_AVAILABLE) ? "There's an update " + buffer.toString() + 
						" available" : "This version is expired. Please update to latest version " + buffer.toString()), response);
		} catch (Exception e) {
			return failure(e);
		}
		
	}
}
