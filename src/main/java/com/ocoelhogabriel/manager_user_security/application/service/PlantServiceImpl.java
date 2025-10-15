package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.CompanyRepository;
import com.ocoelhogabriel.manager_user_security.domain.repository.PlantRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.PlantService;
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

    public PlantServiceImpl(PlantRepository plantRepository, CompanyRepository companyRepository) {
        this.plantRepository = plantRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public Plant registerPlant(Plant plant) {
        validatePlant(plant);
        companyRepository.findById(plant.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + plant.getCompanyId()));
        return plantRepository.save(plant);
    }

    @Override
    @Transactional
    public Plant updatePlant(Plant plant) {
        validatePlant(plant);
        if (plant.getId() == null) {
            throw new DomainException("Plant ID must be provided for update");
        }
        plantRepository.findById(plant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with ID: " + plant.getId()));
        companyRepository.findById(plant.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + plant.getCompanyId()));
        return plantRepository.save(plant);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Plant> findById(Long id) {
        return plantRepository.findById(id).map(this::enrichPlantWithCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public Plant getPlantById(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with ID: " + id));
        return enrichPlantWithCompany(plant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plant> findByCompanyId(Long companyId) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));
        return plantRepository.findByCompanyId(companyId).stream()
                .map(this::enrichPlantWithCompany)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plant> findAllPlants() {
        return plantRepository.findAll().stream()
                .map(this::enrichPlantWithCompany)
                .toList();
    }

    @Override
    @Transactional
    public void deletePlant(Long id) {
        getPlantById(id); // Ensures plant exists
        plantRepository.deleteById(id);
    }

    private void validatePlant(Plant plant) {
        if (!plant.isValid()) {
            throw new DomainException("Invalid plant data");
        }
    }

    private Plant enrichPlantWithCompany(Plant plant) {
        if (plant.getCompanyId() != null && plant.getCompanyId() == null) {
            Company company = companyRepository.findById(plant.getCompanyId())
                    .orElseThrow(() -> new DomainException("Inconsistent data: Plant " + plant.getId() + " has an invalid company ID: " + plant.getCompanyId()));
            plant.setCompanyId(company.getId());
        }
        return plant;
    }
}
