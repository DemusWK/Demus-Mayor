package com.demus.api.system;

import java.util.Map;

import com.demus.api.ex.ApiException;

public interface EmailService {

	void sendEmail(String to, String subject, String bcc, String body, Map<String, String> attachment) throws ApiException;
}
