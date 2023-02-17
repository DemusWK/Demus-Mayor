package com.demus;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

@SpringBootApplication
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity 
@EnableJpaAuditing
public class Application {
	
	public static void main(String[] args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("Africa/Lagos"));
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	RestTemplate restTemplate () {
		return new RestTemplate();
	}
	
	@Bean
	Gson gson () {
		return new Gson();
	}
}
