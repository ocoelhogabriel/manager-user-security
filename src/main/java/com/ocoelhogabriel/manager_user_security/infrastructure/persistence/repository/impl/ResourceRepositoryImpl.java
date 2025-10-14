package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.repository.ResourceRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;
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
        return resourceRepository.findByVersion(version).stream()
                .map(resourceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return resourceRepository.existsById(id);
    }

    @Override
    public boolean existsByUrlPattern(String urlPattern) {
        return resourceRepository.existsByUrlPattern(urlPattern);
    }

    @Override
    public Optional<Resource> findByMatchingUrl(String url) {
        // Find all resources and filter by matching URL pattern
        List<ResourceEntity> allResources = resourceRepository.findAll();

        for (ResourceEntity entity : allResources) {
            if (urlMatches(entity.getUrlPattern(), url)) {
                return Optional.of(resourceMapper.toDomain(entity));
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Resource> findByUrlPatternAndMethod(String urlPattern, String method) {
        return resourceRepository.findByUrlPatternAndMethod(urlPattern, method)
                .map(resourceMapper::toDomain);
    }

    @Override
    public List<Resource> findMatchingResources(String url, String method) {
        // Find all resources and filter by matching URL pattern and method
        List<ResourceEntity> allResources = resourceRepository.findAll();

        return allResources.stream()
                .filter(entity -> urlMatches(entity.getUrlPattern(), url))
                .filter(entity -> methodMatches(entity, method))
                .map(resourceMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to check if a URL matches a URL pattern.
     * This is a simple implementation. For more complex pattern matching,
     * consider using AntPathMatcher from Spring or similar.
     *
     * @param pattern the URL pattern
     * @param url the URL to check
     * @return true if the URL matches the pattern, false otherwise
     */
    private boolean urlMatches(String pattern, String url) {
        if (pattern == null || url == null) {
            return false;
        }

        // Convert the pattern to a regex pattern
        String regex = pattern
                .replace(".", "\\.")
                .replace("*", ".*")
                .replace("?", ".");

        return Pattern.compile(regex).matcher(url).matches();
    }

    /**
     * Helper method to check if a method matches an entity's method or allowed methods.
     *
     * @param entity the resource entity
     * @param method the HTTP method to check
     * @return true if the method matches, false otherwise
     */
    private boolean methodMatches(ResourceEntity entity, String method) {
        if (method == null) {
            return true; // No method filter, return all resources
        }

        // Check primary method
        if (entity.getMethod() != null && entity.getMethod().equalsIgnoreCase(method)) {
            return true;
        }

        // Check allowed methods if available
        if (entity.getAllowedMethods() != null) {
            String[] methods = entity.getAllowedMethods().split(",");
            for (String m : methods) {
                if (m.trim().equalsIgnoreCase(method)) {
                    return true;
                }
            }
        }

        return false;
    }
}
