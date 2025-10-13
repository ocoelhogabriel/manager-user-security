package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.CoverageEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.UserEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.CompanyEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PlantEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Coverage domain entity and CoverageEntity JPA entity.
 */
@Component
public class CoverageMapper {
    
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    private final PlantMapper plantMapper;
    
    @Autowired
    public CoverageMapper(UserMapper userMapper, CompanyMapper companyMapper, PlantMapper plantMapper) {
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
        this.plantMapper = plantMapper;
    }
    
    /**
     * Convert a CoverageEntity to a Coverage domain entity.
     *
     * @param coverageEntity The JPA entity
     * @return The domain entity
     */
    public Coverage toDomain(CoverageEntity coverageEntity) {
        if (coverageEntity == null) {
            return null;
        }
        
        User user = userMapper.toDomain(coverageEntity.getUser());
        Company company = companyMapper.toDomain(coverageEntity.getCompany());
        Plant plant = plantMapper.toDomain(coverageEntity.getPlant());
        
        return Coverage.builder()
                .id(coverageEntity.getId())
                .user(user)
                .company(company)
                .plant(plant)
                .description(coverageEntity.getDescription())
                .active(coverageEntity.isActive())
                .build();
    }
    
    /**
     * Convert a Coverage domain entity to a CoverageEntity JPA entity.
     *
     * @param coverage The domain entity
     * @return The JPA entity
     */
    public CoverageEntity toEntity(Coverage coverage) {
        if (coverage == null) {
            return null;
        }
        
        CoverageEntity coverageEntity = new CoverageEntity();
        coverageEntity.setId(coverage.getId());
        
        if (coverage.getUser() != null) {
            UserEntity userEntity = userMapper.toEntity(coverage.getUser());
            coverageEntity.setUser(userEntity);
        }
        
        if (coverage.getCompany() != null) {
            CompanyEntity companyEntity = companyMapper.toEntity(coverage.getCompany());
            coverageEntity.setCompany(companyEntity);
        }
        
        if (coverage.getPlant() != null) {
            PlantEntity plantEntity = plantMapper.toEntity(coverage.getPlant());
            coverageEntity.setPlant(plantEntity);
        }
        
        coverageEntity.setDescription(coverage.getDescription());
        coverageEntity.setActive(coverage.isActive());
        
        return coverageEntity;
    }
    
    /**
     * Convert a list of CoverageEntity objects to a list of Coverage domain entities.
     *
     * @param coverageEntities The list of JPA entities
     * @return The list of domain entities
     */
    public List<Coverage> toDomainList(List<CoverageEntity> coverageEntities) {
        if (coverageEntities == null) {
            return List.of();
        }
        
        return coverageEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert a list of Coverage domain entities to a list of CoverageEntity JPA entities.
     *
     * @param coverages The list of domain entities
     * @return The list of JPA entities
     */
    public List<CoverageEntity> toEntityList(List<Coverage> coverages) {
        if (coverages == null) {
            return List.of();
        }
        
        return coverages.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
