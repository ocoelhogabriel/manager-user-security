package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository;

import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for CompanyEntity.
 */
@Repository
public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {
    
    /**
     * Find a company by CNPJ
     * 
     * @param cnpj The CNPJ to search for
     * @return An Optional containing the company if found
     */
    Optional<CompanyEntity> findByCnpj(String cnpj);
    
    /**
     * Check if a company exists with the given CNPJ
     * 
     * @param cnpj The CNPJ to check
     * @return true if a company with this CNPJ exists, false otherwise
     */
    boolean existsByCnpj(String cnpj);
}
