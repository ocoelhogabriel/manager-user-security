package com.ocoelhogabriel.manager_user_security.application.exception;

/**
 * Exception thrown when an invalid operation is attempted.
 */
public class InvalidOperationException extends ApplicationException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new InvalidOperationException with the specified message.
     *
     * @param message the exception message
     */
    public InvalidOperationException(String message) {
        super(message);
    }
    
    /**
     * Creates a new InvalidOperationException with the specified message and cause.
     *
     * @param message the exception message
     * @param cause the cause of the exception
     */
    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
