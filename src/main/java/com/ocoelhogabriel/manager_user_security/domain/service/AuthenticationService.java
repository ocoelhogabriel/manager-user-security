package com.ocoelhogabriel.manager_user_security.domain.service;

import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;

/**
 * Service interface for authentication operations.
 * This interface defines the contract for authenticating users.
 */
public interface AuthenticationService {
    
    /**
     * Authenticates a user with username and password.
     *
     * @param username the username of the user to authenticate
     * @param password the password of the user to authenticate
     * @return an Optional containing the authenticated user, or empty if authentication failed
     */
    Optional<User> authenticate(String username, String password);
    
    /**
     * Validates authentication credentials.
     *
     * @param token the authentication token to validate
     * @return an Optional containing the user associated with the token, or empty if validation failed
     */
    Optional<User> validateToken(String token);
    
    /**
     * Generates a new authentication token for a user.
     *
     * @param user the user to generate a token for
     * @return the generated authentication token
     */
    String generateToken(User user);
    
    /**
     * Refreshes an authentication token.
     *
     * @param token the token to refresh
     * @return an Optional containing the new token, or empty if the token couldn't be refreshed
     */
    Optional<String> refreshToken(String token);
    
    /**
     * Invalidates an authentication token.
     *
     * @param token the token to invalidate
     */
    void invalidateToken(String token);
}
