package com.demus.api.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.demus.api.admin.ModelApi;
import com.demus.api.ex.ApiException;
import com.demus.api.system.SmsService;
import com.demus.api.vendors.VendorApi;
import com.demus.crud.PlatformConfigurationCrud;
import com.demus.crud.SubscriberCrud;
import com.demus.crud.WalletCrud;
import com.demus.crud.WalletTransactionCrud;
import com.demus.entity.PlatformConfiguration;
import com.demus.entity.WalletTransaction;

@Component
@DependsOn ({"ConfigurePlatform"})
public class ReportEngine {
	
	@Autowired
	WalletTransactionCrud walletTransactionCrud;
	
	@Autowired
	SubscriberCrud subscriberCrud;
	
	@Autowired
	VendorApi vendorApi;
	
	@Autowired
	ModelApi modelApi;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	PlatformConfigurationCrud platformConfigurationCrud;
	
	PlatformConfiguration platformConfiguration;
	
	@Autowired
	WalletCrud walletCrud;
	
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
	
	@Value ("${com.demus.company.administrators.phonenumbers}")
	String adminPhoneNumbers;

	@Value("${com.demus.transactionPrefix}")
	private static String TRANSACTION_PREFIX;
	
	@Value("${com.demus.smsLabel}")
	private String smsLabel;
	
	@Value("${com.demus.transfer}")
	private Boolean transferService;
	
	private static Logger logger = LoggerFactory.getLogger(ReportEngine.class);
	
	@PostConstruct
	private void configureVelocity () {
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
	}
	
	@PostConstruct
	public void init () {
		LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Africa/Lagos");
        ZonedDateTime now = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime next ;
        next = now.withHour(23).withMinute(59).withSecond(00);
        Duration duration = Duration.between(now, next);
		logger.debug("Current time is {}", localNow.toString());
 		logger.debug("Start time is {} {} {} {}", next.toString());
 		long delay = duration.getSeconds();
 		logger.debug("Will send report in {} seconds", delay);
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					sendDailyReport();
				} catch (Exception e) {
					logger.debug("Coudlnt send daily report", e);
				}
				
			}
		}, delay, 24 * 60 * 60, TimeUnit.SECONDS);
	}
	
	public static String getCurrencyCode () {
		return "NGN";
	}
	
	static DecimalFormat decimalFormat = new DecimalFormat(String.format("%s ##, ###.00", getCurrencyCode()));
	
	Calendar calendar = Calendar.getInstance();
	
	@Autowired
	SmsService smsService;
	
	@Autowired
	private Environment environment;	
	
	public void sendDailyReport () {
		calendar.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
 		int day = calendar.get(Calendar.DAY_OF_MONTH);
 		int month = calendar.get(Calendar.MONTH);
 		int year = calendar.get(Calendar.YEAR);
 		calendar.set(year, month, day, 0, 0, 0);
 		Date startDate = calendar.getTime();
 		calendar.set(year, month, day, 23, 59, 59);
 		Date endDate = calendar.getTime();
 		
 		List<WalletTransaction> transactions = walletTransactionCrud.findByDate(startDate, endDate);
 		
		Integer totalNumberOfQuickTeller = 0;
		Double totalValueOfQuickTeller = 0.00;
		Integer totalNumberOfLoadTransactions = 0;
		Double totalValueOfLoadTransactions = 0.00;
		Integer totalNumberOfCashoutTransactions = 0;
		Double totalValueOfCashoutTransactions = 0.00;
		Integer totalNumberOfReversalTransactions = 0;
		Double totalValueOfReversalTransactions = 0.00;
		
		Integer totalNumberOfReferTransactions = 0;
		Double totalValueOfReferTransactions = 0.00;
		Integer totalNumberOfAirtimeTransactions = 0;
		Double totalValueOfAirtimeTransactions = 0.00;
		Integer totalNumberOfShareStockTransactions = 0;
		Double totalValueOfShareStockTransactions = 0.00;
		Integer totalNumberOfReceiveStockTransactions = 0;
		Double totalValueOfReceiveStockTransactions = 0.00;
		Integer totalNumberOfCommissionTransactions = 0;
		Double totalValueOfCommissionTransactions = 0.00;
		
		for (int i = 0; i < transactions.size(); i++) {
			WalletTransaction transaction = transactions.get(i);
			
			if((transaction.getWallet().getSubscriber().getId() >= 1 && transaction.getWallet().getSubscriber().getId() <= 5) ||
			   (transaction.getWallet().getSubscriber().getId() >= 8 && transaction.getWallet().getSubscriber().getId() <= 14))
				continue;
			
			// Make value positive
			Double amount = transaction.isCredit() ? transaction.getAmount() : -transaction.getAmount();
			switch (transaction.getType()) {
			case QUICKTELLER:
				totalNumberOfQuickTeller++;
				totalValueOfQuickTeller = totalValueOfQuickTeller + transaction.getAmount();
				break;
			case PINLOAD:
				totalNumberOfLoadTransactions++;
				totalValueOfLoadTransactions = totalValueOfLoadTransactions + amount; 
				break;
			case CASHBACK:
				totalNumberOfCashoutTransactions++;
				totalValueOfCashoutTransactions = totalValueOfCashoutTransactions + transaction.getAmount();
				break;
			case REFER:
				totalNumberOfReferTransactions++;
				totalValueOfReferTransactions = totalValueOfReferTransactions + amount;
				break;
			case REVERSAL:
				totalNumberOfReversalTransactions++;
				totalValueOfReversalTransactions = totalValueOfReversalTransactions + amount;
				break;
			case AIRTIME:
				totalNumberOfAirtimeTransactions++;
				totalValueOfAirtimeTransactions = totalValueOfAirtimeTransactions + amount;
				break;
			case SHARE_STOCK:
				totalNumberOfShareStockTransactions++;
				totalValueOfShareStockTransactions = totalValueOfShareStockTransactions + amount;
				break;
			case RECEIVE_STOCK:
				totalNumberOfReceiveStockTransactions++;
				totalValueOfReceiveStockTransactions = totalValueOfReceiveStockTransactions + amount;
				break;
			case COMMISSION:
				totalNumberOfCommissionTransactions++;
				totalValueOfCommissionTransactions = totalValueOfCommissionTransactions + amount;
				break;
			default:
				break;
			}
			
		}

		logger.debug("sending report");
		//Integer numberOfSuccessTransaction = totalNumberOfAirtimeTransactions + totalNumberOfCashoutTransactions + totalNumberOfCommissionTransactions + totalNumberOfLoadTransactions
		//		+ totalNumberOfQuickTeller + totalNumberOfReceiveStockTransactions + totalNumberOfReferTransactions + totalNumberOfReversalTransactions + totalNumberOfShareStockTransactions;
		
		Long totalNumberOfNewSubscribers = subscriberCrud.countByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(startDate, endDate);
		Long totalSubscriberCount = subscriberCrud.count() - 11;
		
		String startDateFmt = "dd-MM-yyyy hh:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(startDateFmt);
		String message = "End of Day report for %s - %s\nQT Count: %s, Total: %s.\nLoad Count: %s, Total: %s.\nCashB Count: %s, Total: %s\n.New Registration: %s, Subscriber Total: %s.";
		message = String.format(message, formatter.format(startDate), formatter.format(endDate), totalNumberOfQuickTeller, totalValueOfQuickTeller, totalNumberOfLoadTransactions, totalValueOfLoadTransactions
				,totalNumberOfCashoutTransactions, totalValueOfCashoutTransactions, totalNumberOfNewSubscribers, totalSubscriberCount);
		try {
	 		logger.debug("sending daily report {}", message);
			smsService.send(adminPhoneNumbers, message, String.format("DML EOD %s", environment.getActiveProfiles()[0]));
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Could not send eod report", e);
		}
	}
}
