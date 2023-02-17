/**
 * 
 */
package com.demus.api.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.demus.api.ex.ApiException;
import com.demus.api.io.ApiResponse;
import com.demus.entity.ActivateTokenType;
import com.demus.entity.ActivationToken;
import com.demus.entity.Address;
import com.demus.entity.BankAccount;
import com.demus.entity.BankOperator;
import com.demus.entity.Country;
import com.demus.entity.Device;
import com.demus.entity.DeviceOs;
import com.demus.entity.EntityStatus;
import com.demus.entity.Order;
import com.demus.entity.PaymentAuthorizationToken;
import com.demus.entity.Permission;
import com.demus.entity.Product;
import com.demus.entity.ProductCategory;
import com.demus.entity.PromoCode;
import com.demus.entity.Refer;
import com.demus.entity.Role;
import com.demus.entity.RolePermission;
import com.demus.entity.State;
import com.demus.entity.Subscriber;
import com.demus.entity.SubscriberRole;
import com.demus.entity.Subscription;
import com.demus.entity.Vendor;
import com.demus.entity.Wallet;
import com.demus.entity.WalletTransaction;


/**
 * Api for all administration functions
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public interface ModelApi {
	
	ApiResponse activationTokens (Filter filter) throws ApiException;
	
	ApiResponse deleteActivationToken (List<Long> tokenIds) throws ApiException;
	
	ApiResponse getActivationToken (Subscriber subscriber, String token, ActivateTokenType type) throws ApiException;
	
	ApiResponse deleteActivationTokens (Subscriber subscriber, ActivateTokenType type) throws ApiException;
	
	ApiResponse addresses (Filter filter) throws ApiException;
	
	ApiResponse deleteAddress (List<Long> id) throws ApiException;
	
	ApiResponse putAddress (Address address) throws ApiException;
	
	ApiResponse getAddress (Long id) throws ApiException;
	
	ApiResponse bankAccounts (Filter filter) throws ApiException;
	
	ApiResponse deleteBankAccount (List<Long> id) throws ApiException;
	
	ApiResponse putBankAccount (BankAccount address) throws ApiException;
	
	ApiResponse getBankAccount (Long id) throws ApiException;
	
	ApiResponse bankOperators (Filter filter) throws ApiException;
	
	ApiResponse deleteBankOperator (List<Long> id) throws ApiException;
	
	ApiResponse putBankOperator (BankOperator operator) throws ApiException;
	
	ApiResponse getBankOperator (Long id) throws ApiException;
	
	ApiResponse getBankOperator (String bankOperatorCode) throws ApiException;
	
	ApiResponse countrys (Filter filter) throws ApiException;
	
	ApiResponse deleteCountry (List<Long> id) throws ApiException;
	
	ApiResponse putCountry (Country country) throws ApiException;
	
	ApiResponse getCountry (Long id) throws ApiException;
	
	ApiResponse devices (Filter filter) throws ApiException;
	
	ApiResponse deleteDevice (List<Long> id) throws ApiException;
	
	ApiResponse putDevice (Device device) throws ApiException;
	
	ApiResponse getDeviceClientId (String clientId) throws ApiException;
	
	ApiResponse getDevice (Long id) throws ApiException;
	
	ApiResponse orders (Filter filter) throws ApiException;
	
	ApiResponse deleteOrder (List<Long> id) throws ApiException;
	
	ApiResponse putOrder (Order order) throws ApiException;
	
	ApiResponse getOrder (Long id) throws ApiException;
	
	ApiResponse paymentAuthorizationTokens (Filter filter) throws ApiException;
	
	ApiResponse deletePaymentAuthorizationToken (List<Long> id) throws ApiException;
	
	ApiResponse putPaymentAuthorizationToken (PaymentAuthorizationToken token) throws ApiException;
	
	ApiResponse getPaymentAuthorizationToken (Long id) throws ApiException;
	
	ApiResponse deleteProduct (List<Long> id) throws ApiException;
	
	ApiResponse putProduct (Product product) throws ApiException;
	
	ApiResponse getProduct (Long id) throws ApiException;
	
	ApiResponse productCategorys (Filter filter) throws ApiException;
	
	ApiResponse deleteProductCategory (List<Long> id) throws ApiException;
	
	ApiResponse putProductCategory (ProductCategory productCategory) throws ApiException;
	
	ApiResponse getProductCategory (Long id) throws ApiException;
	
	ApiResponse promoCodes (Filter filter) throws ApiException;
	
	ApiResponse deletePromoCode (List<Long> id) throws ApiException;
	
	ApiResponse putPromoCode (PromoCode promoCode) throws ApiException;
	
	ApiResponse getPromoCode (Long id) throws ApiException;
	
	ApiResponse Roles (Filter filter) throws ApiException;
	
	ApiResponse deleteRole (List<Long> id) throws ApiException;
	
	ApiResponse putRole (Role Role) throws ApiException;
	
	ApiResponse getRole (Long id) throws ApiException;
	
	ApiResponse States (Filter filter) throws ApiException;
	
	ApiResponse deleteState (List<Long> id) throws ApiException;
	
	ApiResponse putState (State State) throws ApiException;
	
	ApiResponse getState (Long id) throws ApiException;
	
	ApiResponse Subscribers (Filter filter) throws ApiException;
	
	ApiResponse deleteSubscriber (List<Long> id) throws ApiException;
	
	ApiResponse putSubscriber (Subscriber Subscriber) throws ApiException;
	
	ApiResponse getSubscriber (Long id) throws ApiException;
	
	ApiResponse getSubscriberByPhonenumber (String phoneNumber, EntityStatus status) throws ApiException;
	
	ApiResponse getSubscriberByEmail(String email) throws ApiException;
	
	ApiResponse getFacebooker (Filter facebookId) throws ApiException;
	
	ApiResponse getGoogler (Filter googleId) throws ApiException;
	
	ApiResponse SubscriberRoles (Filter filter) throws ApiException;
	
	ApiResponse deleteSubscriberRole (List<Long> id) throws ApiException;
	
	ApiResponse putSubscriberRole (SubscriberRole SubscriberRole) throws ApiException;
	
	ApiResponse getSubscriberRole (Long id) throws ApiException;
	
	ApiResponse Subscriptions (Filter filter) throws ApiException;
	
	ApiResponse deleteSubscription (List<Long> id) throws ApiException;
	
	ApiResponse putSubscription (Subscription Subscription) throws ApiException;
	
	ApiResponse getSubscription (Long id) throws ApiException;
	
	ApiResponse Vendors (Filter filter) throws ApiException;
	
	ApiResponse deleteVendor (List<Long> id) throws ApiException;
	
	ApiResponse putVendor (Vendor Vendor) throws ApiException;
	
	ApiResponse getVendor (Long id) throws ApiException;
	
	ApiResponse Wallets (Filter filter) throws ApiException;
	
	ApiResponse deleteWallet (List<Long> id) throws ApiException;
	
	ApiResponse putRefer (Refer refer) throws ApiException;
	
	ApiResponse getRefer (String phoneNumber, EntityStatus entityStatus) throws ApiException;
	
	ApiResponse getRefer (Subscriber subscriber) throws ApiException;
	
	ApiResponse getRefer (WalletTransaction walletTransaction) throws ApiException;
	
	ApiResponse putWallet (Wallet Wallet) throws ApiException;
	
	ApiResponse getWallet (Long id) throws ApiException;
	
	ApiResponse getWallet(String id, EntityStatus entityStatus) throws ApiException;

	ApiResponse WalletTransactions (Filter filter) throws ApiException;
	
	ApiResponse deleteWalletTransaction (List<Long> id) throws ApiException;
	
	ApiResponse putWalletTransaction (WalletTransaction WalletTransaction) throws ApiException;
	
	ApiResponse getWalletTransaction (Long id) throws ApiException;

	ApiResponse getPermission(Long id) throws ApiException;

	ApiResponse putPermission(Permission permission) throws ApiException;

	ApiResponse deletePermission(List<Long> id) throws ApiException;

	ApiResponse permissions(Filter filter) throws ApiException;

	ApiResponse getRolePermission(Long id) throws ApiException;

	ApiResponse putRolePermission(RolePermission rolePermission) throws ApiException;

	ApiResponse deleteRolePermission(List<Long> id) throws ApiException;

	ApiResponse rolePermissions(Filter filter) throws ApiException;

	ApiResponse getDevice(String clientId, DeviceOs clientOs)  throws ApiException;
	
	ApiResponse getDevice(String clientId, DeviceOs clientOs, Long long1)  throws ApiException;
	
	ApiResponse getProduct (String productCode, EntityStatus status) throws ApiException;

	ApiResponse getPromoCode(String promoCode, EntityStatus status) throws ApiException;

	ApiResponse getWalletTransaction(Subscriber subscriber, Date startDate, Date endDate, Integer page, Integer size);
	
	ApiResponse getWalletTransaction(String transactionId, EntityStatus status) throws ApiException;

	ApiResponse getBankAccount(BankOperator operator, String accountNumber, Subscriber subscriber) throws ApiException;

	ApiResponse getBankAccount(Subscriber subscriber);

	ApiResponse putActivationToken(ActivationToken activationToken);

	ApiResponse getRole(String code);

	ApiResponse averageSubscriptionByProduct(Map<String, Object> request);

	ApiResponse quantityOrdered(Map<String, Object> request);

	ApiResponse productRevenue(Map<String, Object> request);

	ApiResponse avgTransactions(Map<String, Object> request);

	ApiResponse sumTransactions(Map<String, Object> request);

	ApiResponse countSubscribers(Map<String, Object> request);

	ApiResponse getWalletTransaction(Subscriber subscriber, Date startDate, Date endDate);

	ApiResponse getWalletTransactionByRefrence(String reference);

	ApiResponse products(Filter filter) throws ApiException;

	ApiResponse getPaystackAuthCodes(Subscriber subscriber);

	
	
	
	
}
