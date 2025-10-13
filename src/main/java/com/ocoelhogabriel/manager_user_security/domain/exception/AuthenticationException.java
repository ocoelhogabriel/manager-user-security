package com.ocoelhogabriel.manager_user_security.domain.exception;

/**
 * Exception thrown when authentication fails.
 */
public class AuthenticationException extends DomainException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new AuthenticationException with the specified message.
     *
     * @param message the exception message
     */
    public AuthenticationException(String message) {
        super(message);
    }
    
    /**
     * Creates a new AuthenticationException with the specified message and cause.
     *
     * @param message the exception message
     * @param cause the cause of the exception
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
