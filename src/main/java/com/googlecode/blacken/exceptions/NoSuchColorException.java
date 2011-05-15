package com.googlecode.blacken.exceptions;

public class NoSuchColorException extends Exception {

    /**
     * The serialVersionUID is required by Serializable objects
     */
    private static final long serialVersionUID = -4372977339401691848L;

    /**
     * Thrown when a color is looked up which does not exist
     */
    public NoSuchColorException() {
        super();
    }

    /**
     * The serialVersionUID is required by Serializable objects
     * 
     * @param message
     *            Description of the problem
     * @param cause
     *            Related exception
     */
    public NoSuchColorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * The serialVersionUID is required by Serializable objects
     * 
     * @param message
     *            Description of the problem
     */
    public NoSuchColorException(String message) {
        super(message);
    }

    /**
     * The serialVersionUID is required by Serializable objects
     * 
     * @param cause
     *            Related exception
     */
    public NoSuchColorException(Throwable cause) {
        super(cause);
    }
}
