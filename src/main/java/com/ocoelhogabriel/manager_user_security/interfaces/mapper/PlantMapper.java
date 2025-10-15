package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PlantEntity;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.PlantRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.PlantResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.PlantUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Consolidated Mapper for Plant entity using MapStruct.
 * Handles conversions between Domain, DTOs, and Persistence entities.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface PlantMapper {

    // --- DTO -> Domain ---
    Plant toDomain(PlantRequest request);

    Plant toDomain(PlantUpdateRequest request);

    // --- Domain -> DTO ---
    PlantResponse toResponse(Plant domain);

    List<PlantResponse> toResponseList(List<Plant> domains);

    // --- Persistence -> Domain ---
    Plant toDomain(PlantEntity entity);

    List<Plant> toDomainList(List<PlantEntity> entities);

    // --- Domain -> Persistence ---
    PlantEntity toPersistenceEntity(Plant domain);

    List<PlantEntity> toPersistenceEntityList(List<Plant> domains);

    // --- Update Persistence from Domain ---
    void updatePersistenceEntity(@MappingTarget PlantEntity entity, Plant domain);
}
