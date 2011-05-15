package com.googlecode.blacken.exceptions;

public class UnrecoverableException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4239679840891572306L;

	public UnrecoverableException() {
		super("No way to recover.");
	}

	public UnrecoverableException(String message) {
		super(message);
	}

	public UnrecoverableException(Throwable cause) {
		super("No way to recover.", cause);
	}

	public UnrecoverableException(String message, Throwable cause) {
		super(message, cause);
	}

}
