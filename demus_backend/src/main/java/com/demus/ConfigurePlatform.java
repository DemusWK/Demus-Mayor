package com.demus;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.demus.crud.PlatformConfigurationCrud;
import com.demus.entity.PlatformConfiguration;

@Configuration 
@Service (value = "ConfigurePlatform")
public class ConfigurePlatform {

	@Autowired
	private PlatformConfigurationCrud platformConfigurationCrud;
	
	Logger logger = LoggerFactory.getLogger(ConfigurePlatform.class);
	
	@PostConstruct
	public void init () {
		List<PlatformConfiguration> platformConfigurations = platformConfigurationCrud.findAll();
		if (platformConfigurations == null || platformConfigurations.size() == 0) {
			PlatformConfiguration platformConfiguration = new PlatformConfiguration();
			platformConfiguration.setCashBackFixedCharge(100.00);
			platformConfiguration.setCashBackPercentageCharge(2.00);
			platformConfiguration.setMinimumAmountForCashbackPercentageCharge(2000.00);
			platformConfiguration.setMaximumAmountForCashbackPercentageCharge(100000000000.00);
			platformConfiguration.setMinimumAmountForCashBackFixedCharge(0.00);
			platformConfiguration.setMaximumAmountForCashBackFixedCharge(100000000000.00);
			platformConfiguration.setPinLoaderFixedCharge(0.00);
			platformConfiguration.setPinLoaderPercentageCharge(3.00);
			platformConfiguration.setMinimumAmountForPinLoaderPercentageCharge(50.00);
			platformConfiguration.setMaximumAmountForPinLoaderPercentageCharge(100000000000.00);
			platformConfiguration.setMinimumAmountForPinLoaderFixedCharge(0.00);
			platformConfiguration.setMaximumAmountForPinLoaderFixedCharge(1000000000.00);
			platformConfigurationCrud.save(platformConfiguration);
			logger.info("Configured platform");
		}
	}
}
