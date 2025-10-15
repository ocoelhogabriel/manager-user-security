package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.exception.DuplicateResourceException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.CompanyRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of CompanyService that handles business logic for Company operations.
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public Company registerCompany(Company company) {
        if (!company.isValid()) {
            throw new DomainException("Invalid company data");
        }

        if (companyRepository.existsByCnpj(company.getCnpj())) {
            throw new DuplicateResourceException("Company with CNPJ " + company.getCnpj() + " already exists");
        }

        return companyRepository.save(company);
    }

    @Override
    @Transactional
    public Company updateCompany(Company company) {
        if (!company.isValid()) {
            throw new DomainException("Invalid company data");
        }

        if (company.getId() == null) {
            throw new DomainException("Company ID must be provided for update");
        }

        // Check if company exists
        companyRepository.findById(company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + company.getId()));

        // Check if new CNPJ would conflict with existing company
        Optional<Company> existingByCnpj = companyRepository.findByCnpj(company.getCnpj());
        if (existingByCnpj.isPresent() && !existingByCnpj.get().getId().equals(company.getId())) {
            throw new DuplicateResourceException("Company with CNPJ " + company.getCnpj() + " already exists");
        }

        return companyRepository.save(company);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findByCnpj(String cnpj) {
        return companyRepository.findByCnpj(cnpj);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteCompany(Long id) {
        // Use getCompanyById to ensure the company exists or throw ResourceNotFoundException
        getCompanyById(id);
        companyRepository.deleteById(id);
    }
}
