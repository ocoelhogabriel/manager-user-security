package com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.usersecurity.domain.entity.Resource;
import com.ocoelhogabriel.usersecurity.domain.repository.ResourceRepository;
import com.ocoelhogabriel.usersecurity.infrastructure.persistence.mapper.ResourceMapper;
import com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.ResourceRepository;

/**
 * Implementation of the ResourceRepository interface.
 * This class adapts the Spring Data JPA repository to the domain repository interface.
 */
@Component
public class ResourceRepositoryImpl implements ResourceRepository {

    private final com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    /**
     * Constructor.
     *
     * @param resourceRepository the JPA resource repository
     * @param resourceMapper the resource mapper
     */
    public ResourceRepositoryImpl(com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.ResourceRepository resourceRepository,
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