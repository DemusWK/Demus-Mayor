package com.demus;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.demus.api.AuthService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	AuthService authService;
	
	@Value ("${internal.ipAddresses}")
	private String internalIpAddresses;
	
	private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
	    	// Rules are obeyed in order
	    	StringBuffer access = new StringBuffer();
	    	if (!StringUtils.isEmpty(internalIpAddresses)) {
	        	String[] ips = internalIpAddresses.split(",");
	        	logger.info(String.format("Internal ip rule %s", access));
	        	buildAccess(access, ips);
	    }
	    	
	    	String attributes = access.toString().trim();
	    	
	    	logger.info("Access control is " + attributes);
	    	
	    	http.sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	    	
	    	http.csrf()
	    		.disable()
	    		.httpBasic()
	    		.and()
	        	.authorizeRequests()
	                .antMatchers("/public/**").permitAll()
	                .antMatchers("/secure/**").authenticated()
	                .antMatchers("/backend/**").authenticated()
	                .antMatchers("/internal/**").authenticated()
	                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
    }
    
    private void buildAccess (StringBuffer access, String[] ips) {
	    	String accessPattern = "hasIpAddress('%s')";
	    	if (access.length() > 0) {
			access.append(" or ");
		}
	    	
	    	for (int i = 0; i < ips.length; i++) {
	    		String ip = ips[i];
	    		ip = ip.trim();
	    		logger.info("Allowing internal ip addresses " + ip);
	    		
	    		access.append(String.format(accessPattern, ip));
	    		access.append(i < ips.length - 1 ? " or " : "");
	    	}
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
    		web.ignoring()
	    	   .antMatchers("/public/**")
	    	   .antMatchers("/secure/product/recharge/web/**")
	    	   .antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    		auth.userDetailsService(authService).passwordEncoder(passwordEncoder());
    }
    
    @Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    @Bean 
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
         return super.authenticationManagerBean();
    }
}