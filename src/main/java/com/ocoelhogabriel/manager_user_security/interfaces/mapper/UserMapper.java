package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.UserEntity;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Consolidated Mapper for User entity using MapStruct.
 * Handles conversions between Domain, DTOs, and Persistence entities.
 */
@Mapper(componentModel = "spring", uses = { RoleMapper.class })
public interface UserMapper {

    // --- Domain -> DTO ---
    UserResponse toResponse(User domain);

    List<UserResponse> toResponseList(List<User> domains);

    // --- Persistence -> Domain ---
    @Mapping(source = "password", target = "passwordHash")
    User toDomain(UserEntity entity);

    List<User> toDomainList(List<UserEntity> entities);

    // --- Domain -> Persistence ---
    @Mapping(source = "passwordHash", target = "password")
    UserEntity toPersistenceEntity(User domain);

    List<UserEntity> toPersistenceEntityList(List<User> domains);
}
