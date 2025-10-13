package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository;

import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.CoverageEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.UserEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.CompanyEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PlantEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for Coverage entities.
 */
@Repository
public interface CoverageJpaRepository extends JpaRepository<CoverageEntity, Long> {
    
    /**
     * Find all coverage entities by user ID.
     *
     * @param userId The user ID
     * @return List of coverage entities for the user
     */
    List<CoverageEntity> findByUserId(Long userId);
    
    /**
     * Find all coverage entities by company ID.
     *
     * @param companyId The company ID
     * @return List of coverage entities for the company
     */
    List<CoverageEntity> findByCompanyId(Long companyId);
    
    /**
     * Find all coverage entities by plant ID.
     *
     * @param plantId The plant ID
     * @return List of coverage entities for the plant
     */
    List<CoverageEntity> findByPlantId(Long plantId);
    
    /**
     * Find all coverage entities by user ID and company ID.
     *
     * @param userId The user ID
     * @param companyId The company ID
     * @return List of coverage entities for both user and company
     */
    List<CoverageEntity> findByUserIdAndCompanyId(Long userId, Long companyId);
    
    /**
     * Find all active coverage entities by user ID.
     *
     * @param userId The user ID
     * @param active The active status (true for active coverages)
     * @return List of active coverage entities for the user
     */
    List<CoverageEntity> findByUserIdAndActive(Long userId, boolean active);
    
    /**
     * Check if a user has access to a company.
     *
     * @param userId The user ID
     * @param companyId The company ID
     * @return True if at least one coverage exists, false otherwise
     */
    @Query("SELECT COUNT(c) > 0 FROM CoverageEntity c WHERE c.user.id = :userId AND c.company.id = :companyId AND c.active = true")
    boolean existsByUserIdAndCompanyIdAndActiveTrue(@Param("userId") Long userId, @Param("companyId") Long companyId);
    
    /**
     * Check if a user has access to a plant.
     *
     * @param userId The user ID
     * @param plantId The plant ID
     * @return True if at least one coverage exists, false otherwise
     */
    @Query("SELECT COUNT(c) > 0 FROM CoverageEntity c WHERE c.user.id = :userId AND c.plant.id = :plantId AND c.active = true")
    boolean existsByUserIdAndPlantIdAndActiveTrue(@Param("userId") Long userId, @Param("plantId") Long plantId);
}
