package com.ocoelhogabriel.manager_user_security.domain.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Plant operations in the domain layer.
 */
public interface PlantService {
    
    /**
     * Register a new plant
     *
     * @param plant The plant to register
     * @return The registered plant with ID
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.DuplicateResourceException if plant with same name already exists for the company
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if company not found
     */
    Plant registerPlant(Plant plant);
    
    /**
     * Update an existing plant
     *
     * @param plant The plant to update
     * @return The updated plant
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if plant not found
     */
    Plant updatePlant(Plant plant);
    
    /**
     * Find a plant by its ID
     *
     * @param id The plant ID
     * @return An Optional containing the plant if found
     */
    Optional<Plant> findById(Long id);
    
    /**
     * Get a plant by its ID
     *
     * @param id The plant ID
     * @return The plant with the given ID
     * @throws com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException if plant not found
     */
    Plant getPlantById(Long id);
    
    /**
     * Find all plants
     *
     * @return A list of all plants
     */
    List<Plant> findAllPlants();
    
    /**
     * Find all plants by company ID
     *
     * @param companyId The company ID
     * @return A list of plants for the specified company
     */
    List<Plant> findByCompanyId(Long companyId);
    
    /**
     * Delete a plant by its ID
     *
     * @param id The plant ID
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if plant not found
     */
    void deletePlant(Long id);
}
