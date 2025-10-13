package com.ocoelhogabriel.manager_user_security.application.usecase;

import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Use case for plant operations. This class orchestrates the application logic
 * and delegates to domain services.
 */
@Component
public class PlantUseCase {

    private final PlantService plantService;

    @Autowired
    public PlantUseCase(PlantService plantService) {
        this.plantService = plantService;
    }

    /**
     * Creates a new plant.
     *
     * @param plant The plant to create
     * @return The created plant with ID
     * @throws ResourceNotFoundException if referenced company not found
     * @throws DomainException if plant data is invalid
     */
    public Plant createPlant(Plant plant) {
        return plantService.registerPlant(plant);
    }

    /**
     * Updates an existing plant.
     *
     * @param plant The plant to update
     * @return The updated plant
     * @throws ResourceNotFoundException if plant or referenced company not found
     * @throws DomainException if plant data is invalid
     */
    public Plant updatePlant(Plant plant) {
        return plantService.updatePlant(plant);
    }

    /**
     * Gets a plant by its ID.
     *
     * @param id The plant ID
     * @return The plant
     * @throws ResourceNotFoundException if plant not found
     */
    public Plant getPlantById(Long id) {
        return plantService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with ID: " + id));
    }

    /**
     * Gets all plants for a company.
     *
     * @param companyId The company ID
     * @return List of plants for the company
     * @throws ResourceNotFoundException if company not found
     */
    public List<Plant> getPlantsByCompanyId(Long companyId) {
        return plantService.findByCompanyId(companyId);
    }

    /**
     * Gets all plants.
     *
     * @return List of all plants
     */
    public List<Plant> getAllPlants() {
        return plantService.findAllPlants();
    }

    /**
     * Deletes a plant by its ID.
     *
     * @param id The plant ID
     * @throws ResourceNotFoundException if plant not found
     */
    public void deletePlant(Long id) {
        plantService.deletePlant(id);
    }
}
