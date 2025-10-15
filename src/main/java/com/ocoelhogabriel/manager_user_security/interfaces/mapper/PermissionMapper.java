package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.permission.CreatePermissionRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.permission.PermissionResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.permission.UpdatePermissionRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.resource.ResourceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Consolidated Mapper for Permission entity using MapStruct.
 * Handles conversions between Domain, DTOs, and Persistence entities.
 */
@Mapper(componentModel = "spring", uses = { ResourceMapper.class })
public interface PermissionMapper {

    // --- DTO -> Domain ---
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "resourceName", target = "resource")
    @Mapping(source = "action", target = "actions", qualifiedByName = "stringToActionSet")
    Permission toDomain(CreatePermissionRequest request);

    @Mapping(source = "resourceName", target = "resource")
    @Mapping(source = "action", target = "actions", qualifiedByName = "stringToActionSet")
    Permission toDomain(UpdatePermissionRequest request);

    // --- Domain -> DTO ---
    @Mapping(source = "id", target = "id")
    @Mapping(source = "resource", target = "resource", qualifiedByName = "stringToResourceResponse")
    PermissionResponse toResponse(Permission domain);

    @Named("stringToResourceResponse")
    default ResourceResponse stringToResourceResponse(String resourceName) {
        ResourceResponse response = new ResourceResponse();
        response.setName(resourceName);
        return response;
    }

    List<PermissionResponse> toResponseList(List<Permission> domains);

    // --- Persistence -> Domain ---
    @Mapping(source = "resource.name", target = "resource")
    @Mapping(source = "action", target = "actions", qualifiedByName = "stringToActionSet")
    Permission toDomain(PermissionEntity entity);

    List<Permission> toDomainList(List<PermissionEntity> entities);

    // --- Domain -> Persistence ---
    @Mapping(target = "id", source = "id")
    @Mapping(target = "resource", ignore = true) // The service will fetch and set the ResourceEntity
    @Mapping(target = "action", source = "actions", qualifiedByName = "actionSetToString")
    @Mapping(target = "name", expression = "java(domain.getResource() + \"_\" + domain.getActions().stream().findFirst().orElse(\"\"))")
    @Mapping(target = "description", expression = "java(\"Permission to \" + domain.getActions().stream().findFirst().orElse(\"\") + \" on \" + domain.getResource())")
    PermissionEntity toPersistenceEntity(Permission domain);

    // --- Helper Methods for Action mapping ---
    @Named("stringToActionSet")
    default Set<String> stringToActionSet(String action) {
        return action == null || action.isBlank() ? Collections.emptySet() : Set.of(action);
    }

    @Named("actionSetToString")
    default String actionSetToString(Set<String> actions) {
        return actions == null || actions.isEmpty() ? null : actions.iterator().next();
    }
}
