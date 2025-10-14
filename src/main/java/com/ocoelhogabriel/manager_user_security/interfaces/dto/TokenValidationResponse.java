package com.ocoelhogabriel.manager_user_security.interfaces.dto;

/**
 * Data Transfer Object for token validation responses.
 */
public class TokenValidationResponse {
    private boolean valid;
    private long expiresIn;
    private String message;
    
    public TokenValidationResponse() {
    }
    
    public TokenValidationResponse(boolean valid, long expiresIn, String message) {
        this.valid = valid;
        this.expiresIn = expiresIn;
        this.message = message;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public long getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
