package com.ocoelhogabriel.manager_user_security.application.usecase;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.exception.DuplicateResourceException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Use case for company operations. This class orchestrates the application logic
 * and delegates to domain services.
 */
@Component
public class CompanyUseCase {

    private final CompanyService companyService;

    @Autowired
    public CompanyUseCase(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Creates a new company.
     *
     * @param company The company to create
     * @return The created company with ID
     * @throws DuplicateResourceException if company with same CNPJ already exists
     * @throws DomainException if company data is invalid
     */
    public Company createCompany(Company company) {
        return companyService.registerCompany(company);
    }

    /**
     * Updates an existing company.
     *
     * @param company The company to update
     * @return The updated company
     * @throws ResourceNotFoundException if company not found
     * @throws DuplicateResourceException if new CNPJ conflicts with an existing company
     * @throws DomainException if company data is invalid
     */
    public Company updateCompany(Company company) {
        return companyService.updateCompany(company);
    }

    /**
     * Gets a company by its ID.
     *
     * @param id The company ID
     * @return The company
     * @throws ResourceNotFoundException if company not found
     */
    public Company getCompanyById(Long id) {
        return companyService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
    }

    /**
     * Gets a company by its CNPJ.
     *
     * @param cnpj The company CNPJ
     * @return The company
     * @throws ResourceNotFoundException if company not found
     */
    public Company getCompanyByCnpj(String cnpj) {
        return companyService.findByCnpj(cnpj)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with CNPJ: " + cnpj));
    }

    /**
     * Gets all companies.
     *
     * @return List of all companies
     */
    public List<Company> getAllCompanies() {
        return companyService.findAllCompanies();
    }

    /**
     * Deletes a company by its ID.
     *
     * @param id The company ID
     * @throws ResourceNotFoundException if company not found
     */
    public void deleteCompany(Long id) {
        companyService.deleteCompany(id);
    }
}
