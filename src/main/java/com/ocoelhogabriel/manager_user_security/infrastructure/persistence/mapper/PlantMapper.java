package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PlantEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Plant domain entity and PlantEntity JPA entity.
 */
@Component
public class PlantMapper {
    
    /**
     * Convert a JPA entity to a domain entity
     *
     * @param entity The JPA entity to convert
     * @return The domain entity
     */
    public Plant toDomain(PlantEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new Plant.Builder()
                .withId(entity.getId())
                .withCompanyId(entity.getCompanyId())
                .withName(entity.getName())
                .build();
    }
    
    /**
     * Convert a domain entity to a JPA entity
     *
     * @param domain The domain entity to convert
     * @return The JPA entity
     */
    public PlantEntity toEntity(Plant domain) {
        if (domain == null) {
            return null;
        }
        
        return new PlantEntity(
                domain.getId(),
                domain.getCompanyId(),
                domain.getName()
        );
    }
    
    /**
     * Update an existing JPA entity with values from a domain entity
     *
     * @param entity The JPA entity to update
     * @param domain The domain entity with new values
     * @return The updated JPA entity
     */
    public PlantEntity updateEntity(PlantEntity entity, Plant domain) {
        if (entity == null || domain == null) {
            return entity;
        }
        
        entity.setCompanyId(domain.getCompanyId());
        entity.setName(domain.getName());
        
        return entity;
    }
}
