/**
 * 
 */
package com.demus.api.system;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.demus.api.ex.ApiException;

/**
 * This 
 * @author Lekan Baruwa and Folarin Omotoriogun, EcleverMobile Ltd
 * @since 7 Apr 2016
 */
@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired 
	private JavaMailSender sender;
	
	@Value ("${com.demus.company.email.reply}")
	private String replyTo;
	
	@Value ("${com.demus.company.name}")
	private String appName;
	
	private ExecutorService notifyService = Executors.newFixedThreadPool(1000);
	
	private Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Override
	public void sendEmail(final String to, final String subject, final String bcc, final String body, final Map<String, String> attachment) throws ApiException {
		if (!EmailValidator.getInstance().isValid(to)) {
			logger.error(to + " is not a valid email");
		}
		try {
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					MimeMessagePreparator preparator = new MimeMessagePreparator() {
						
			            @Override
						public void prepare(MimeMessage mimeMessage) throws Exception {
			                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
			                message.setTo(to);
			                if (!Objects.isNull(bcc) && !StringUtils.isEmpty(bcc)) {
			                	logger.info(bcc + " is not a valid email");
			                	message.setBcc(new InternetAddress[]{new InternetAddress(bcc), new InternetAddress("info@demusmayor.com")});
			                } else {
			                	message.setBcc(new InternetAddress[]{new InternetAddress("info@demusmayor.com")});
			                }
			                message.setSubject(subject);
			                message.setFrom(replyTo, Config.APP_NAME + " Notifications");
			                message.setText(body, true);
			                if (attachment != null) {
			                	for (Entry<String, String> entry : attachment.entrySet()) {
				                	String fileName = entry.getKey();
				                	String filePath = entry.getValue();
				                	FileSystemResource res = new FileSystemResource(new File(filePath));
				                	message.addAttachment(fileName, res);
				                }
			                }
			            }
			        };
			        sender.send(preparator);
				}
			};
			notifyService.execute(runnable);
		} catch (Exception e) {
			logger.debug("Could not send email", e);
		}
	}

}
