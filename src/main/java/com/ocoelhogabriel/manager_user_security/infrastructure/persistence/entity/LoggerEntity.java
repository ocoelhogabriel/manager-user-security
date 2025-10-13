package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * JPA entity for Logger.
 */
@Entity
@Table(name = "logger")
public class LoggerEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;
    
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType type;
    
    @Column(name = "message", nullable = false, length = 1000)
    private String message;
    
    // Default constructor required by JPA
    public LoggerEntity() {
    }
    
    // Constructor for creating entity from domain object (via mapper)
    public LoggerEntity(Long id, LocalDateTime timestamp, String serialNumber, 
                       com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType type, String message) {
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

    public com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType getType() {
        return type;
    }

    public void setType(com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoggerEntity that = (LoggerEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LoggerEntity{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", serialNumber='" + serialNumber + '\'' +
                ", type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
