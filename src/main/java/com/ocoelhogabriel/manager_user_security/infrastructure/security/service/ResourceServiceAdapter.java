package com.ocoelhogabriel.manager_user_security.infrastructure.security.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.repository.ResourceRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // Adicionado

@Service
public class ResourceServiceAdapter implements ResourceService {

    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceServiceAdapter(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    public Resource findById(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<Resource> findByName(String name) { // Corrigido
        return resourceRepository.findByName(name);
    }

    @Override
    public Resource create(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Override
    public Resource update(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Override
    public void delete(Long id) {
        resourceRepository.deleteById(id);
    }

    @Override
    public List<Resource> findMatchingResources(String url, String method) {
        return resourceRepository.findMatchingResources(url, method);
    }

    @Override
    public List<Resource> findByVersion(String version) {
        return resourceRepository.findByVersion(version);
    }

    @Override
    public Optional<Resource> findByUrlPattern(String urlPattern) {
        return resourceRepository.findByMatchingUrl(urlPattern);
    }

    @Override
    public boolean existsByUrlPattern(String urlPattern) {
        return resourceRepository.existsByUrlPattern(urlPattern);
    }

    // O método findByPathAndMethod foi removido
}
