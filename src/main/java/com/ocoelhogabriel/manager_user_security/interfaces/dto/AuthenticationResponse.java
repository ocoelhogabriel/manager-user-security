package com.ocoelhogabriel.manager_user_security.interfaces.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for authentication responses.
 */
public class AuthenticationResponse {
    private String token;
    private LocalDateTime issuedAt;
    private long expiresInSeconds;
    private String userId;
    private UserRoleDto userRole;
    
    public AuthenticationResponse() {
    }
    
    public AuthenticationResponse(String token, LocalDateTime issuedAt, long expiresInSeconds, 
                                 String userId, UserRoleDto userRole) {
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresInSeconds = expiresInSeconds;
        this.userId = userId;
        this.userRole = userRole;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
    
    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
    
    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }
    
    public void setExpiresInSeconds(long expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public UserRoleDto getUserRole() {
        return userRole;
    }
    
    public void setUserRole(UserRoleDto userRole) {
        this.userRole = userRole;
    }
}
