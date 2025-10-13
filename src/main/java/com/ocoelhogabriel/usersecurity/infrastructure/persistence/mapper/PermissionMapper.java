package com.ocoelhogabriel.usersecurity.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.usersecurity.domain.entity.Permission;
import com.ocoelhogabriel.usersecurity.domain.entity.Resource;
import com.ocoelhogabriel.usersecurity.infrastructure.persistence.entity.PermissionEntity;

/**
 * Mapper for converting between Permission domain entity and PermissionEntity JPA entity.
 */
@Component
public class PermissionMapper {

    private final ResourceMapper resourceMapper;

    /**
     * Constructor.
     *
     * @param resourceMapper the resource mapper
     */
    public PermissionMapper(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    /**
     * Maps a JPA entity to a domain entity.
     *
     * @param entity the JPA entity
     * @return the domain entity
     */
    public Permission toDomain(PermissionEntity entity) {
        if (entity == null) {
            return null;
        }

        Resource resource = null;
        if (entity.getResource() != null) {
            resource = resourceMapper.toDomain(entity.getResource());
        }

        Permission permission = new Permission(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                resource
        );

        return permission;
    }

    /**
     * Maps a domain entity to a JPA entity.
     *
     * @param domain the domain entity
     * @return the JPA entity
     */
    public PermissionEntity toEntity(Permission domain) {
        if (domain == null) {
            return null;
        }

        PermissionEntity entity = new PermissionEntity();
        
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());

        // Map resource if it exists
        if (domain.getResource() != null) {
            entity.setResource(resourceMapper.toEntity(domain.getResource()));
        }

        return entity;
    }

    /**
     * Updates a JPA entity with data from a domain entity.
     *
     * @param entity the JPA entity to update
     * @param domain the domain entity with updated data
     * @return the updated JPA entity
     */
    public PermissionEntity updateEntityFromDomain(PermissionEntity entity, Permission domain) {
        if (entity == null || domain == null) {
            return entity;
        }

        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());

        // Update resource if it exists in the domain object
        if (domain.getResource() != null) {
            entity.setResource(resourceMapper.toEntity(domain.getResource()));
        }

        return entity;
    }
}