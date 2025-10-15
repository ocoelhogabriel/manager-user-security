package com.ocoelhogabriel.manager_user_security.domain.entity;

import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Logger domain entity representing a log entry in the system.
 * This is a pure domain entity, independent of any framework or infrastructure concerns.
 */
public class Logger {
    
    private final Long id;
    private LocalDateTime timestamp;
    private final String serialNumber;
    private final LoggerType type;
    private final String message;
    
    // Public no-args constructor for MapStruct
    public Logger() {
        this.id = null;
        this.timestamp = null;
        this.serialNumber = null;
        this.type = null;
        this.message = null;
    }

    // Private constructor for builder pattern
    private Logger(Builder builder) {
        this.id = builder.id;
        this.timestamp = builder.timestamp;
        this.serialNumber = builder.serialNumber;
        this.type = builder.type;
        this.message = builder.message;
    }
    
    // Getters only - immutable entity
    public Long getId() {
        return id;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }
    
    public LoggerType getType() {
        return type;
    }
    
    public String getMessage() {
        return message;
    }
    
    // Domain logic methods
    public boolean isValid() {
        return timestamp != null && 
               serialNumber != null && !serialNumber.isBlank() &&
               type != null &&
               message != null && !message.isBlank();
    }
    
    // Builder pattern for Logger
    public static class Builder {
        private Long id;
        private LocalDateTime timestamp;
        private String serialNumber;
        private LoggerType type;
        private String message;
        
        public Builder withId(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder withSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }
        
        public Builder withType(LoggerType type) {
            this.type = type;
            return this;
        }
        
        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }
        
        public Logger build() {
            if (timestamp == null) {
                timestamp = LocalDateTime.now();
            }
            return new Logger(this);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Logger logger = (Logger) o;
        return Objects.equals(id, logger.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Logger{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", serialNumber='" + serialNumber + '\'' +
                ", type=" + type +
                ", message='" + message + '\'' +
                '}';
    }

    public void setTimestamp(LocalDateTime timestamp) {
        // setter para MapStruct
    }
}
