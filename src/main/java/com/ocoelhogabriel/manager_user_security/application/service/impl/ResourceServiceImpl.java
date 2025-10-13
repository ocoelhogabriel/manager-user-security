package com.ocoelhogabriel.manager_user_security.application.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocoelhogabriel.manager_user_security.application.service.ResourceService;
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
    @Transactional
    public Resource create(Resource resource) {
        // Check if resource with same path and method already exists
        Optional<Resource> existingResource = resourceRepository
                .findByPathAndMethod(resource.getPath(), resource.getMethod());
        
        if (existingResource.isPresent()) {
            throw new DuplicateResourceException("Resource", 
                    "path and method", resource.getPath() + " - " + resource.getMethod());
        }
        
        return resourceRepository.save(resource);
    }

    @Override
    @Transactional
    public Resource update(Resource resource) {
        // Check if resource exists
        resourceRepository.findById(resource.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource", resource.getId()));
        
        // Check if updated resource would conflict with existing one
        Optional<Resource> existingResource = resourceRepository
                .findByPathAndMethodAndIdNot(resource.getPath(), resource.getMethod(), resource.getId());
        
        if (existingResource.isPresent()) {
            throw new DuplicateResourceException("Resource", 
                    "path and method", resource.getPath() + " - " + resource.getMethod());
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
    public List<Resource> findByPathAndMethod(String path, String method) {
        return resourceRepository.findMatchingResources(path, method);
    }
}
