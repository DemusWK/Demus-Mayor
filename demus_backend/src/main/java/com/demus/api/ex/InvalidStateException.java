/**
 * 
 */
package com.demus.api.ex;

/**
 * Exception thrown when an unauthorized client attempts to access a protected resource
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
public class InvalidStateException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidStateException(String string) {
		super(string);
	}

}
