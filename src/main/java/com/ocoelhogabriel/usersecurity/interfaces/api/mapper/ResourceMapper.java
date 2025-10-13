package com.ocoelhogabriel.usersecurity.interfaces.api.mapper;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.usersecurity.domain.entity.Resource;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.resource.CreateResourceRequest;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.resource.ResourceResponse;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.resource.UpdateResourceRequest;

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

        return new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getPath(),
                resource.getDescription(),
                resource.getMethod()
        );
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

        Resource resource = new Resource();
        resource.setName(request.getName());
        resource.setPath(request.getPath());
        resource.setDescription(request.getDescription());
        resource.setMethod(request.getMethod());

        return resource;
    }

    /**
     * Maps an UpdateResourceRequest DTO to Resource entity.
     *
     * @param request the UpdateResourceRequest DTO
     * @return the Resource entity
     */
    public Resource toEntity(UpdateResourceRequest request) {
        if (request == null) {
            return null;
        }

        Resource resource = new Resource();
        resource.setName(request.getName());
        resource.setPath(request.getPath());
        resource.setDescription(request.getDescription());
        resource.setMethod(request.getMethod());

        return resource;
    }
}