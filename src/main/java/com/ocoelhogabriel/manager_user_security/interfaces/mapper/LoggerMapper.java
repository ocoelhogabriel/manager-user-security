package com.ocoelhogabriel.manager_user_security.interfaces.api.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.LoggerRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.LoggerResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Logger domain entity and DTOs.
 */
@Component
public class LoggerMapper {
    
    /**
     * Converts a LoggerRequest DTO to a Logger domain entity.
     *
     * @param request The logger request DTO
     * @return The logger domain entity
     */
    public Logger toEntity(LoggerRequest request) {
        return new Logger.Builder()
                .withTimestamp(LocalDateTime.now())
                .withSerialNumber(request.getSerialNumber())
                .withType(request.getType())
                .withMessage(request.getMessage())
                .build();
    }
    
    /**
     * Converts a Logger domain entity to a LoggerResponse DTO.
     *
     * @param entity The logger domain entity
     * @return The logger response DTO
     */
    public LoggerResponse toResponse(Logger entity) {
        return new LoggerResponse(
                entity.getId(),
                entity.getTimestamp(),
                entity.getSerialNumber(),
                entity.getType(),
                entity.getMessage()
        );
    }
    
    /**
     * Converts a list of Logger domain entities to a list of LoggerResponse DTOs.
     *
     * @param entities The list of logger domain entities
     * @return The list of logger response DTOs
     */
    public List<LoggerResponse> toResponseList(List<Logger> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
