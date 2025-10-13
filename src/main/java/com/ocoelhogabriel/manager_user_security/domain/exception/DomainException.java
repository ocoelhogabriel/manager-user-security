package com.ocoelhogabriel.manager_user_security.domain.exception;

/**
 * Base exception class for all domain exceptions.
 * This class serves as a root exception for domain-specific exceptions.
 */
public class DomainException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new DomainException with the specified message.
     *
     * @param message the exception message
     */
    public DomainException(String message) {
        super(message);
    }
    
    /**
     * Creates a new DomainException with the specified message and cause.
     *
     * @param message the exception message
     * @param cause the cause of the exception
     */
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
