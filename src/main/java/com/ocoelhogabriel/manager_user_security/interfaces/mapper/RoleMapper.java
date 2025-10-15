package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.role.CreateRoleRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.role.RoleResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.role.UpdateRoleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Consolidated Mapper for Role entity using MapStruct.
 * Handles conversions between Domain, DTOs, and Persistence entities.
 */
@Mapper(componentModel = "spring", uses = { PermissionMapper.class })
public interface RoleMapper {

    // --- DTO -> Domain ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    Role toDomain(CreateRoleRequest request);

    @Mapping(target = "permissions", ignore = true)
    Role toDomain(UpdateRoleRequest request);

    // --- Domain -> DTO ---
    RoleResponse toResponse(Role domain);

    List<RoleResponse> toResponseList(List<Role> domains);

    // --- Persistence -> Domain ---
    Role toDomain(RoleEntity entity);

    List<Role> toDomainList(List<RoleEntity> entities);

    // --- Domain -> Persistence ---
    RoleEntity toPersistenceEntity(Role domain);

    List<RoleEntity> toPersistenceEntityList(List<Role> domains);
}
