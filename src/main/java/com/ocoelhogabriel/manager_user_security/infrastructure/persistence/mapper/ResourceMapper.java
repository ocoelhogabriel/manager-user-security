package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

        // Create a set of allowed methods
        Set<String> allowedMethods = new HashSet<>();

        // First check if we have allowedMethods stored as a string
        if (entity.getAllowedMethods() != null && !entity.getAllowedMethods().isEmpty()) {
            // Split the comma-separated string into individual methods
            allowedMethods.addAll(Arrays.asList(entity.getAllowedMethods().split(",")));
        } else if (entity.getMethod() != null) {
            // Fallback to single method if allowedMethods is not set
            allowedMethods.add(entity.getMethod());
        }

        // Use the version from entity or default to "v1" if not available
        String version = entity.getVersion() != null ? entity.getVersion() : "v1";

        Resource resource = new Resource(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getUrlPattern(),
                version,
                allowedMethods
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

        // Convert the set of allowed methods to a comma-separated string
        String allowedMethodsStr = domain.getAllowedMethods().stream()
                .collect(Collectors.joining(","));

        // Get the primary method (first one or empty)
        String primaryMethod = domain.getAllowedMethods().isEmpty() ?
                              "" : domain.getAllowedMethods().iterator().next();

        ResourceEntity entity = new ResourceEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setUrlPattern(domain.getUrlPattern());
        entity.setMethod(primaryMethod);
        entity.setVersion(domain.getVersion());
        entity.setAllowedMethods(allowedMethodsStr);

        return entity;
    }
}
