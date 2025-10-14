package com.ocoelhogabriel.manager_user_security.interfaces.dto;

import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;
import java.time.LocalDateTime;

/**
 * DTO for logger entry responses.
 */
public class LoggerResponse {
    
    private Long id;
    private LocalDateTime timestamp;
    private String serialNumber;
    private LoggerType type;
    private String message;
    
    // Constructors
    public LoggerResponse() {
    }
    
    public LoggerResponse(Long id, LocalDateTime timestamp, String serialNumber, LoggerType type, String message) {
        this.id = id;
        this.timestamp = timestamp;
        this.serialNumber = serialNumber;
        this.type = type;
        this.message = message;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
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
