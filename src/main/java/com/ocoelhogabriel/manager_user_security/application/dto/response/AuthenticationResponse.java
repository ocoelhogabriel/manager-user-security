package com.ocoelhogabriel.manager_user_security.application.dto.response;

/**
 * DTO for authentication responses
 */
public class AuthenticationResponse {
    
    private String token;
    private String issuedAt;
    private String expiresAt;
    private String userId;
    private RoleResponse role;
    
    public AuthenticationResponse() {
    }
    
    public AuthenticationResponse(String token, String issuedAt, String expiresAt, String userId, RoleResponse role) {
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.userId = userId;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RoleResponse getRole() {
        return role;
    }

    public void setRole(RoleResponse role) {
        this.role = role;
    }
}