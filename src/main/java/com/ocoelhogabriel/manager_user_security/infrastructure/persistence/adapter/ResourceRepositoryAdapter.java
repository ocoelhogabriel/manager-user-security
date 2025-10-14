package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.adapter;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.repository.ResourceRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.ResourceMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.ResourceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ResourceRepositoryAdapter implements ResourceRepository {

    private final ResourceJpaRepository resourceJpaRepository;
    private final ResourceMapper resourceMapper;

    @Autowired
    public ResourceRepositoryAdapter(ResourceJpaRepository resourceJpaRepository, ResourceMapper resourceMapper) {
        this.resourceJpaRepository = resourceJpaRepository;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public Resource save(Resource resource) {
        var entity = resourceMapper.toEntity(resource);
        var savedEntity = resourceJpaRepository.save(entity);
        return resourceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Resource> findById(Long id) {
        return resourceJpaRepository.findById(id).map(resourceMapper::toDomain);
    }

    @Override
    public Optional<Resource> findByName(String name) {
        return resourceJpaRepository.findByName(name).map(resourceMapper::toDomain);
    }

    @Override
    public List<Resource> findAll() {
        return resourceJpaRepository.findAll().stream()
                .map(resourceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        resourceJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Resource> findByUrlPatternAndMethod(String urlPattern, String method) {
        return resourceJpaRepository.findByUrlPatternAndAllowedMethodsContains(urlPattern, method)
                .map(resourceMapper::toDomain);
    }

    @Override
    public List<Resource> findMatchingResources(String url, String method) {
        // This logic might be complex and better suited for a custom query in the JpaRepository
        // For now, we delegate to a method that should exist on the JpaRepository.
        return resourceJpaRepository.findMatchingResources(url, method).stream()
                .map(resourceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Resource> findByPathAndMethod(String path, String method) {
        // Assuming this is a more specific query
        return resourceJpaRepository.findByUrlPatternAndAllowedMethodsContains(path, method).stream()
                .map(resourceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return resourceJpaRepository.existsById(id);
    }
}
