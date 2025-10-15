package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;
import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.CompanyRepository;
import com.ocoelhogabriel.manager_user_security.domain.repository.CoverageRepository;
import com.ocoelhogabriel.manager_user_security.domain.repository.PlantRepository;
import com.ocoelhogabriel.manager_user_security.domain.repository.UserRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.CoverageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CoverageServiceImpl implements CoverageService {

    private static final Logger logger = LoggerFactory.getLogger(CoverageServiceImpl.class);

    private final CoverageRepository coverageRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PlantRepository plantRepository;

    @Autowired
    public CoverageServiceImpl(
            @Qualifier("coverageRepositoryAdapter") CoverageRepository coverageRepository,
            UserRepository userRepository,
            CompanyRepository companyRepository,
            PlantRepository plantRepository) {
        this.coverageRepository = coverageRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.plantRepository = plantRepository;
    }

    @Override
    @Transactional
    public Coverage createCoverage(Long userId, Long companyId, Long plantId, String description) {
        logger.info("Creating coverage for user ID: {}, company ID: {}, plant ID: {}", userId, companyId, plantId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));
        Plant plant = plantId != null ? plantRepository.findById(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with ID: " + plantId)) : null;

        Coverage coverage = Coverage.builder()
                .user(user)
                .company(company)
                .plant(plant)
                .description(description)
                .active(true)
                .build();

        return coverageRepository.save(coverage);
    }

    @Override
    @Transactional(readOnly = true)
    public Coverage getCoverageById(Long id) {
        logger.debug("Getting coverage with ID: {}", id);
        return coverageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coverage not found with ID: " + id));
    }

    @Override
    @Transactional
    public Coverage updateCoverage(Long id, Long companyId, Long plantId, String description, Boolean active) {
        logger.info("Updating coverage ID: {}", id);

        Coverage existingCoverage = getCoverageById(id);

        Company company = companyId != null ? companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId)) : existingCoverage.getCompany();

        Plant plant = plantId != null ? plantRepository.findById(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with ID: " + plantId)) : existingCoverage.getPlant();

        Coverage updatedCoverage = Coverage.builder()
                .id(id)
                .user(existingCoverage.getUser())
                .company(company)
                .plant(plant)
                .description(description != null ? description : existingCoverage.getDescription())
                .active(active != null ? active : existingCoverage.isActive())
                .build();

        return coverageRepository.save(updatedCoverage);
    }

    @Override
    @Transactional
    public void deleteCoverage(Long id) {
        logger.debug("Deleting coverage with ID: {}", id);
        if (!coverageRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Coverage not found with ID: " + id);
        }
        coverageRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> getCoveragesByUser(Long userId) {
        logger.debug("Getting coverages for user ID: {}", userId);
        if (!userRepository.findById(userId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return coverageRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> getCoveragesByCompany(Long companyId) {
        logger.debug("Getting coverages for company ID: {}", companyId);
        if (!companyRepository.findById(companyId).isPresent()) {
            throw new ResourceNotFoundException("Company not found with ID: " + companyId);
        }
        return coverageRepository.findByCompanyId(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> getCoveragesByPlant(Long plantId) {
        logger.debug("Getting coverages for plant ID: {}", plantId);
        if (!plantRepository.findById(plantId).isPresent()) {
            throw new ResourceNotFoundException("Plant not found with ID: " + plantId);
        }
        return coverageRepository.findByPlantId(plantId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasCompanyAccess(Long userId, Long companyId) {
        logger.debug("Checking if user ID: {} has access to company ID: {}", userId, companyId);
        List<Coverage> coverages = coverageRepository.findByUserIdAndCompanyId(userId, companyId);
        return coverages.stream().anyMatch(Coverage::isActive);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPlantAccess(Long userId, Long plantId) {
        logger.debug("Checking if user ID: {} has access to plant ID: {}", userId, plantId);
        List<Coverage> coverages = coverageRepository.findByPlantId(plantId);
        return coverages.stream()
                .filter(c -> c.getUser().getId().equals(userId))
                .anyMatch(Coverage::isActive);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> getAllCoverages() {
        logger.debug("Getting all coverages");
        return coverageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> getActiveCoveragesByUser(Long userId) {
        logger.debug("Getting active coverages for user ID: {}", userId);
        if (!userRepository.findById(userId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return coverageRepository.findActiveByUserId(userId);
    }
}
