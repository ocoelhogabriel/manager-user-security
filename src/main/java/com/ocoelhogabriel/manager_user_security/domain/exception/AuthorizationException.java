package com.ocoelhogabriel.manager_user_security.domain.exception;

/**
 * Exception thrown when authorization fails.
 */
public class AuthorizationException extends DomainException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new AuthorizationException with the specified message.
     *
     * @param message the exception message
     */
    public AuthorizationException(String message) {
        super(message);
    }
    
    /**
     * Creates a new AuthorizationException with the specified message and cause.
     *
     * @param message the exception message
     * @param cause the cause of the exception
     */
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
