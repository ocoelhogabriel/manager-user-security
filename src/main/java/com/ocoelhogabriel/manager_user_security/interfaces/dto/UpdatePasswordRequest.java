package com.ocoelhogabriel.manager_user_security.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating a password.
 */
public class UpdatePasswordRequest {
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", 
             message = "Password must contain at least one letter and one number")
    private String password;
    
    public UpdatePasswordRequest() {
    }
    
    public UpdatePasswordRequest(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
