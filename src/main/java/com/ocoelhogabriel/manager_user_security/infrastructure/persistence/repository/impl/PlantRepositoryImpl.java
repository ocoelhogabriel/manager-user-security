package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.impl;

import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.domain.repository.PlantRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PlantEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.PlantMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.PlantJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the PlantRepository interface using JPA.
 */
@Repository
public class PlantRepositoryImpl implements PlantRepository {
    
    private final PlantJpaRepository plantJpaRepository;
    private final PlantMapper plantMapper;
    
    public PlantRepositoryImpl(PlantJpaRepository plantJpaRepository, PlantMapper plantMapper) {
        this.plantJpaRepository = plantJpaRepository;
        this.plantMapper = plantMapper;
    }
    
    @Override
    public Plant save(Plant plant) {
        PlantEntity entity;
        
        if (plant.getId() != null) {
            // Update existing plant
            Optional<PlantEntity> existingEntity = plantJpaRepository.findById(plant.getId());
            if (existingEntity.isPresent()) {
                entity = plantMapper.updateEntity(existingEntity.get(), plant);
            } else {
                entity = plantMapper.toEntity(plant);
            }
        } else {
            // Create new plant
            entity = plantMapper.toEntity(plant);
        }
        
        PlantEntity savedEntity = plantJpaRepository.save(entity);
        return plantMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Plant> findById(Long id) {
        return plantJpaRepository.findById(id)
                .map(plantMapper::toDomain);
    }
    
    @Override
    public List<Plant> findAll() {
        return plantJpaRepository.findAll().stream()
                .map(plantMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Plant> findByCompanyId(Long companyId) {
        return plantJpaRepository.findByCompanyId(companyId).stream()
                .map(plantMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        plantJpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByNameAndCompanyId(String name, Long companyId) {
        return plantJpaRepository.existsByNameAndCompanyId(name, companyId);
    }
}
