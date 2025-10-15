package com.ocoelhogabriel.manager_user_security.domain.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;

import java.util.List;

/**
 * Service interface for Coverage domain operations.
 */
public interface CoverageService {

    Coverage createCoverage(Long userId, Long companyId, Long plantId, String description);

    Coverage getCoverageById(Long id);

    Coverage updateCoverage(Long id, Long companyId, Long plantId, String description, Boolean active);

    void deleteCoverage(Long id);

    List<Coverage> getCoveragesByUser(Long userId);

    List<Coverage> getCoveragesByCompany(Long companyId);

    List<Coverage> getCoveragesByPlant(Long plantId);

    boolean hasCompanyAccess(Long userId, Long companyId);

    boolean hasPlantAccess(Long userId, Long plantId);

    List<Coverage> getAllCoverages();

    List<Coverage> getActiveCoveragesByUser(Long userId);
}
