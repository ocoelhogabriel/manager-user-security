package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.resource.CreateResourceRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.resource.ResourceResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.resource.UpdateResourceRequest;

/**
 * Mapper for Resource entities and DTOs.
 */
@Component
public class ResourceMapper {

    /**
     * Maps a Resource entity to ResourceResponse DTO.
     *
     * @param resource the Resource entity
     * @return the ResourceResponse DTO
     */
    public ResourceResponse toResponse(Resource resource) {
        if (resource == null) {
            return null;
        }

        ResourceResponse response = new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getUrlPattern(),
                resource.getDescription(),
                resource.getAllowedMethods().isEmpty() ? null : resource.getAllowedMethods().iterator().next()
        );
        
        return response;
    }

    /**
     * Maps a CreateResourceRequest DTO to Resource entity.
     *
     * @param request the CreateResourceRequest DTO
     * @return the Resource entity
     */
    public Resource toEntity(CreateResourceRequest request) {
        if (request == null) {
            return null;
        }

        // Create a set of allowed methods
        Set<String> allowedMethods = new HashSet<>();
        if (request.getMethod() != null && !request.getMethod().isEmpty()) {
            allowedMethods.add(request.getMethod());
        }

        // Create a new Resource using constructor with values from request
        Resource resource = new Resource(
            null,  // ID will be assigned by the database
            request.getName(),
            request.getDescription(),
            request.getPath(),
            "v1",  // Default version value
            allowedMethods
        );
        
        return resource;
    }

    /**
     * Maps an UpdateResourceRequest DTO to Resource entity.
     *
     * @param id the ID of the resource
     * @param request the UpdateResourceRequest DTO
     * @return the Resource entity
     */
    public Resource toEntity(Long id, UpdateResourceRequest request) {
        if (request == null) {
            return null;
        }

        // Create a set of allowed methods
        Set<String> allowedMethods = new HashSet<>();
        if (request.getMethod() != null && !request.getMethod().isEmpty()) {
            allowedMethods.add(request.getMethod());
        }

        // Create a Resource with the existing ID and updated values
        Resource resource = new Resource(
            id,
            request.getName(),
            request.getDescription(),
            request.getPath(),
            "v1",  // Default version value
            allowedMethods
        );

        return resource;
    }
    
    /**
     * Updates an existing Resource entity with values from UpdateResourceRequest DTO.
     *
     * @param request the UpdateResourceRequest DTO with new values
     * @param existingResource the existing Resource entity to update
     * @return the updated Resource entity
     */
    public Resource updateEntity(UpdateResourceRequest request, Resource existingResource) {
        if (request == null || existingResource == null) {
            return existingResource;
        }
        
        // Update the fields from the request
        existingResource.setName(request.getName());
        existingResource.setDescription(request.getDescription());
        existingResource.setUrlPattern(request.getPath());
        
        // Update allowed methods
        if (request.getMethod() != null && !request.getMethod().isEmpty()) {
            // First remove all existing methods by getting a copy of the current methods
            Set<String> currentMethods = new HashSet<>(existingResource.getAllowedMethods());
            for (String method : currentMethods) {
                existingResource.removeAllowedMethod(method);
            }
            
            // Add the new method
            existingResource.addAllowedMethod(request.getMethod());
        }
        
        return existingResource;
    }
}
