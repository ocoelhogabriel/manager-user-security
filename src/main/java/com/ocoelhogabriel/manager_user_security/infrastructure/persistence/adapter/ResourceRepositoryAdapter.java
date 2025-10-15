package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.adapter;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.repository.ResourceRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.ResourceJpaRepository;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.ResourceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ResourceRepositoryAdapter implements ResourceRepository {

    private final ResourceJpaRepository resourceJpaRepository;
    private final ResourceMapper resourceMapper;

    public ResourceRepositoryAdapter(ResourceJpaRepository resourceJpaRepository, ResourceMapper resourceMapper) {
        this.resourceJpaRepository = resourceJpaRepository;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public Resource save(Resource resource) {
        var entity = resourceMapper.toPersistenceEntity(resource);
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
                .toList();
    }

    @Override
    public void delete(Resource resource) {
        resourceJpaRepository.delete(resourceMapper.toPersistenceEntity(resource));
    }

    @Override
    public void deleteById(Long id) {
        resourceJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Resource> findByUrlPatternAndMethod(String urlPattern, String method) {
        List<ResourceEntity> entities = resourceJpaRepository.findByUrlPatternAndAllowedMethodsContains(urlPattern, method);
        return entities.stream().findFirst().map(resourceMapper::toDomain);
    }

    @Override
    public List<Resource> findMatchingResources(String url, String method) {
        return resourceJpaRepository.findMatchingResources(url, method).stream()
                .map(resourceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return resourceJpaRepository.existsById(id);
    }

    @Override
    public Optional<Resource> findByMatchingUrl(String url) {
        return Optional.empty();
    }

    @Override
    public List<Resource> findByVersion(String version) {
        return resourceJpaRepository.findByVersion(version).stream()
                .map(resourceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByUrlPattern(String urlPattern) {
        return resourceJpaRepository.existsByUrlPattern(urlPattern);
    }
}
