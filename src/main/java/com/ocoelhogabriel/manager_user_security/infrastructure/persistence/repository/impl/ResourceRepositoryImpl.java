package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.repository.ResourceRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.ResourceMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.ResourceJpaRepository;

/**
 * Implementation of the ResourceRepository interface.
 * This class adapts the Spring Data JPA repository to the domain repository interface.
 */
@Component
public class ResourceRepositoryImpl implements ResourceRepository {

    private final ResourceJpaRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    /**
     * Constructor.
     *
     * @param resourceRepository the JPA resource repository
     * @param resourceMapper the resource mapper
     */
    public ResourceRepositoryImpl(ResourceJpaRepository resourceRepository,
                             ResourceMapper resourceMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public Resource save(Resource resource) {
        var resourceEntity = resourceMapper.toEntity(resource);
        var savedEntity = resourceRepository.save(resourceEntity);
        return resourceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Resource> findById(Long id) {
        return resourceRepository.findById(id)
                .map(resourceMapper::toDomain);
    }

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll().stream()
                .map(resourceMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(Resource resource) {
        resourceRepository.deleteById(resource.getId());
    }

    @Override
    public void deleteById(Long id) {
        resourceRepository.deleteById(id);
    }

    @Override
    public Optional<Resource> findByName(String name) {
        return resourceRepository.findByName(name)
                .map(resourceMapper::toDomain);
    }

    @Override
    public List<Resource> findByVersion(String version) {
        // Implementation depends on ResourceEntity having a version field
        // For now, filtering from all resources
        return resourceRepository.findAll().stream()
                .map(resourceMapper::toDomain)
                .filter(resource -> resource.getVersion().equals(version))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsById(Long id) {
        return resourceRepository.existsById(id);
    }
}
