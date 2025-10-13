package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.LoggerEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Logger domain entity and LoggerEntity JPA entity.
 */
@Component
public class LoggerMapper {
    
    /**
     * Convert a JPA entity to a domain entity
     *
     * @param entity The JPA entity to convert
     * @return The domain entity
     */
    public Logger toDomain(LoggerEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new Logger.Builder()
                .withId(entity.getId())
                .withTimestamp(entity.getTimestamp())
                .withSerialNumber(entity.getSerialNumber())
                .withType(entity.getType())
                .withMessage(entity.getMessage())
                .build();
    }
    
    /**
     * Convert a domain entity to a JPA entity
     *
     * @param domain The domain entity to convert
     * @return The JPA entity
     */
    public LoggerEntity toEntity(Logger domain) {
        if (domain == null) {
            return null;
        }
        
        return new LoggerEntity(
                domain.getId(),
                domain.getTimestamp(),
                domain.getSerialNumber(),
                domain.getType(),
                domain.getMessage()
        );
    }
    
    /**
     * Update an existing JPA entity with values from a domain entity
     *
     * @param entity The JPA entity to update
     * @param domain The domain entity with new values
     * @return The updated JPA entity
     */
    public LoggerEntity updateEntity(LoggerEntity entity, Logger domain) {
        if (entity == null || domain == null) {
            return entity;
        }
        
        entity.setTimestamp(domain.getTimestamp());
        entity.setSerialNumber(domain.getSerialNumber());
        entity.setType(domain.getType());
        entity.setMessage(domain.getMessage());
        
        return entity;
    }
}
