package com.googlecode.blacken.exceptions;

/**
 * This indicates that an invalid string format was found.
 * 
 * @author Steven Black
 *
 */
public class InvalidStringFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidStringFormatException() {
		super();
	}

	public InvalidStringFormatException(String arg0) {
		super(arg0);
	}

	public InvalidStringFormatException(Throwable arg0) {
		super(arg0);
	}

	public InvalidStringFormatException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
