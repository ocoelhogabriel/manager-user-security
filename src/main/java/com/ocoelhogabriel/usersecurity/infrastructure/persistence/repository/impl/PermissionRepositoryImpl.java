package com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.usersecurity.domain.entity.Permission;
import com.ocoelhogabriel.usersecurity.domain.repository.PermissionRepository;
import com.ocoelhogabriel.usersecurity.infrastructure.persistence.mapper.PermissionMapper;
import com.ocoelhogabriel.usersecurity.infrastructure.persistence.mapper.ResourceMapper;
import com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.ResourceRepository;

/**
 * Implementation of the PermissionRepository interface.
 * This class adapts the Spring Data JPA repository to the domain repository interface.
 */
@Component
public class PermissionRepositoryImpl implements PermissionRepository {

    private final com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.PermissionRepository permissionRepository;
    private final ResourceRepository resourceRepository;
    private final PermissionMapper permissionMapper;
    private final ResourceMapper resourceMapper;

    /**
     * Constructor.
     *
     * @param permissionRepository the JPA permission repository
     * @param resourceRepository the JPA resource repository
     * @param permissionMapper the permission mapper
     * @param resourceMapper the resource mapper
     */
    public PermissionRepositoryImpl(
            com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.PermissionRepository permissionRepository,
            ResourceRepository resourceRepository,
            PermissionMapper permissionMapper,
            ResourceMapper resourceMapper) {
        this.permissionRepository = permissionRepository;
        this.resourceRepository = resourceRepository;
        this.permissionMapper = permissionMapper;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public Permission save(Permission permission) {
        var permissionEntity = permissionMapper.toEntity(permission);
        var savedEntity = permissionRepository.save(permissionEntity);
        return permissionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toDomain);
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Permission permission) {
        permissionRepository.deleteById(permission.getId());
    }

    @Override
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public List<Permission> findByResource(String resource) {
        var resourceEntity = resourceRepository.findByName(resource);
        if (resourceEntity.isEmpty()) {
            return List.of();
        }
        
        return permissionRepository.findByResourceId(resourceEntity.get().getId()).stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Permission> findByResourceAndAction(String resource, String action) {
        var resourceEntity = resourceRepository.findByName(resource);
        if (resourceEntity.isEmpty()) {
            return Optional.empty();
        }
        
        return permissionRepository.findAll().stream()
                .filter(p -> p.getResource().getId().equals(resourceEntity.get().getId()) && 
                             p.getAction().equals(action))
                .map(permissionMapper::toDomain)
                .findFirst();
    }

    @Override
    public boolean existsByResourceAndAction(String resource, String action) {
        return findByResourceAndAction(resource, action).isPresent();
    }
    
    @Override
    public boolean existsById(Long id) {
        return permissionRepository.existsById(id);
    }
}