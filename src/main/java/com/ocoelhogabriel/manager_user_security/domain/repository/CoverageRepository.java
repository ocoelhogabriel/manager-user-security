package com.ocoelhogabriel.manager_user_security.domain.repository;

import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Coverage entities.
 */
public interface CoverageRepository {
    
    /**
     * Save a coverage entity.
     *
     * @param coverage The coverage to save
     * @return The saved coverage with generated ID
     */
    Coverage save(Coverage coverage);
    
    /**
     * Find a coverage by its ID.
     *
     * @param id The coverage ID
     * @return An Optional containing the coverage if found, empty otherwise
     */
    Optional<Coverage> findById(Long id);
    
    /**
     * Find all coverages for a specific user.
     *
     * @param userId The user ID
     * @return List of coverages associated with the user
     */
    List<Coverage> findByUserId(Long userId);
    
    /**
     * Find all coverages for a specific company.
     *
     * @param companyId The company ID
     * @return List of coverages associated with the company
     */
    List<Coverage> findByCompanyId(Long companyId);
    
    /**
     * Find all coverages for a specific plant.
     *
     * @param plantId The plant ID
     * @return List of coverages associated with the plant
     */
    List<Coverage> findByPlantId(Long plantId);
    
    /**
     * Find all coverages for a user and company.
     *
     * @param userId The user ID
     * @param companyId The company ID
     * @return List of coverages associated with both the user and company
     */
    List<Coverage> findByUserIdAndCompanyId(Long userId, Long companyId);
    
    /**
     * Find all active coverages for a user.
     *
     * @param userId The user ID
     * @return List of active coverages for the user
     */
    List<Coverage> findActiveByUserId(Long userId);
    
    /**
     * Delete a coverage by its ID.
     *
     * @param id The coverage ID to delete
     */
    void deleteById(Long id);
    
    /**
     * Find all coverages.
     *
     * @return List of all coverages
     */
    List<Coverage> findAll();
}
