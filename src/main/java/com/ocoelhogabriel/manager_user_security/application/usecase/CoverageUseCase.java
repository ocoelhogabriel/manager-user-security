package com.ocoelhogabriel.manager_user_security.application.usecase;

import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.domain.service.CoverageService;
import com.ocoelhogabriel.manager_user_security.domain.service.UserService;
import com.ocoelhogabriel.manager_user_security.domain.service.CompanyService;
import com.ocoelhogabriel.manager_user_security.domain.service.PlantService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use case for Coverage-related operations.
 */
@Component
public class CoverageUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CoverageUseCase.class);

    private final CoverageService coverageService;
    private final UserService userService;
    private final CompanyService companyService;
    private final PlantService plantService;

    @Autowired
    public CoverageUseCase(
            CoverageService coverageService,
            UserService userService,
            CompanyService companyService,
            PlantService plantService) {
        this.coverageService = coverageService;
        this.userService = userService;
        this.companyService = companyService;
        this.plantService = plantService;
    }

    /**
     * Create a new coverage for a user.
     *
     * @param userId The ID of the user
     * @param companyId The ID of the company
     * @param plantId The ID of the plant (optional, can be null)
     * @param description The description for the coverage (optional)
     * @return The created coverage
     */
    @Transactional
    public Coverage createCoverage(Long userId, Long companyId, Long plantId, String description) {
        logger.info("Creating coverage for user ID: {}, company ID: {}, plant ID: {}", 
                userId, companyId, plantId);
        
        User user = userService.getUserById(userId);
        Company company = companyService.getCompanyById(companyId);
        Plant plant = plantId != null ? plantService.getPlantById(plantId) : null;
        
        Coverage coverage = Coverage.builder()
                .user(user)
                .company(company)
                .plant(plant)
                .description(description)
                .active(true)
                .build();
        
        return coverageService.createCoverage(coverage);
    }

    /**
     * Get a coverage by ID.
     *
     * @param id The coverage ID
     * @return The coverage
     */
    @Transactional(readOnly = true)
    public Coverage getCoverageById(Long id) {
        return coverageService.getCoverageById(id);
    }

    /**
     * Update an existing coverage.
     *
     * @param id The coverage ID
     * @param companyId The company ID (optional, null if not changing)
     * @param plantId The plant ID (optional, null if not changing)
     * @param description The description (optional, null if not changing)
     * @param active The active status (optional, null if not changing)
     * @return The updated coverage
     */
    @Transactional
    public Coverage updateCoverage(Long id, Long companyId, Long plantId, String description, Boolean active) {
        logger.info("Updating coverage ID: {}", id);
        
        Coverage existingCoverage = coverageService.getCoverageById(id);
        
        // Build updated coverage with changes
        Coverage.Builder builder = Coverage.builder()
                .id(id)
                .user(existingCoverage.getUser());
                
        // Update company if provided
        if (companyId != null) {
            Company company = companyService.getCompanyById(companyId);
            builder.company(company);
        } else {
            builder.company(existingCoverage.getCompany());
        }
        
        // Update plant if provided
        if (plantId != null) {
            Plant plant = plantService.getPlantById(plantId);
            builder.plant(plant);
        } else {
            builder.plant(existingCoverage.getPlant());
        }
        
        // Update description if provided
        builder.description(description != null ? description : existingCoverage.getDescription());
        
        // Update active status if provided
        builder.active(active != null ? active : existingCoverage.isActive());
        
        Coverage updatedCoverage = builder.build();
        return coverageService.updateCoverage(id, updatedCoverage);
    }

    /**
     * Delete a coverage.
     *
     * @param id The coverage ID
     */
    @Transactional
    public void deleteCoverage(Long id) {
        logger.info("Deleting coverage ID: {}", id);
        coverageService.deleteCoverage(id);
    }

    /**
     * Get all coverages for a user.
     *
     * @param userId The user ID
     * @return List of coverages for the user
     */
    @Transactional(readOnly = true)
    public List<Coverage> getCoveragesByUser(Long userId) {
        return coverageService.getCoveragesByUserId(userId);
    }

    /**
     * Get all coverages for a company.
     *
     * @param companyId The company ID
     * @return List of coverages for the company
     */
    @Transactional(readOnly = true)
    public List<Coverage> getCoveragesByCompany(Long companyId) {
        return coverageService.getCoveragesByCompanyId(companyId);
    }

    /**
     * Get all coverages for a plant.
     *
     * @param plantId The plant ID
     * @return List of coverages for the plant
     */
    @Transactional(readOnly = true)
    public List<Coverage> getCoveragesByPlant(Long plantId) {
        return coverageService.getCoveragesByPlantId(plantId);
    }

    /**
     * Get all active coverages for a user.
     *
     * @param userId The user ID
     * @return List of active coverages for the user
     */
    @Transactional(readOnly = true)
    public List<Coverage> getActiveCoveragesByUser(Long userId) {
        return coverageService.getActiveCoveragesByUserId(userId);
    }

    /**
     * Check if a user has access to a company.
     *
     * @param userId The user ID
     * @param companyId The company ID
     * @return True if the user has access, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean hasCompanyAccess(Long userId, Long companyId) {
        return coverageService.hasCompanyAccess(userId, companyId);
    }

    /**
     * Check if a user has access to a plant.
     *
     * @param userId The user ID
     * @param plantId The plant ID
     * @return True if the user has access, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean hasPlantAccess(Long userId, Long plantId) {
        return coverageService.hasPlantAccess(userId, plantId);
    }

    /**
     * Get all coverages.
     *
     * @return List of all coverages
     */
    @Transactional(readOnly = true)
    public List<Coverage> getAllCoverages() {
        return coverageService.getAllCoverages();
    }
}
