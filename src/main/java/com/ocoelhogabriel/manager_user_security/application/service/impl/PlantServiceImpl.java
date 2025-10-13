package com.ocoelhogabriel.manager_user_security.application.service.impl;

import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.CompanyRepository;
import com.ocoelhogabriel.manager_user_security.domain.repository.PlantRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of PlantService that handles business logic for Plant operations.
 */
@Service
public class PlantServiceImpl implements PlantService {

    private final PlantRepository plantRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public PlantServiceImpl(PlantRepository plantRepository, CompanyRepository companyRepository) {
        this.plantRepository = plantRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public Plant registerPlant(Plant plant) {
        validatePlant(plant);
        
        // Ensure the referenced company exists
        if (!companyRepository.findById(plant.getCompanyId()).isPresent()) {
            throw new ResourceNotFoundException("Company not found with ID: " + plant.getCompanyId());
        }
        
        return plantRepository.save(plant);
    }

    @Override
    @Transactional
    public Plant updatePlant(Plant plant) {
        validatePlant(plant);
        
        if (plant.getId() == null) {
            throw new DomainException("Plant ID must be provided for update");
        }

        // Check if plant exists
        plantRepository.findById(plant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with ID: " + plant.getId()));

        // Ensure the referenced company exists
        if (!companyRepository.findById(plant.getCompanyId()).isPresent()) {
            throw new ResourceNotFoundException("Company not found with ID: " + plant.getCompanyId());
        }
        
        return plantRepository.save(plant);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Plant> findById(Long id) {
        return plantRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plant> findByCompanyId(Long companyId) {
        // Check if company exists before searching for its plants
        if (!companyRepository.findById(companyId).isPresent()) {
            throw new ResourceNotFoundException("Company not found with ID: " + companyId);
        }
        
        return plantRepository.findByCompanyId(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plant> findAllPlants() {
        return plantRepository.findAll();
    }

    @Override
    @Transactional
    public void deletePlant(Long id) {
        if (!plantRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Plant not found with ID: " + id);
        }
        plantRepository.deleteById(id);
    }
    
    private void validatePlant(Plant plant) {
        if (!plant.isValid()) {
            throw new DomainException("Invalid plant data");
        }
    }
}
