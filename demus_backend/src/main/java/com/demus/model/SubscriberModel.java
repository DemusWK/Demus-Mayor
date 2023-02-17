/**
 * 
 */
package com.demus.model;

import com.demus.api.ex.ApiException;
import com.demus.api.io.ApiRequest;
import com.demus.api.io.ApiResponse;
import com.demus.entity.WalletTransactionType;

/**
 * Public Api Interface for all operations
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public interface SubscriberModel {
	
	ApiResponse login (LoginRq request) throws ApiException;
	
	ApiResponse register (RegisterationRq request) throws ApiException;
	
	ApiResponse logoutUser (ApiRequest request) throws ApiException;
	
	ApiResponse recharge (RechargeRetailRq request) throws ApiException;
	
	ApiResponse data (DataRetailRq request) throws ApiException;
	
	ApiResponse listProducts (SearchFilterableRq request) throws ApiException;
	
	ApiResponse listTransactions (DateFilterableRq request) throws ApiException;
	
	ApiResponse getTransaction (GetStringRq request) throws ApiException;
	
	ApiResponse deposit (DepositRq request) throws ApiException;
	
	ApiResponse checkBalance (ApiRequest request) throws ApiException;
	
	ApiResponse listBanks (SearchFilterableRq request) throws ApiException;
	
	ApiResponse changePassword (ChangePasswordRq request) throws ApiException;
	
	ApiResponse resetPin (ResetPinRq request) throws ApiException;
	
	ApiResponse updateSubscriber (RegisterationRq request) throws ApiException;
	
	ApiResponse updateDevice (UpdateDeviceRq request) throws ApiException;
	
	ApiResponse refer (ReferSubscriberRq request) throws ApiException;
	
	ApiResponse transfer (TransferRq request) throws ApiException;

	ApiResponse changePin(SetPinRq request) throws ApiException;
	
	ApiResponse changeEmail (ChangeEmailRq request) throws ApiException;

	ApiResponse migratePhoneNumber(MigratePhoneNumberRq request) throws ApiException;
	
	ApiResponse confirmEmail (ConfirmDataRq request) throws ApiException;
	
	ApiResponse confirmPhoneNumber (ConfirmDataRq request) throws ApiException;
	
	ApiResponse requestEmailToken (GenerateTokenRq request) throws ApiException;
	
	ApiResponse requestSmsToken (GenerateTokenRq request) throws ApiException;
	
	ApiResponse forgotPassword (ForgotRq request) throws ApiException;

	ApiResponse resetPassword(ResetPasswordRq request) throws ApiException;

	ApiResponse confirmForgotPassword(ConfirmPasswordResetRq request) throws ApiException;

	ApiResponse confirmSubscriber(String id) throws ApiException;

	ApiResponse reverseDeposit(DepositRq request) throws ApiException;
	
	ApiResponse requestAccountStatement (AccountStatementRequest request) throws ApiException;
	
	ApiResponse confirmOrder (String startDate, String endDate, String productCode, Integer quantity, Double amount, WalletTransactionType type) throws ApiException;

	ApiResponse listMessages(DateFilterableRq request) throws ApiException;

	ApiResponse register2(RegisterationRq request) throws ApiException;

	ApiResponse depositPayStack(PayStackDepositRq request) throws ApiException;

	ApiResponse listMyBankAccounts(ApiRequest request) throws ApiException;

	ApiResponse updatePrimaryBank(BankAccountRq request) throws ApiException;

	ApiResponse register3(RegisterationRq request) throws ApiException;

	ApiResponse depositEcleverPin(EcleverPinDepositRq request) throws ApiException;

	ApiResponse depositPayStackAuth(PayStackDepositRq request) throws ApiException;

	ApiResponse listCardAuthTokens(ApiRequest request) throws ApiException;

	ApiResponse depositPayStackAuthLastUsedCardToken(PayStackDepositRq request) throws ApiException;

	ApiResponse rechargeWeb(RechargeRetailWebRq request, boolean isTest) throws ApiException;
}
