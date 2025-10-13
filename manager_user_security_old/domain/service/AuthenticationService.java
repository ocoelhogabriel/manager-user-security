package com.ocoelhogabriel.manager_user_security.domain.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.TokenDetails;

/**
 * Service interface for authentication operations
 */
public interface AuthenticationService extends UserDetailsService {
    
    /**
     * Generate a token for a user
     * 
     * @param username The username
     * @param password The password
     * @return TokenDetails containing the generated token
     */
    TokenDetails generateToken(String username, String password);
    
    /**
     * Validate a token
     * 
     * @param token The token to validate
     * @return The username from the token
     */
    String validateToken(String token);
    
    /**
     * Refresh an expired token
     * 
     * @param token The expired token
     * @return TokenDetails containing the new token
     */
    TokenDetails refreshToken(String token);
    
    /**
     * Validate and parse a token
     * 
     * @param token The token to validate and parse
     * @return TokenDetails containing the token information
     */
    TokenDetails validateAndParseToken(String token);
    
    /**
     * Authenticate a user
     * 
     * @param username The username
     * @param password The password
     * @return The authenticated user
     */
    User authenticate(String username, String password);
}