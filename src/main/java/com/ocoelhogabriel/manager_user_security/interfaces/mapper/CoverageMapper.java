package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.CoverageEntity;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.CoverageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Consolidated Mapper for Coverage entity using MapStruct.
 * Handles conversions between Domain, DTOs, and Persistence entities.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, CompanyMapper.class, PlantMapper.class })
public interface CoverageMapper {

    // --- Domain -> DTO ---
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "plant.id", target = "plantId")
    @Mapping(source = "plant.name", target = "plantName")
    CoverageResponse toResponse(Coverage domain);

    List<CoverageResponse> toResponseList(List<Coverage> domains);

    // --- Persistence -> Domain ---
    Coverage toDomain(CoverageEntity entity);

    List<Coverage> toDomainList(List<CoverageEntity> entities);

    // --- Domain -> Persistence ---
    CoverageEntity toPersistenceEntity(Coverage domain);

    List<CoverageEntity> toPersistenceEntityList(List<Coverage> domains);
}
