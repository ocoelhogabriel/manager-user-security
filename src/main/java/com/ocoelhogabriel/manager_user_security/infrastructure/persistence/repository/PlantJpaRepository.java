package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository;

import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PlantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for PlantEntity.
 */
@Repository
public interface PlantJpaRepository extends JpaRepository<PlantEntity, Long> {
    
    /**
     * Find all plants by company ID
     * 
     * @param companyId The company ID to search for
     * @return A list of plants for the specified company
     */
    List<PlantEntity> findByCompanyId(Long companyId);
    
    /**
     * Check if a plant exists with the given name and company ID
     * 
     * @param name The plant name
     * @param companyId The company ID
     * @return true if a plant with this name exists for the company, false otherwise
     */
    boolean existsByNameAndCompanyId(String name, Long companyId);
}
