package com.ocoelhogabriel.manager_user_security.domain.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;

import java.util.List;

/**
 * Service interface for Coverage domain operations.
 */
public interface CoverageService {
    
    /**
     * Create a new coverage.
     *
     * @param coverage The coverage to create
     * @return The created coverage with ID
     */
    Coverage createCoverage(Coverage coverage);
    
    /**
     * Get a coverage by its ID.
     *
     * @param id The coverage ID
     * @return The coverage if found
     * @throws ResourceNotFoundException if coverage is not found
     */
    Coverage getCoverageById(Long id);
    
    /**
     * Update an existing coverage.
     *
     * @param id The ID of the coverage to update
     * @param coverage The new coverage data
     * @return The updated coverage
     * @throws ResourceNotFoundException if coverage is not found
     */
    Coverage updateCoverage(Long id, Coverage coverage);
    
    /**
     * Delete a coverage by ID.
     *
     * @param id The ID of the coverage to delete
     * @throws ResourceNotFoundException if coverage is not found
     */
    void deleteCoverage(Long id);
    
    /**
     * Get all coverages for a user.
     *
     * @param userId The user ID
     * @return List of coverages for the user
     */
    List<Coverage> getCoveragesByUserId(Long userId);
    
    /**
     * Get all coverages for a company.
     *
     * @param companyId The company ID
     * @return List of coverages for the company
     */
    List<Coverage> getCoveragesByCompanyId(Long companyId);
    
    /**
     * Get all coverages for a plant.
     *
     * @param plantId The plant ID
     * @return List of coverages for the plant
     */
    List<Coverage> getCoveragesByPlantId(Long plantId);
    
    /**
     * Check if a user has access to a specific company.
     *
     * @param userId The user ID
     * @param companyId The company ID
     * @return true if user has access to the company, false otherwise
     */
    boolean hasCompanyAccess(Long userId, Long companyId);
    
    /**
     * Check if a user has access to a specific plant.
     *
     * @param userId The user ID
     * @param plantId The plant ID
     * @return true if user has access to the plant, false otherwise
     */
    boolean hasPlantAccess(Long userId, Long plantId);
    
    /**
     * Get all coverages.
     *
     * @return List of all coverages
     */
    List<Coverage> getAllCoverages();
    
    /**
     * Get all active coverages for a user.
     *
     * @param userId The user ID
     * @return List of active coverages for the user
     */
    List<Coverage> getActiveCoveragesByUserId(Long userId);
}
