package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.resource.CreateResourceRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.resource.ResourceResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.resource.UpdateResourceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Consolidated Mapper for Resource entity using MapStruct.
 * Handles conversions between Domain, DTOs, and Persistence entities.
 */
@Mapper(componentModel = "spring")
public interface ResourceMapper {

    // --- DTO -> Domain ---
    @Mapping(source = "path", target = "urlPattern")
    @Mapping(source = "method", target = "allowedMethods", qualifiedByName = "stringToSet")
    @Mapping(target = "id", ignore = true)
    Resource toDomain(CreateResourceRequest request);

    @Mapping(source = "path", target = "urlPattern")
    @Mapping(source = "method", target = "allowedMethods", qualifiedByName = "stringToSet")
    Resource toDomain(UpdateResourceRequest request);

    // --- Domain -> DTO ---
    @Mapping(source = "urlPattern", target = "path")
    @Mapping(source = "allowedMethods", target = "method", qualifiedByName = "setToString")
    ResourceResponse toResponse(Resource domain);

    List<ResourceResponse> toResponseList(List<Resource> domains);

    // --- Persistence -> Domain ---
    @Mapping(source = "urlPattern", target = "urlPattern")
    @Mapping(source = "allowedMethods", target = "allowedMethods", qualifiedByName = "commaSeparatedStringToSet")
    Resource toDomain(ResourceEntity entity);

    List<Resource> toDomainList(List<ResourceEntity> entities);

    // --- Domain -> Persistence ---
    @Mapping(source = "urlPattern", target = "urlPattern")
    @Mapping(source = "allowedMethods", target = "allowedMethods", qualifiedByName = "setTocommaSeparatedString")
    ResourceEntity toPersistenceEntity(Resource domain);

    // --- Helper Methods ---
    @Named("stringToSet")
    default Set<String> stringToSet(String method) {
        return method == null || method.isBlank() ? Collections.emptySet() : Set.of(method);
    }

    @Named("setToString")
    default String setToFirstString(Set<String> methods) {
        return methods == null || methods.isEmpty() ? null : methods.iterator().next();
    }

    @Named("commaSeparatedStringToSet")
    default Set<String> commaSeparatedStringToSet(String methods) {
        if (methods == null || methods.isBlank()) {
            return Collections.emptySet();
        }
        return Arrays.stream(methods.split(",")).map(String::trim).collect(Collectors.toSet());
    }

    @Named("setTocommaSeparatedString")
    default String setTocommaSeparatedString(Set<String> methods) {
        if (methods == null || methods.isEmpty()) {
            return null;
        }
        return String.join(",", methods);
    }
}
