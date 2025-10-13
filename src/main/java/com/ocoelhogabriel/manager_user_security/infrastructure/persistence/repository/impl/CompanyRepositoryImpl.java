package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.impl;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.repository.CompanyRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.CompanyEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.CompanyMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.CompanyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the CompanyRepository interface using JPA.
 */
@Repository
public class CompanyRepositoryImpl implements CompanyRepository {
    
    private final CompanyJpaRepository companyJpaRepository;
    private final CompanyMapper companyMapper;
    
    public CompanyRepositoryImpl(CompanyJpaRepository companyJpaRepository, CompanyMapper companyMapper) {
        this.companyJpaRepository = companyJpaRepository;
        this.companyMapper = companyMapper;
    }
    
    @Override
    public Company save(Company company) {
        CompanyEntity entity;
        
        if (company.getId() != null) {
            // Update existing company
            Optional<CompanyEntity> existingEntity = companyJpaRepository.findById(company.getId());
            if (existingEntity.isPresent()) {
                entity = companyMapper.updateEntity(existingEntity.get(), company);
            } else {
                entity = companyMapper.toEntity(company);
            }
        } else {
            // Create new company
            entity = companyMapper.toEntity(company);
        }
        
        CompanyEntity savedEntity = companyJpaRepository.save(entity);
        return companyMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Company> findById(Long id) {
        return companyJpaRepository.findById(id)
                .map(companyMapper::toDomain);
    }
    
    @Override
    public Optional<Company> findByCnpj(String cnpj) {
        return companyJpaRepository.findByCnpj(cnpj)
                .map(companyMapper::toDomain);
    }
    
    @Override
    public List<Company> findAll() {
        return companyJpaRepository.findAll().stream()
                .map(companyMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        companyJpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByCnpj(String cnpj) {
        return companyJpaRepository.existsByCnpj(cnpj);
    }
}
