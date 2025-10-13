package com.ocoelhogabriel.manager_user_security.domain.exception;

/**
 * Exception thrown when duplicate data is detected.
 */
public class DuplicateResourceException extends DomainException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new DuplicateResourceException with the specified message.
     *
     * @param message the exception message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    /**
     * Creates a new DuplicateResourceException for a resource with the specified field value.
     *
     * @param resourceType the type of resource
     * @param field the field name
     * @param value the field value
     */
    public DuplicateResourceException(String resourceType, String field, Object value) {
        super(String.format("%s with %s '%s' already exists", resourceType, field, value));
    }
}
