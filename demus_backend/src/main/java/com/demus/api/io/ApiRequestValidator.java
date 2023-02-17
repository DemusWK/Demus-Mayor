package com.demus.api.io;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * Validates the Api Requests
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public class ApiRequestValidator {
	
	public static void valStringRange (String name, String object, int min, int max) {
		if (object == null)
			return;
		if (object.length() < min) {
			throw new IllegalArgumentException(name + " too short. At least " + min + " characters required.");
		} else if (object.length() > max) {
			throw new IllegalArgumentException(name + " too long. At most " + max + " characters allowed.");
		}
	}
	
	public static void valFixedSize (String name, String object, int size) {
		if (object == null)
			return;
		if (object.length() != size) {
			throw new IllegalArgumentException(name + " must be " + size + " characters long");
		} 
	}
	
	public static void valUrl (String name, String object) {
		if (object == null)
			return;
		Boolean isVal = UrlValidator.getInstance().isValid(object);
		if (!isVal)
			throwEx(name , " not a valid url");
	}
	
	public static void valEmail (String name, String object) {
		if (object == null)
			return;
		Boolean isVal = EmailValidator.getInstance().isValid(object);
		if (!isVal)
			throwEx(name , " not a valid email");
	}
	
	public static void notEmpty (String name, String object) {
		if (object == null || object.trim().length() == 0)
			throwEx(name , " cannot be empty");
	}
	
	public static void notEmptyIfNonNull (String name, String object) {
		if (object != null && object.trim().length() == 0)
			throwEx(name , " cannot be empty");
	}
	
	public static void throwEx (String name, String message) {
		name = name.toLowerCase().trim();
		throw new IllegalArgumentException(name + message);
	}

	public static void notNull(String name, Object object) {
		if (object == null)
			throwEx(name , " is required");
	}

	public static void setValueIfNull(Object nullable, Object value) {
		if (nullable == null)
			nullable = value;
	}
	
	public static void validateBase64 (String name, String object) {
		notNull(name, object);
		if (object.split(":").length == 1) {
			throwEx(name, " invalid base64 image");
		}
		String fragment = object.split(":")[1];
		String type = fragment.split(";")[0];
		String data = fragment.split(",")[1];
		if (type == null || data == null) {
			throwEx(name, " invalid base64 image");
		}
	}

	public static void validateDelimiter(String name, String object, String delimiter, int max) {
		if (object == null)
			return;
		String[] tags = object.split(delimiter);
		if (tags.length > max)
			throwEx(name, " cannot be more than " + max);
	}

	public static void valMinSize(String name, @SuppressWarnings("rawtypes") List pictures, int i) {
		if (pictures == null || pictures.size() < i)
			throwEx(name, " must be at least " + i);
	}

	public static void valMaxSize(String name, String address, int maxAddress) {
		if (name == null || address.length() > maxAddress)
			throwEx(name, " must not be greater than " + maxAddress);
	}
	
	
}