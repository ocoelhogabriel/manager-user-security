package com.ocoelhogabriel.usersecurity.infrastructure.security.util;

/**
 * Constants used in security configuration.
 */
public class SecurityConstants {

    private SecurityConstants() {
        // Utility class, no instantiation
    }
    
    /**
     * Paths that are excluded from authentication requirements.
     */
    public static final String[] AUTH_WHITELIST = {
        "/api/auth/v1/login",
        "/api/auth/v1/validate",
        "/api/auth/v1/refresh",
        "/api/auth/v1/register",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**",
        "/error",
        "/actuator/**"
    };
}