package com.demus.entity;

import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.demus.Exclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A transaction on a wallet account
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Entity
@Table (name = "WALLET_TRANSACTION", indexes = {@Index(name = "wallet_trans_ref_idx",  columnList="reference", unique = false), 
		@Index(name = "wallet_trans_wallet_ref_idx",  columnList="wallet_id", unique = false)})
public class WalletTransaction extends Persistent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "WALLET_ID", nullable = false, updatable = false)
	@Exclude
	@JsonIgnore
	private Wallet wallet;
	
	@NotNull
	@Size (max = 255)
	@Column (name = "TRANSACTION_ID", nullable = false, unique = true, updatable = false)
	private String transactionId;
	
	@NotNull
	@Column (name = "AMOUNT", nullable = false, updatable = false)
	private Double amount;
	
	// In case 
	@NotNull
	@Column (name = "TYPE", nullable = false, updatable = false)
	@Enumerated (EnumType.STRING)
	private WalletTransactionType type;
	 
	@NotNull
	@Column (name = "PAYMENT_GATEWAY", nullable = false, updatable = false)
	@Enumerated (EnumType.STRING)
	private PaymentGateway paymentGateway;
	
	// In case of retail
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "ORDER_ID", referencedColumnName = "ID", nullable = true, updatable = false)
	private Order order;
	
	// In case of transfer
	@JoinColumn (name = "RECEIVER_ID", referencedColumnName = "ID", nullable = true, updatable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@Exclude @JsonIgnore
	private Subscriber receiver;
	
	// In case of cash back
	@JoinColumn (name = "BANK_ACCOUNT_ID", referencedColumnName = "ID", nullable = true, updatable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@Exclude @JsonIgnore
	private BankAccount bankAccount;
	
	@NotNull
	@Column (name = "TRANSACTION_STATUS", nullable = true, updatable = false)
	@Enumerated (EnumType.STRING)
	private WalletTransactionStatus transactionStatus = WalletTransactionStatus.PENDING;
	
	@NotNull
	@Column (name = "BALANCE_AFTER_TRANSACTION", nullable = false, updatable = true)
	private Double balanceAfterTransaction;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn (name = "REFERENCE_TRANSACTION", referencedColumnName = "TRANSACTION_ID", nullable = true)
	private WalletTransaction referenceTransaction;
	
	@Column (name = "DESCRIPTION", nullable = true, updatable = true)
	private String description;

	@Column (name = "REFERENCE", nullable = true, updatable = false)
	private String reference;
	
	@Column (name = "PAYMENT_AUTHORIZATION_TOKEN", nullable = true, updatable = false)
	@Exclude
	@JsonIgnore
	private String paymentAuthorizationToken;
	
	@Column (name = "PAYSTACK_TRANSACTION_REF", nullable = true, updatable = false)
	@Exclude
	@JsonIgnore
	private String paystackTransactionRef;
	
	public WalletTransaction () {}
	
	/**
	 * Constructor for retail transactions
	 * @param transactionId
	 * @param wallet
	 * @param amount
	 * @param type
	 * @param paymentGateway
	 * @param order
	 * @param subscriber
	 */
	public WalletTransaction (String transactionId, Wallet wallet, Double amount, Double newBalance, WalletTransactionType type, PaymentGateway paymentGateway, Order order, BankAccount bankAccount, WalletTransactionStatus transactionStatus, EntityStatus status) {
		setTransactionId(transactionId);
		setWallet(wallet);
		setAmount(amount);
		setType(type);
		setPaymentGateway(paymentGateway);
		setOrder(order);
		setBalanceAfterTransaction(newBalance);
		setBankAccount(bankAccount);
		setTransactionStatus(transactionStatus);
		setEntityStatus(status);
	}
	
	public WalletTransaction (String transactionId, Wallet wallet, Double amount, Double newBalance, WalletTransactionType type, PaymentGateway paymentGateway, Order order, BankAccount bankAccount, WalletTransactionStatus transactionStatus, EntityStatus status, String paymentAuthorizationToken) {
		setTransactionId(transactionId);
		setWallet(wallet);
		setAmount(amount);
		setType(type);
		setPaymentGateway(paymentGateway);
		setOrder(order);
		setBalanceAfterTransaction(newBalance);
		setBankAccount(bankAccount);
		setTransactionStatus(transactionStatus);
		setEntityStatus(status);
		setPaymentAuthorizationToken(paymentAuthorizationToken);
	}
	
	public WalletTransaction (String transactionId, Wallet wallet, Double amount, Double newBalance, WalletTransactionType type, PaymentGateway paymentGateway, Order order, BankAccount bankAccount, WalletTransactionStatus transactionStatus, String paystackRef, EntityStatus status) {
		setTransactionId(transactionId);
		setWallet(wallet);
		setAmount(amount);
		setType(type);
		setPaymentGateway(paymentGateway);
		setOrder(order);
		setBalanceAfterTransaction(newBalance);
		setBankAccount(bankAccount);
		setTransactionStatus(transactionStatus);
		setEntityStatus(status);
		setPaystackTransactionRef(paystackRef);
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public WalletTransactionType getType() {
		return type;
	}

	public void setType(WalletTransactionType type) {
		this.type = type;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public PaymentGateway getPaymentGateway() {
		return paymentGateway;
	}

	public void setPaymentGateway(PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public WalletTransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(WalletTransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public Subscriber getReceiver() {
		return receiver;
	}

	public void setReceiver(Subscriber receiver) {
		this.receiver = receiver;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Double getBalanceAfterTransaction() {
		return balanceAfterTransaction;
	}

	public void setBalanceAfterTransaction(Double balanceAfterTransaction) {
		this.balanceAfterTransaction = balanceAfterTransaction;
	}

	public WalletTransaction getReferenceTransaction() {
		return referenceTransaction;
	}

	public void setReferenceTransaction(WalletTransaction referenceTransaction) {
		this.referenceTransaction = referenceTransaction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Boolean isCredit () {
		return amount > 0;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	//@JsonInclude
	public static String getCurrencyCode () {
		return "NGN";
	}
	
	//@JsonInclude
	public Double getAbsolutePrice () {
		return Math.abs(amount);
	}
	
	static DecimalFormat decimalFormat = new DecimalFormat(String.format("%s ##, ###.00", getCurrencyCode()));
	
	public String getPrintAmount () {
		if(amount == 0.00)
			return "NGN 0.00";
		else
			return decimalFormat.format(getAbsolutePrice());
	}
	
	public String getPrintBalance () {
		if(balanceAfterTransaction == 0.00)
			return "NGN 0.00";
		else
			return decimalFormat.format(balanceAfterTransaction);
	}

	public String getPaymentAuthorizationToken() {
		return paymentAuthorizationToken;
	}

	public void setPaymentAuthorizationToken(String paymentAuthorizationToken) {
		this.paymentAuthorizationToken = paymentAuthorizationToken;
	}

	public String getPaystackTransactionRef() {
		return paystackTransactionRef;
	}

	public void setPaystackTransactionRef(String paystackTransactionRef) {
		this.paystackTransactionRef = paystackTransactionRef;
	}
}
