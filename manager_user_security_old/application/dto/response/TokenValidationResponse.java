package com.ocoelhogabriel.manager_user_security.application.dto.response;

/**
 * DTO for token validation responses
 */
public class TokenValidationResponse {
    
    private boolean valid;
    private long timeToExpiry;
    private String expirationTime;
    
    public TokenValidationResponse() {
    }
    
    public TokenValidationResponse(boolean valid, long timeToExpiry, String expirationTime) {
        this.valid = valid;
        this.timeToExpiry = timeToExpiry;
        this.expirationTime = expirationTime;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getTimeToExpiry() {
        return timeToExpiry;
    }

    public void setTimeToExpiry(long timeToExpiry) {
        this.timeToExpiry = timeToExpiry;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }
}