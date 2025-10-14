package com.ocoelhogabriel.manager_user_security.interfaces.dto;

import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for logger entry creation requests.
 */
public class LoggerRequest {
    
    @NotBlank(message = "Serial number is required")
    @Size(min = 3, max = 50, message = "Serial number must be between 3 and 50 characters")
    private String serialNumber;
    
    @NotNull(message = "Log type is required")
    private LoggerType type;
    
    @NotBlank(message = "Message is required")
    @Size(min = 3, max = 500, message = "Message must be between 3 and 500 characters")
    private String message;
    
    // Getters and setters
    public String getSerialNumber() {
        return serialNumber;
    }
    
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public LoggerType getType() {
        return type;
    }
    
    public void setType(LoggerType type) {
        this.type = type;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
