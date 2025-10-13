package com.ocoelhogabriel.usersecurity.application.dto;

/**
 * Data transfer object for token details.
 */
public class TokenDetails {
    private final String username;
    private final String token;
    private final String issuedAt;
    private final String expiresAt;

    /**
     * Constructor for TokenDetails.
     * 
     * @param username the username
     * @param token the JWT token
     * @param issuedAt the date and time when the token was issued
     * @param expiresAt the date and time when the token will expire
     */
    public TokenDetails(String username, String token, String issuedAt, String expiresAt) {
        this.username = username;
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    /**
     * Gets the username.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the token.
     * 
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets the issued at timestamp.
     * 
     * @return the issued at timestamp
     */
    public String getIssuedAt() {
        return issuedAt;
    }

    /**
     * Gets the expires at timestamp.
     * 
     * @return the expires at timestamp
     */
    public String getExpiresAt() {
        return expiresAt;
    }
}