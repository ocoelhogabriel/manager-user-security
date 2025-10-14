package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.adapter;

import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.domain.repository.PlantRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.PlantMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.PlantJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PlantRepositoryAdapter implements PlantRepository {

    private final PlantJpaRepository plantJpaRepository;
    private final PlantMapper plantMapper;

    @Autowired
    public PlantRepositoryAdapter(PlantJpaRepository plantJpaRepository, PlantMapper plantMapper) {
        this.plantJpaRepository = plantJpaRepository;
        this.plantMapper = plantMapper;
    }

    @Override
    public Plant save(Plant plant) {
        var entity = plantMapper.toEntity(plant);
        var savedEntity = plantJpaRepository.save(entity);
        return plantMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Plant> findById(Long id) {
        return plantJpaRepository.findById(id).map(plantMapper::toDomain);
    }

    @Override
    public List<Plant> findByCompanyId(Long companyId) {
        return plantJpaRepository.findByCompanyId(companyId).stream()
                .map(plantMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Plant> findAll() {
        return plantJpaRepository.findAll().stream()
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
