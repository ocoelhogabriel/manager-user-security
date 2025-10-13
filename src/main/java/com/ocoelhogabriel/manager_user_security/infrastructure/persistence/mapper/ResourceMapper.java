package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;

/**
 * Mapper for converting between Resource domain entity and ResourceEntity JPA entity.
 */
@Component
public class ResourceMapper {

    /**
     * Maps a JPA entity to a domain entity.
     *
     * @param entity the JPA entity
     * @return the domain entity
     */
    public Resource toDomain(ResourceEntity entity) {
        if (entity == null) {
            return null;
        }

        Resource resource = new Resource(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPath(),
                entity.getMethod()
        );

        return resource;
    }

    /**
     * Maps a domain entity to a JPA entity.
     *
     * @param domain the domain entity
     * @return the JPA entity
     */
    public ResourceEntity toEntity(Resource domain) {
        if (domain == null) {
            return null;
        }

        ResourceEntity entity = new ResourceEntity();
        
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPath(domain.getPath());
        entity.setMethod(domain.getMethod());

        return entity;
    }

    /**
     * Updates a JPA entity with data from a domain entity.
     *
     * @param entity the JPA entity to update
     * @param domain the domain entity with updated data
     * @return the updated JPA entity
     */
    public ResourceEntity updateEntityFromDomain(ResourceEntity entity, Resource domain) {
        if (entity == null || domain == null) {
            return entity;
        }

        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPath(domain.getPath());
        entity.setMethod(domain.getMethod());

        return entity;
    }
}
