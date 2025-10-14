package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Collections;
import java.util.stream.Collectors;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.ResourceJpaRepository;

/**
 * Mapper for converting between Permission domain entity and PermissionEntity JPA entity.
 */
@Component
public class PermissionMapper {

    private final ResourceMapper resourceMapper;
    private final ResourceJpaRepository resourceRepository;

    /**
     * Constructor.
     *
     * @param resourceMapper the resource mapper
     * @param resourceRepository the resource repository
     */
    public PermissionMapper(ResourceMapper resourceMapper, ResourceJpaRepository resourceRepository) {
        this.resourceMapper = resourceMapper;
        this.resourceRepository = resourceRepository;
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

        // Create a set with actions
        HashSet<String> actions = new HashSet<>();

        // Add the action if available, otherwise use the name as a fallback
        if (entity.getAction() != null && !entity.getAction().isEmpty()) {
            actions.add(entity.getAction());
        } else {
            actions.add(entity.getName());
        }

        // Create the permission with the ID, resource name, and actions
        Permission permission = new Permission(
                entity.getId(),
                entity.getResource() != null ? entity.getResource().getName() : "unknown",
                actions
        );

        return permission;
    }

    /**
     * Maps a domain entity to a JPA entity.
     * This method will look up the appropriate ResourceEntity based on the resource name in the domain entity.
     *
     * @param domain the domain entity
     * @return the JPA entity or null if the resource cannot be found
     */
    public PermissionEntity toEntity(Permission domain) {
        if (domain == null) {
            return null;
        }

        // Find the resource entity by name
        ResourceEntity resourceEntity = resourceRepository.findByName(domain.getResource())
                .orElse(null);

        // If resource not found, we can't create a valid permission entity
        if (resourceEntity == null) {
            return null;
        }

        return toEntity(domain, resourceEntity);
    }

    /**
     * Maps a domain entity to a JPA entity.
     *
     * @param domain the domain entity
     * @param resourceEntity the resource entity to associate with the permission
     * @return the JPA entity
     */
    public PermissionEntity toEntity(Permission domain, ResourceEntity resourceEntity) {
        if (domain == null) {
            return null;
        }

        // Get the first action from the set or use empty string if none exists
        String action = domain.getActions().isEmpty() ?
                        "" : domain.getActions().iterator().next();

        // Create the permission entity with name and description
        PermissionEntity entity = new PermissionEntity();
        entity.setName(domain.getResource() + "_" + action);  // Generate a name from resource and action
        entity.setDescription("Permission to " + action + " on " + domain.getResource());
        entity.setAction(action);
        entity.setResource(resourceEntity);

        return entity;
    }
}
