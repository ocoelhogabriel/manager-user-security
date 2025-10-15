package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.LoggerEntity;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.LoggerRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.LoggerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Consolidated Mapper for Logger entity using MapStruct.
 * Handles conversions between Domain, DTOs, and Persistence entities.
 */
@Mapper(componentModel = "spring")
public interface LoggerMapper {

    // --- DTO -> Domain ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    Logger toDomain(LoggerRequest request);

    // --- Domain -> DTO ---
    LoggerResponse toResponse(Logger domain);

    List<LoggerResponse> toResponseList(List<Logger> domains);

    // --- Persistence -> Domain ---
    Logger toDomain(LoggerEntity entity);

    List<Logger> toDomainList(List<LoggerEntity> entities);

    // --- Domain -> Persistence ---
    LoggerEntity toPersistenceEntity(Logger domain);

    List<LoggerEntity> toPersistenceEntityList(List<Logger> domains);

}
