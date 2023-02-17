/**
 * 
 */
package com.demus.api.admin;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demus.api.ex.ApiException;
import com.demus.api.io.ApiRequestValidator;
import com.demus.api.io.ApiResponse;
import com.demus.api.io.ApiResponseStatus;
import com.demus.api.system.Config;
import com.demus.crud.ActivationTokenCrud;
import com.demus.crud.AddressCrud;
import com.demus.crud.BankAccountCrud;
import com.demus.crud.BankOperatorCrud;
import com.demus.crud.CountryCrud;
import com.demus.crud.DeviceCrud;
import com.demus.crud.OrderCrud;
import com.demus.crud.PaymentAuthorizationTokenCrud;
import com.demus.crud.PaystackAuthorizationTokenCrud;
import com.demus.crud.PermissionCrud;
import com.demus.crud.ProductCategoryCrud;
import com.demus.crud.ProductCrud;
import com.demus.crud.PromoCodeCrud;
import com.demus.crud.ReferCrud;
import com.demus.crud.RoleCrud;
import com.demus.crud.RolePermissionCrud;
import com.demus.crud.StateCrud;
import com.demus.crud.SubscriberCrud;
import com.demus.crud.SubscriberRoleCrud;
import com.demus.crud.SubscriptionCrud;
import com.demus.crud.VendorCrud;
import com.demus.crud.WalletCrud;
import com.demus.crud.WalletTransactionCrud;
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
import com.demus.entity.WalletTransactionStatus;
import com.demus.entity.WalletTransactionType;

/**
 * Crud Model Interface
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@RestController
@RequestMapping(value = "/backend")
public class ModelApiImpl implements ModelApi {
	
	@Autowired
	ActivationTokenCrud activationTokenCrud;
	
	@Override
	public ApiResponse putActivationToken (ActivationToken activationToken) {
		try {
			required("Activation Token", activationToken);
			activationToken = activationTokenCrud.save(activationToken);
			return success("Activation Token saved", activationToken);
		} catch (Exception e) {
			return failure(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#activationTokens()
	 */
	@Override
	@RequestMapping(value = "/activationtokens", method = RequestMethod.GET)
	public ApiResponse activationTokens(Filter filter)  throws ApiException {
		try {
			return success(null, activationTokenCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteActivationToken(java.util.List)
	 */
	@Override
	@RequestMapping(value = "/activationtoken", method = RequestMethod.DELETE)
	public ApiResponse deleteActivationToken(@RequestBody List<Long> tokenIds)  throws ApiException {
		try {
			validate("token ids", tokenIds);
			activationTokenCrud.deleteInBatch(tokenIds);
			return success ("Deleted", "");
		} catch (Exception e) {
			return failure (e);
		}
	}
	


	@Override
	public ApiResponse getActivationToken(Subscriber subscriber, String token, ActivateTokenType type) throws ApiException {
		try {
			required("Token", token);
			required("Token type", type);
			ActivationToken activationToken = null;
			if (subscriber != null) {
				activationToken = activationTokenCrud.findBySubscriberAndValueAndTypeAndExpiryDateGreaterThanEqual (subscriber, token, type, new Date());
			} else {
				activationToken = activationTokenCrud.findByValueAndTypeAndExpiryDateGreaterThanEqual(token, type, new Date());
			}
			return success("", activationToken);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	private void validate (String name, List<Long> ids) {
		required(name, ids);
		ApiRequestValidator.valMinSize(name, ids, 1);
	}
	
	@Autowired
	private AddressCrud addressCrud;
	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#addresses(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/addresses", method = RequestMethod.GET)
	public ApiResponse addresses(Filter filter)  throws ApiException {
		try {
			return success ("", addressCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteAddress(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/address", method = RequestMethod.DELETE)
	public ApiResponse deleteAddress(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate("address ids", id);
			addressCrud.recycleBin(id);
			return success ("Deleted", null);
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putAddress(com.demus.entity.Address)
	 */
	@Override
	@RequestMapping(value = "/address", method = RequestMethod.POST)
	public ApiResponse putAddress(@RequestBody Address address)  throws ApiException {
		try {
			required("address", address);
			required("subscriber", address.getSubscriber());
			required("address", address.getAddress());
			required("subscriber id", address.getSubscriber().getId());
			required("state", address.getState());
			required("state id", address.getState().getId());
			ApiRequestValidator.valMaxSize ("address information", address.getAddress(), Config.MAX_ADDRESS);
			return success ("", addressCrud.save(address));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getAddress(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/address", method = RequestMethod.GET)
	public ApiResponse getAddress(Long id)  throws ApiException {
		try {
			required("address id", id);
			return success ("", addressCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	
	@Autowired
	private BankAccountCrud bankAccountCrud;
	/**
	 * List all bank accounts in hte system
	 * @param filter
	 * @return
	 * @throws ApiException
	 */
	@Override
	@RequestMapping(value = "/bankaccounts", method = RequestMethod.GET)
	public ApiResponse bankAccounts(Filter filter)  throws ApiException {
		try {
			return success ("", bankAccountCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteBankAccount(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/bankaccount", method = RequestMethod.DELETE)
	public ApiResponse deleteBankAccount(@RequestBody List<Long> ids)  throws ApiException {
		try {
			validate("bank ids", ids);
			bankAccountCrud.recycleBin(ids);
			return success ("Deleted", null);
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putBankAccount(com.demus.entity.BankAccount)
	 */
	@Override
	@RequestMapping(value = "/bankaccount", method = RequestMethod.POST)
	public ApiResponse putBankAccount(@RequestBody BankAccount bankAccount)  throws ApiException {
		try {
			required("bank account", bankAccount);
			required("account number", bankAccount.getAccountNumber());
			required("bank operator", bankAccount.getBankOperator());
			required("bank operator id", bankAccount.getBankOperator().getId());
			required("subscriber", bankAccount.getSubscriber());
			required("subscriber id", bankAccount.getSubscriber().getId());
			required("is primary account", bankAccount.getIsPrimary());
			ApiRequestValidator.valMaxSize("account number", bankAccount.getAccountNumber(), Config.MAX_ACCOUNT_NUMBER);
			return success ("", bankAccountCrud.save(bankAccount));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getBankAccount(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/bankaccount", method = RequestMethod.GET)
	public ApiResponse getBankAccount(Long id)  throws ApiException {
		try {
			required("bank account id", id);
			return success ("", bankAccountCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Override
	public ApiResponse getBankAccount(Subscriber subscriber) {
		try {
			required("subscriber", subscriber);
			return success ("", bankAccountCrud.findBySubscriber(subscriber));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private PaystackAuthorizationTokenCrud paystackAuthorizationTokenCrud;
	@Override
	public ApiResponse getPaystackAuthCodes(Subscriber subscriber) {
		try {
			required("subscriber", subscriber);
			return success ("", paystackAuthorizationTokenCrud.findBySubscriber(subscriber));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Override
	public ApiResponse getBankAccount(BankOperator operator, String accountNumber, Subscriber subscriber)  throws ApiException {
		try {
			required("bank account operator", operator);
			required("bank account number", accountNumber);
			required("bank account subscriber", subscriber);
			return success ("", bankAccountCrud.findBySubscriberAndAccountNumberAndBankOperatorAndEntityStatus(subscriber, accountNumber, operator, EntityStatus.ACTIVE));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	BankOperatorCrud bankOperatorCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#bankOperators(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/bankoperators", method = RequestMethod.GET)
	public ApiResponse bankOperators(Filter filter)  throws ApiException {
		try {
			if (filter.getMatch() != null) {
				return success("", bankOperatorCrud.findByBankOperatorCodeLikeIgnoreCaseOrNameLikeIgnoreCase (filter.getMatch(), filter.getMatch(), filter(filter)));
			}
			return success ("", bankOperatorCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteBankOperator(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/bankoperator", method = RequestMethod.DELETE)
	public ApiResponse deleteBankOperator(@RequestBody List<Long> id)  throws ApiException {
		try {
			ApiRequestValidator.valMinSize("ids", id, 1);
			bankOperatorCrud.recycleBin(id);
			return deleted ();
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	private ApiResponse deleted () {
		return success("Deleted", null);
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putBankOperator(com.demus.entity.BankOperator)
	 */
	@Override
	@RequestMapping(value = "/bankoperator", method = RequestMethod.POST)
	public ApiResponse putBankOperator(@RequestBody BankOperator operator)  throws ApiException {
		try {
			required("operator", operator);
			required("name", operator.getName());
			required("operator code", operator.getBankOperatorCode());
			required("country", operator.getCountry());
			required("country id", operator.getCountry().getId());
			return success ("", bankOperatorCrud.save(operator));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getBankOperator(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/bankoperator", method = RequestMethod.GET)
	public ApiResponse getBankOperator(Long id)  throws ApiException {
		try {
			required("id", id);
			return success ("", bankOperatorCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	

	@Override
	public ApiResponse getBankOperator(String bankOperatorCode) throws ApiException {
		try {
			required("Bank Operator Code", bankOperatorCode);
			return success ("", bankOperatorCrud.findByBankOperatorCode(bankOperatorCode));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private CountryCrud countryCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#countrys(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/countries", method = RequestMethod.GET)
	public ApiResponse countrys(Filter filter)  throws ApiException {
		try {		
			if (filter.getMatch() != null ) {
				return success ("", countryCrud.findByNameContainsIgnoreCase(filter.getMatch(), filter(filter)));
			}
			return success ("", countryCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteCountry(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/country", method = RequestMethod.DELETE)
	public ApiResponse deleteCountry(@RequestBody List<Long> id) throws ApiException {
		try {
			ApiRequestValidator.valMinSize("ids", id, 1);
			countryCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putCountry(com.demus.entity.Country)
	 */
	@Override
	@RequestMapping(value = "/country", method = RequestMethod.POST)
	public ApiResponse putCountry(@RequestBody Country country)  throws ApiException {
		try {
			required("country", country);
			required("country code", country.getCountryCode());
			required("country currency", country.getCurrency());
			required("country", country.getIntCallingCode());
			required("country name", country.getName());
			return success ("", countryCrud.save(country));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getCountry(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public ApiResponse getCountry(Long id)  throws ApiException {
		try {
			required("id", id);
			return success ("", countryCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private DeviceCrud deviceCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#devices(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/devices", method = RequestMethod.GET)
	public ApiResponse devices(Filter filter)  throws ApiException {
		try {
			return success ("", deviceCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteDevice(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/device", method = RequestMethod.DELETE)
	public ApiResponse deleteDevice(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate (id);
			deviceCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	private void validate (List<Long> id) {
		ApiRequestValidator.valMinSize("id", id, 1);
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putDevice(com.demus.entity.Device)
	 */
	@Override
	@RequestMapping(value = "/device", method = RequestMethod.POST)
	public ApiResponse putDevice(@RequestBody Device device)  throws ApiException {
		try {
			required("device", device);
			required("device os", device.getDeviceOs());
			required("subscriber", device.getSubscriber());
			required("subscriber id", device.getSubscriber().getId());
			required("version", device.getVersion());
			return success ("", deviceCrud.save(device));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getDevice(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/device", method = RequestMethod.GET)
	public ApiResponse getDevice(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", deviceCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	private void validate (Long id) {
		required("id", id);
	}
	
	@Autowired
	private OrderCrud orderCrud;
	
	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#orders(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public ApiResponse orders(Filter filter)  throws ApiException {
		try {
			if (filter.getMatch() != null)
				return success("", orderCrud.findByOrderId(filter.getMatch(), filter(filter)));
			return success ("", orderCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteOrder(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/order", method = RequestMethod.DELETE)
	public ApiResponse deleteOrder(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			orderCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putOrder(com.demus.entity.Order)
	 */
	@Override
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	public ApiResponse putOrder(@RequestBody Order order)  throws ApiException {
		try {
			required("order", order);
			required("cost", order.getCost());
			required("order status", order.getOrderStatus());
			required("subscriber", order.getSubscriber());
			required("subscriber id", order.getSubscriber().getId());
			return success ("", orderCrud.save(order));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	private void required (String name, Object object) {
		ApiRequestValidator.notNull(name, object);
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getOrder(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public ApiResponse getOrder(Long id)  throws ApiException {
		try {
			required("id", id);
			return success ("", orderCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}

	@Autowired
	private PaymentAuthorizationTokenCrud paymentAuthorizationTokenCrud;
	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#paymentAuthorizationTokens(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/paymentauthorizationtokens", method = RequestMethod.GET)
	public ApiResponse paymentAuthorizationTokens(Filter filter)  throws ApiException {
		try {
			return success ("", paymentAuthorizationTokenCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deletePaymentAuthorizationToken(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/paymentauthorizationtoken", method = RequestMethod.DELETE)
	public ApiResponse deletePaymentAuthorizationToken(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			paymentAuthorizationTokenCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putPaymentAuthorizationToken(com.demus.entity.PaymentAuthorizationToken)
	 */
	@Override
	@RequestMapping(value = "/paymentauthorizationtoken", method = RequestMethod.POST)
	public ApiResponse putPaymentAuthorizationToken(@RequestBody PaymentAuthorizationToken token)  throws ApiException {
		try {
			required("token", token);
			required("expiry date", token.getExpiryDate());
			// Wallet Transaction required
			required("wallet transaction", token.getTransaction());
			return success ("", paymentAuthorizationTokenCrud.save(token));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getPaymentAuthorizationToken(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/paymentauthorizationtoken", method = RequestMethod.GET)
	public ApiResponse getPaymentAuthorizationToken(Long id)  throws ApiException {
		try {
			required("token id", id);
			return success ("", paymentAuthorizationTokenCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	ProductCrud productCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#products(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public ApiResponse products(Filter filter)  throws ApiException {
		try {
			if (filter.getMatch() != null) {
				return success("", productCrud.findByProductCodeContainsOrNameContainsIgnoreCase (filter.getMatch(), filter.getMatch(), filter(filter)));
			}
			return success ("", productCrud.findAll(filter(filter)).getContent());
			//return success ("", productCrud.findAll());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteProduct(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/product", method = RequestMethod.DELETE)
	public ApiResponse deleteProduct(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			productCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putProduct(com.demus.entity.Product)
	 */
	@Override
	@RequestMapping(value = "/product", method = RequestMethod.POST)
	public ApiResponse putProduct(@RequestBody Product product)  throws ApiException {
		try {
			required("product", product);
			required("category", product.getCategory());
			required("country", product.getCountry());
			required("country id", product.getCountry().getId());
			required("price", product.getPrice());
			required("product code", product.getProductCode());
			required("stock count", product.getStockCount());
			required("name", product.getName());
//			required("vendor", product.getVendor());
//			required("vendor id", product.getVendor().getId());
			return success ("", productCrud.save(product));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getProduct(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public ApiResponse getProduct(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", productCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private ProductCategoryCrud productCategoryCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#productCategorys(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/productcategories", method = RequestMethod.GET)
	public ApiResponse productCategorys(Filter filter)  throws ApiException {
		try {
			if (filter.getMatch() != null) {
				return success ("", productCategoryCrud.findByCategoryCodeLikeIgnoreCaseOrNameLikeIgnoreCase (filter.getMatch(), filter.getMatch(),filter(filter)));
			}
			return success ("", productCategoryCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteProductCategory(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/productcategory", method = RequestMethod.DELETE)
	public ApiResponse deleteProductCategory(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			productCategoryCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putProductCategory(com.demus.entity.ProductCategory)
	 */
	@Override
	@RequestMapping(value = "/productcategory", method = RequestMethod.POST)
	public ApiResponse putProductCategory(@RequestBody ProductCategory productCategory)  throws ApiException {
		try {
			required("category", productCategory);
			required("code", productCategory.getCategoryCode());
			required("name", productCategory.getName());
			required("order index", productCategory.getOrderIndex());
			if (productCategory.getParentCategory() != null) {
				required("parent id", productCategory.getParentCategory().getId());
			}
			return success ("", productCategoryCrud.save(productCategory));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getProductCategory(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/productcategory", method = RequestMethod.GET)
	public ApiResponse getProductCategory(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", productCategoryCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private PromoCodeCrud promoCodeCrud;
	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#promoCodes(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/promocodes", method = RequestMethod.GET)
	public ApiResponse promoCodes(Filter filter)  throws ApiException {
		try {
			return success ("", promoCodeCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deletePromoCode(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/promocode", method = RequestMethod.DELETE)
	public ApiResponse deletePromoCode(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			promoCodeCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putPromoCode(com.demus.entity.PromoCode)
	 */
	@Override
	@RequestMapping(value = "/promocode", method = RequestMethod.POST)
	public ApiResponse putPromoCode(@RequestBody PromoCode promoCode)  throws ApiException {
		try {
			required("promo code", promoCode);
			required("expiry date", promoCode.getExpiryDate());
			required("product", promoCode.getProduct());
			required("product id", promoCode.getProduct().getId());
			required("type", promoCode.getType());
			return success ("", promoCodeCrud.save(promoCode));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getPromoCode(java.lang.Long)
	 */
	@Override	
	@RequestMapping(value = "/promocode", method = RequestMethod.GET)
	public ApiResponse getPromoCode(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", promoCodeCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private RoleCrud roleCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#Roles(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public ApiResponse Roles(Filter filter)  throws ApiException {
		try {
			if (filter.getMatch() != null) {
				return success("", roleCrud.findByNameContainsIgnoreCase (filter.getMatch()));
			}
			return success ("", roleCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteRole(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/role", method = RequestMethod.DELETE)
	public ApiResponse deleteRole(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			roleCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putRole(com.demus.entity.Role)
	 */
	@Override
	@RequestMapping(value = "/role", method = RequestMethod.POST)
	public ApiResponse putRole(@RequestBody Role role)  throws ApiException {
		try {
			required("role", role);
			required("role", role.getName());
			required("code", role.getRoleCode());
			List<Permission> permissions = role.savePermissions;
			ApiRequestValidator.valMinSize("permissions", permissions, 1);
			role = roleCrud.saveAndFlush(role);
			List<RolePermission> rolePermissions = new ArrayList<RolePermission>();
			for (Permission permission : permissions) {
				if (rolePermissionCrud.findByRoleAndPermission(role, permission) == null) {
					RolePermission rp = new RolePermission();
					rp.setRole(role);
					rp.setPermission(permission);
					rolePermissions.add(rp);
					p (role.getId() + " " + permission.getId());
				}
			}
			
			rolePermissionCrud.save(rolePermissions);
			return success ("", role);
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	private void p (Object o) {
		System.out.println(o);
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getRole(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public ApiResponse getRole(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", roleCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	 
	@Override
	public ApiResponse getRole(String code) {
		try {
			required("Role code", code);
			return success ("", roleCrud.findByRoleCode(code));
		} catch (Exception e) {
			return failure (e);
		}
	}

	@Autowired
	private StateCrud stateCrud;
	
	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#States(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/states", method = RequestMethod.GET)
	public ApiResponse States(Filter filter)  throws ApiException {
		try {
			return success ("", stateCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteState(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/state", method = RequestMethod.DELETE)
	public ApiResponse deleteState(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			stateCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putState(com.demus.entity.State)
	 */
	@Override
	@RequestMapping(value = "/state", method = RequestMethod.POST)
	public ApiResponse putState(@RequestBody State state)  throws ApiException {
		try {
			required("state", state);
			required("country", state.getCountry());
			required("country id", state.getCountry().getId());
			required("code", state.getStateCode());
			required("name", state.getName());
			return success ("", stateCrud.save(state));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getState(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/state", method = RequestMethod.GET)
	public ApiResponse getState(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", stateCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}

	@Autowired
	private SubscriberCrud subscriberCrud;
	
	@Override
	@RequestMapping(value = "/subscribers/count", method = RequestMethod.GET)
	public ApiResponse countSubscribers (Map<String, Object> request) {
		try {
			Date startDate = new Date ((Long) request.get("startDate"));
			Date endDate = new Date((Long) request.get("endDate"));
			Long count = subscriberCrud.countByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(startDate, endDate);
			return success("", count);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#Subscribers(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/subscribers", method = RequestMethod.GET)
	public ApiResponse Subscribers(Filter filter)  throws ApiException {
		try {
			if (filter.getMatch() != null) {
				String match = filter.getMatch();
				return success ("", subscriberCrud.findByNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCaseOrEmailContainsIgnoreCase(match, match, match, filter(filter)));
			} else if (filter.startDate != null && filter.endDate != null) {
				return success("", subscriberCrud.findByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(filter.startDate, filter.endDate));
			}
			return success ("", subscriberCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteSubscriber(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/subscriber", method = RequestMethod.DELETE)
	public ApiResponse deleteSubscriber(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			subscriberCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	private void throwMsg (Boolean test, String message) throws ApiException {
		if (test)
			throw new ApiException (message);
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putSubscriber(com.demus.entity.Subscriber)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@RequestMapping(value = "/subscriber", method = RequestMethod.POST)
	public ApiResponse putSubscriber(@RequestBody Subscriber subscriber)  throws ApiException {
		try {
			required("subscriber", subscriber);
			required("subscriber email", subscriber.getEmail());
			required("name", subscriber.getName());
			throwMsg(subscriber.getId() == null && subscriber.getPassword() == null, "Password is required");
			required("phone number", subscriber.getPhoneNumber());
			Long id = subscriber.getId();
			if (id != null) {
				Subscriber previous = (Subscriber) ((List) getSubscriber(id).results).get(0);
				subscriber.setEncryptedPassword(previous.getPassword());
				subscriber.setEncryptedPin(previous.getPin());
			}		
			ApiRequestValidator.valFixedSize("phone number", subscriber.getPhoneNumber(), 11);
			return success ("", subscriberCrud.save(subscriber));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getSubscriber(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/subscriber", method = RequestMethod.GET)
	public ApiResponse getSubscriber(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", subscriberCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private SubscriberRoleCrud subscriberRoleCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#SubscriberRoles(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/subscriberroles", method = RequestMethod.GET)
	public ApiResponse SubscriberRoles(Filter filter)  throws ApiException {
		try {
			return success ("", subscriberRoleCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteSubscriberRole(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/subscriberrole", method = RequestMethod.DELETE)
	public ApiResponse deleteSubscriberRole(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			subscriberRoleCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putSubscriberRole(com.demus.entity.SubscriberRole)
	 */
	@Override
	@RequestMapping(value = "/subscriberrole", method = RequestMethod.POST)
	public ApiResponse putSubscriberRole(@RequestBody SubscriberRole subscriberRole)  throws ApiException {
		try {
			required("subscriber role", subscriberRole);
			required("role", subscriberRole.getRole());
			required("role id", subscriberRole.getRole().getId());
			required("subscriber", subscriberRole.getSubscriber());
			required("subscriber id", subscriberRole.getSubscriber().getId());
			return success ("", subscriberRoleCrud.save(subscriberRole));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getSubscriberRole(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/subscriberrole", method = RequestMethod.GET)
	public ApiResponse getSubscriberRole(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", subscriberRoleCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private SubscriptionCrud subscriptionCrud;
	
	/**
	 * Reporting
	 */
	@Override
	@RequestMapping(value = "/subscriptions/average", method = RequestMethod.GET)
	public ApiResponse averageSubscriptionByProduct (Map<String, Object> request) {
		try {
			Long startDate = (Long) request.get("startDate");
			Long endDate = (Long) request.get("endDate");
			Long productId = (Long) request.get("productId");
			Date  sDate = new Date(startDate);
			Date  eDate = new Date(endDate);
			Product product = new Product(productId);
			EntityStatus status = EntityStatus.valueOf((String) request.get("status"));
			Double avg = subscriptionCrud.averageOrderSize(product, status , sDate, eDate);
			return success("Average product subscription processed", avg);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@Override
	@RequestMapping(value = "/subscriptions/quantity", method = RequestMethod.GET)
	public ApiResponse quantityOrdered (Map<String, Object> request) {
		try {
			Long startDate = (Long) request.get("startDate");
			Long endDate = (Long) request.get("endDate");
			Long productId = (Long) request.get("productId");
			Date  sDate = new Date(startDate);
			Date  eDate = new Date(endDate);
			Product product = new Product(productId);
			EntityStatus status = EntityStatus.valueOf((String) request.get("status"));
			Double avg = subscriptionCrud.averageOrderSize(product, status , sDate, eDate);
			return success("Subscription quantity by product processed", avg);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@Override
	@RequestMapping(value = "/subscriptions/revenue", method = RequestMethod.GET)
	public ApiResponse productRevenue (Map<String, Object> request) {
		try {
			Long startDate = (Long) request.get("startDate");
			Long endDate = (Long) request.get("endDate");
			Long productId = (Long) request.get("productId");
			Date  sDate = new Date(startDate);
			Date  eDate = new Date(endDate);
			Product product = new Product(productId);
			EntityStatus status = EntityStatus.valueOf((String) request.get("status"));
			Double avg = subscriptionCrud.sumRevenue(product, status , sDate, eDate);
			return success("Revenue by product processed", avg);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#Subscriptions(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
	public ApiResponse Subscriptions(Filter filter)  throws ApiException {
		try {
			return success ("", subscriptionCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteSubscription(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/subscription", method = RequestMethod.DELETE)
	public ApiResponse deleteSubscription(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			subscriptionCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putSubscription(com.demus.entity.Subscription)
	 */
	@Override
	@RequestMapping(value = "/subscription", method = RequestMethod.POST)
	public ApiResponse putSubscription(@RequestBody Subscription subscription)  throws ApiException {
		try {
			required("subscription", subscription);
			if (subscription.getId() == null) 
				required("cost", subscription.getCost());
			required("order", subscription.getOrder());
			required("order id", subscription.getOrder().getId());
			required("product", subscription.getProduct());
			required("product id", subscription.getProduct().getId());
			required("quantity", subscription.getQuantity());
			return success ("", subscriptionCrud.save(subscription));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getSubscription(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/subscription", method = RequestMethod.GET)
	public ApiResponse getSubscription(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", subscriptionCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private VendorCrud vendorCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#Vendors(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/vendors", method = RequestMethod.GET)
	public ApiResponse Vendors(Filter filter)  throws ApiException {
		try {
			if (filter.getMatch() != null) {
				return success ("", vendorCrud.findByNameLike(filter.getMatch()));
			}
			return success ("", vendorCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteVendor(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/vendor", method = RequestMethod.DELETE)
	public ApiResponse deleteVendor(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			vendorCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putVendor(com.demus.entity.Vendor)
	 */
	@Override
	@RequestMapping(value = "/vendor", method = RequestMethod.POST)
	public ApiResponse putVendor(@RequestBody Vendor vendor)  throws ApiException {
		try {
			required("vendor", vendor);
			required("vendor url", vendor.getApiUrl());
			required("vendor name", vendor.getName());
			required("code", vendor.getVendorCode());
			return success ("", vendorCrud.save(vendor));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getVendor(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/vendor", method = RequestMethod.GET)
	public ApiResponse getVendor(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", vendorCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private WalletCrud walletCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#Wallets(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/wallets", method = RequestMethod.GET)
	public ApiResponse Wallets(Filter filter)  throws ApiException {
		try {
			return success ("", walletCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteWallet(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/wallet", method = RequestMethod.DELETE)
	public ApiResponse deleteWallet(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			walletCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putWallet(com.demus.entity.Wallet)
	 */
	@Override
	@RequestMapping(value = "/wallet", method = RequestMethod.POST)
	public ApiResponse putWallet(@RequestBody Wallet wallet)  throws ApiException {
		try {
			required("wallet", wallet);
			required("balance", wallet.getBalance());
			required("subscriber", wallet.getSubscriber());
			required("subscriber id", wallet.getSubscriber().getId());
			required("wallet id", wallet.getWalletId());
			return success ("", walletCrud.save(wallet));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getWallet(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/wallet", method = RequestMethod.GET)
	public ApiResponse getWallet(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", walletCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private WalletTransactionCrud walletTransactionCrud;
	
	@Override
	public ApiResponse getWalletTransactionByRefrence (String reference) {
		try {
			return success("Wallet Transaction", walletTransactionCrud.findByReference(reference));
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	
	@Override
	@RequestMapping(value = "/wallettransactions/average", method = RequestMethod.GET)
	public ApiResponse avgTransactions (Map<String, Object> request) {
		try {
			WalletTransactionType type = WalletTransactionType.valueOf((String) request.get("type"));
			WalletTransactionStatus status = WalletTransactionStatus.valueOf((String) request.get("status"));
			Date startDate = new Date((Long) request.get("startDate"));
			Date endDate = new Date((Long) request.get("endDate"));
			Double avg = walletTransactionCrud.avg(type, status, startDate, endDate);
			return success("Average transactions returned", avg);
 		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@Override
	@RequestMapping(value = "/wallettransactions/sum", method = RequestMethod.GET)
	public ApiResponse sumTransactions (Map<String, Object> request) {
		try {
			WalletTransactionType type = WalletTransactionType.valueOf((String) request.get("type"));
			WalletTransactionStatus status = WalletTransactionStatus.valueOf((String) request.get("status"));
			Date startDate = new Date((Long) request.get("startDate"));
			Date endDate = new Date((Long) request.get("endDate"));
			Double avg = walletTransactionCrud.sum(type, status, startDate, endDate);
			return success("Sum transactions returned", avg);
 		} catch (Exception e) {
			return failure(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#WalletTransactions(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/wallettransactions", method = RequestMethod.GET)
	public ApiResponse WalletTransactions(Filter filter)  throws ApiException {
		try {
			if (filter.getMatch() != null) {
				return success("", walletTransactionCrud.findByTransactionId(filter.getMatch(), filter(filter)));
			}
			return success ("", walletTransactionCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteWalletTransaction(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/wallettransaction", method = RequestMethod.DELETE)
	public ApiResponse deleteWalletTransaction(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			walletTransactionCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putWalletTransaction(com.demus.entity.WalletTransaction)
	 */
	@Override
	@RequestMapping(value = "/wallettransaction", method = RequestMethod.POST)
	public ApiResponse putWalletTransaction(@RequestBody WalletTransaction walletTransaction)  throws ApiException {
		try {
			required("wallet transaction", walletTransaction);
			required("amount", walletTransaction.getAmount());
			required("payment gateway", walletTransaction.getPaymentGateway());
			required("transaction id", walletTransaction.getTransactionId());
			required("type", walletTransaction.getType());
			required("wallet", walletTransaction.getWallet());
			required("wallet id", walletTransaction.getWallet().getId());
			return success ("", walletTransactionCrud.save(walletTransaction));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getWalletTransaction(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/wallettransaction", method = RequestMethod.GET)
	public ApiResponse getWalletTransaction(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", walletTransactionCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private PermissionCrud permissionCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#Permissions(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/permissions", method = RequestMethod.GET)
	public ApiResponse permissions(Filter filter)  throws ApiException {
		try {
			if (filter.getMatch() != null) {
				return success("", permissionCrud.findByNameLikeIgnoreCase (filter.getMatch(), filter(filter)));
			}
			return success ("", permissionCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deletePermission(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/permission", method = RequestMethod.DELETE)
	public ApiResponse deletePermission(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			permissionCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putPermission(com.demus.entity.Permission)
	 */
	@Override
	@RequestMapping(value = "/permission", method = RequestMethod.POST)
	public ApiResponse putPermission(@RequestBody Permission permission)  throws ApiException {
		try {
			required("permission", permission);
			required("name", permission.getName());
			required("description", permission.getName());
			return success ("", permissionCrud.save(permission));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getPermission(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/permission", method = RequestMethod.GET)
	public ApiResponse getPermission(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", permissionCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@Autowired
	private RolePermissionCrud rolePermissionCrud;

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#RolePermissions(com.demus.api.admin.Filter)
	 */
	@Override
	@RequestMapping(value = "/rolepermissions", method = RequestMethod.GET)
	public ApiResponse rolePermissions(Filter filter)  throws ApiException {
		try {
			return success ("", rolePermissionCrud.findAll(filter(filter)).getContent());
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#deleteRolePermission(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/rolepermission", method = RequestMethod.DELETE)
	public ApiResponse deleteRolePermission(@RequestBody List<Long> id)  throws ApiException {
		try {
			validate(id);
			rolePermissionCrud.recycleBin(id);
			return deleted();
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#putRolePermission(com.demus.entity.RolePermission)
	 */
	@Override
	@RequestMapping(value = "/rolepermission", method = RequestMethod.POST)
	public ApiResponse putRolePermission(@RequestBody RolePermission rolePermission)  throws ApiException {
		try {
			required("role permission", rolePermission);
			required("role", rolePermission.getRole());
			required("role id", rolePermission.getRole().getId());
			required("permission", rolePermission.getPermission());
			required("permission id", rolePermission.getPermission().getId());
			return success ("", rolePermissionCrud.save(rolePermission));
		} catch (Exception e) {
			return failure (e);
		}
	}

	/* (non-Javadoc)
	 * @see com.demus.api.admin.ModelApi#getRolePermission(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/rolepermission", method = RequestMethod.GET)
	public ApiResponse getRolePermission(Long id)  throws ApiException {
		try {
			validate(id);
			return success ("", rolePermissionCrud.findOne(id));
		} catch (Exception e) {
			return failure (e);
		}
	}
	
	@SuppressWarnings("unchecked")
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
	
	private Logger logger = LoggerFactory.getLogger(ModelApiImpl.class);
	private ApiResponse failure(Exception e) {
		logger.warn(e.getMessage(), e);
		// remove in production
//		if (e instanceof IllegalArgumentException) {
//			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.BAD_REQUEST);
//		} else if (e instanceof ApiAccessDeniedException) {
//			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.DENIED);
//		} else if (e instanceof ConstraintViolationException) {
//			return new ApiResponse("Server could not persist data.", Collections.emptyList(), 0,
//					ApiResponseStatus.SERVER_ERROR);
//		} else if (e instanceof ClientExpiredException) {
//			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.UPDATE_REQUIRED);
//		} else if (e instanceof ApiAuthenticationException) {
//			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.DENIED); 
//		} else if (e instanceof AccessDeniedException) {
//			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.DENIED); 
//		} else if (e instanceof InvalidStateException) {
//			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.CANNOT_TRANSACT); 
//		} else if (e instanceof ApiException) {
//			return new ApiResponse(e.getMessage(), Collections.emptyList(), 0, ApiResponseStatus.SERVER_ERROR);
//		} else {
		final StringWriter sw = new StringWriter();
	    final PrintWriter pw = new PrintWriter(sw, true);
	    e.printStackTrace(pw);
	    String err = sw.getBuffer().toString();
	    
		return new ApiResponse("We experienced a server error with your request. Please try again. " + e.getMessage() + " > " + err,
				Collections.emptyList(), 0, ApiResponseStatus.SERVER_ERROR);
//		}
	}
	
	private PageRequest filter (Filter filter) {
		validateFilter(filter);
		validateFilter (filter);
		int size = filter.size;
		int page = filter.page;
		// TODO Test sort split : is an array of size two
		String sortParam = filter.sort.split(":")[0];
		String sortOrder = filter.sort.split(":")[1];
		Sort sort = new Sort(sortOrder.equals("DESC") ? Direction.DESC : Direction.ASC, sortParam);
		PageRequest request = new PageRequest(page, size, sort);
		return request;
	}
	
	private void validateFilter(Filter filter) {
		required("filter", filter);
	}

	@Override
	public ApiResponse getSubscriberByPhonenumber(String phoneNumber, EntityStatus status) throws ApiException {
		try {
			required("Phone number", phoneNumber);
			if (status!=  null)
				return success("", subscriberCrud.findByPhoneNumberAndEntityStatus(phoneNumber, EntityStatus.ACTIVE));
			else 
				return success("", subscriberCrud.findByPhoneNumber (phoneNumber));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getSubscriberByEmail(String email) throws ApiException {
		try {
			required("Email", email);
			return success("", subscriberCrud.findByEmail(email));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getFacebooker(Filter filter) throws ApiException {
		try {
			required("Filter", filter);
			String facebookId = filter.getMatch();
			required("FacebookId", facebookId);
			if (filter.getStatus() != null)
				return success("", subscriberCrud.findByFacebookIdAndEntityStatus(facebookId, EntityStatus.ACTIVE));
			else
				return success("", subscriberCrud.findByFacebookId (facebookId));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getGoogler(Filter filter) throws ApiException {
		try {
			required("Filter", filter);
			String GoogleId = filter.getMatch();
			required("GoogleId", GoogleId);
			if (filter.getStatus() != null)
				return success("", subscriberCrud.findByGoogleIdAndEntityStatus(GoogleId, EntityStatus.ACTIVE));
			else
				return success("", subscriberCrud.findByGoogleId (GoogleId));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getDeviceClientId(String clientId) throws ApiException {
		try {
			required("Client Id", clientId);
			Device device = deviceCrud.findByClientId(clientId);
			return success("", device);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getDevice(String clientId, DeviceOs clientOs) {
		try {
			required("client id", clientId);
			required("client os", clientOs);
			Device device = deviceCrud.findByClientIdAndDeviceOs(clientId, clientOs);
			return success("", device);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@Override
	public ApiResponse getDevice(String clientId, DeviceOs clientOs, Long subscriberId) {
		try {
			required("client id", clientId);
			required("client os", clientOs);
			required("subscriber Id", subscriberId);
			Device device = deviceCrud.findByClientIdAndDeviceOsAndSubscriberId(clientId, clientOs, subscriberId);
			return success("", device);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getProduct(String productCode, EntityStatus status) throws ApiException {
		try {
			required("product code", productCode);
			required("status", status);
			Product product = productCrud.findByProductCodeAndEntityStatus(productCode, status);
			return success("", product);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getPromoCode(String promoCode, EntityStatus status) throws ApiException {
		try {
			required("promo code", promoCode);
			return success("", promoCodeCrud.findByCodeAndEntityStatusAndExpiryDateLessThanEqual(promoCode, status, new Date()));
		} catch (Exception e) {
			return failure(e);	
		}
	}

	@Override
	public ApiResponse getWalletTransaction(Subscriber subscriber, Date startDate, Date endDate, Integer page, Integer size) {
		try {
			required("subscriber", subscriber);
			required("start date", startDate);
			required("end date", endDate);
			required("page", page);
			required("size", size);
			@SuppressWarnings("unchecked")
			Wallet wallet = ((List<Wallet>) getWallet(subscriber.getPhoneNumber(), null).results).get(0);
			PageRequest pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, "createdDate"));
			return success("", walletTransactionCrud.findByWalletAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualOrderByIdDesc(wallet, startDate, endDate, pageRequest));
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@Override
	public ApiResponse getWalletTransaction(Subscriber subscriber, Date startDate, Date endDate) {
		try {
			required("subscriber", subscriber);
			required("start date", startDate);
			required("end date", endDate);
			@SuppressWarnings("unchecked")
			Wallet wallet = ((List<Wallet>) getWallet(subscriber.getPhoneNumber(), null).results).get(0);
			return success("", walletTransactionCrud.accountStatement(startDate, endDate, wallet));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getWalletTransaction(String transactionId, EntityStatus status) throws ApiException {
		try {
			required("Transaction Id", transactionId);
			if (status != null) {
				WalletTransaction transaction = walletTransactionCrud.findByTransactionIdAndEntityStatus(transactionId, status);
				return success("", transaction);
			} else {
				WalletTransaction transaction = walletTransactionCrud.findByTransactionId(transactionId);
				return success("", transaction);
			}
			
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getWallet(String id, EntityStatus entityStatus) throws ApiException {
		try {
			Wallet wallet;
			if (entityStatus != null) {
				wallet = walletCrud.findByWalletIdAndEntityStatus(id, entityStatus);
			} else {
				wallet = walletCrud.findByWalletId(id);
			}
			return success("", wallet);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@Autowired
	ReferCrud referCrud;

	@Override
	public ApiResponse putRefer(Refer refer) throws ApiException {
		try {
			required("Refer", refer);
			refer = referCrud.save(refer);
			return success("", refer);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getRefer(String phoneNumber, EntityStatus entityStatus) throws ApiException {
		try {
			required("Phone number", phoneNumber);
			required("Entity Status", entityStatus);
			List<Refer> refer = referCrud.findByPhoneNumberAndEntityStatus(phoneNumber, entityStatus);
			return success("", refer);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getRefer(Subscriber subscriber) throws ApiException {
		try {
			required("Subscriber", subscriber);
			List<Refer> refer = referCrud.findBySubscriber(subscriber);
			return success("", refer);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse getRefer(WalletTransaction walletTransaction) throws ApiException {
		try {
			required("Wallet Transaction", walletTransaction);
			Refer refer = referCrud.findByTransaction(walletTransaction);
			return success("", refer);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@Override
	public ApiResponse deleteActivationTokens(Subscriber subscriber, ActivateTokenType type) throws ApiException {
		try {
			required("Subscriber", subscriber);
			required("Type", type);
			activationTokenCrud.deleteInBatch(subscriber, type);
			return success("Tokens deleted successfully", null);
		} catch (Exception e) {
			return failure(e);
		}
	}

	

}
