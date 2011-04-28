package com.googlecode.blacken.exceptions;

public class ImmutibleException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ImmutibleException() {
    }

    public ImmutibleException(String message) {
        super(message);
    }

    public ImmutibleException(Throwable cause) {
        super(cause);
    }

    public ImmutibleException(String message, Throwable cause) {
        super(message, cause);
    }

}
