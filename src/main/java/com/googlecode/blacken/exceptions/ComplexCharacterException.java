package com.googlecode.blacken.exceptions;

/**
 * This is thrown when a complex character shows up in an environment expecting
 * only simple characters.
 *  
 * @author Steven Black
 *
 */
public class ComplexCharacterException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 4389421563337664791L;

    public ComplexCharacterException() {
		super();
	}

	public ComplexCharacterException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ComplexCharacterException(String arg0) {
		super(arg0);
	}

	public ComplexCharacterException(Throwable arg0) {
		super(arg0);
	}

}
