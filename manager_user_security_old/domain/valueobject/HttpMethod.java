package com.ocoelhogabriel.manager_user_security.domain.valueobject;

/**
 * Enum representing HTTP methods
 */
public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS;
    
    /**
     * Converts a string to an HttpMethod enum
     * 
     * @param method The method as a string
     * @return The corresponding HttpMethod enum
     */
    public static HttpMethod fromString(String method) {
        if (method == null) {
            return null;
        }
        
        try {
            return HttpMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}