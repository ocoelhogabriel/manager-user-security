package com.ocoelhogabriel.manager_user_security.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.exception.DuplicateResourceException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.ResourceRepository;

/**
 * Implementation of the ResourceService interface.
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    
    /**
     * Constructor.
     *
     * @param resourceRepository the resource repository
     */
    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Resource findById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Resource> findByName(String name) {
        return resourceRepository.findByName(name);
    }

    @Override
    @Transactional
    public Resource create(Resource resource) {
        // Check if resource with same urlPattern and method already exists
        Optional<Resource> existingResource = resourceRepository
                .findByUrlPatternAndMethod(resource.getUrlPattern(), getFirstAllowedMethod(resource));

        if (existingResource.isPresent()) {
            throw new DuplicateResourceException("Resource with URL pattern " + resource.getUrlPattern() +
                    " and method " + getFirstAllowedMethod(resource) + " already exists");
        }
        
        return resourceRepository.save(resource);
    }

    @Override
    @Transactional
    public Resource update(Resource resource) {
        // Verify resource exists
        resourceRepository.findById(resource.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Resource", resource.getId()));

        // Check if resource with same urlPattern and method already exists (excluding this one)
        Optional<Resource> existingResource = resourceRepository
                .findByUrlPatternAndMethod(resource.getUrlPattern(), getFirstAllowedMethod(resource));

        if (existingResource.isPresent() && !existingResource.get().getId().equals(resource.getId())) {
            throw new DuplicateResourceException("Resource with URL pattern " + resource.getUrlPattern() +
                    " and method " + getFirstAllowedMethod(resource) + " already exists");
        }

        return resourceRepository.save(resource);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource", id);
        }
        
        resourceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resource> findMatchingResources(String url, String method) {
        return resourceRepository.findMatchingResources(url, method);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resource> findByVersion(String version) {
        return resourceRepository.findByVersion(version);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Resource> findByUrlPattern(String urlPattern) {
        // Supondo que o método findByMatchingUrl faz o papel de busca por padrão
        return resourceRepository.findByMatchingUrl(urlPattern);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUrlPattern(String urlPattern) {
        return resourceRepository.existsByUrlPattern(urlPattern);
    }

    /**
     * Helper method to get the first allowed method from a resource.
     *
     * @param resource the resource
     * @return the first allowed method or an empty string if none
     */
    private String getFirstAllowedMethod(Resource resource) {
        return resource.getAllowedMethods().isEmpty() ?
               "" : resource.getAllowedMethods().iterator().next();
    }
}
