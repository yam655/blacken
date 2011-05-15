package com.googlecode.blacken.exceptions;

public class CharacterUnsupportedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3138235441574739078L;

	public CharacterUnsupportedException() {
		super();
	}

	public CharacterUnsupportedException(String message) {
		super(message);
	}

	public CharacterUnsupportedException(Throwable cause) {
		super(cause);
	}

	public CharacterUnsupportedException(String message, Throwable cause) {
		super(message, cause);
	}

}
