package com.ocoelhogabriel.manager_user_security.domain.repository;

import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Plant domain entity.
 * This is part of the domain layer and doesn't depend on infrastructure.
 */
public interface PlantRepository {
    
    /**
     * Save or update a plant
     *
     * @param plant The plant to save or update
     * @return The saved plant with ID
     */
    Plant save(Plant plant);
    
    /**
     * Find a plant by its ID
     *
     * @param id The plant ID
     * @return An Optional containing the plant if found
     */
    Optional<Plant> findById(Long id);
    
    /**
     * Find all plants
     *
     * @return A list of all plants
     */
    List<Plant> findAll();
    
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
     */
    void deleteById(Long id);
    
    /**
     * Check if a plant exists by name and company ID
     *
     * @param name The plant name
     * @param companyId The company ID
     * @return true if exists, false otherwise
     */
    boolean existsByNameAndCompanyId(String name, Long companyId);
}
