package com.ocoelhogabriel.manager_user_security.domain.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Company operations in the domain layer.
 */
public interface CompanyService {
    
    /**
     * Register a new company
     *
     * @param company The company to register
     * @return The registered company with ID
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.DuplicateResourceException if company with same CNPJ already exists
     */
    Company registerCompany(Company company);
    
    /**
     * Update an existing company
     *
     * @param company The company to update
     * @return The updated company
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if company not found
     */
    Company updateCompany(Company company);
    
    /**
     * Find a company by its ID
     *
     * @param id The company ID
     * @return An Optional containing the company if found
     */
    Optional<Company> findById(Long id);
    
    /**
     * Get a company by its ID
     *
     * @param id The company ID
     * @return The company with the given ID
     * @throws com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException if company not found
     */
    Company getCompanyById(Long id);
    
    /**
     * Find a company by its CNPJ
     *
     * @param cnpj The company CNPJ
     * @return An Optional containing the company if found
     */
    Optional<Company> findByCnpj(String cnpj);
    
    /**
     * Find all companies
     *
     * @return A list of all companies
     */
    List<Company> findAllCompanies();
    
    /**
     * Delete a company by its ID
     *
     * @param id The company ID
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if company not found
     */
    void deleteCompany(Long id);
}
