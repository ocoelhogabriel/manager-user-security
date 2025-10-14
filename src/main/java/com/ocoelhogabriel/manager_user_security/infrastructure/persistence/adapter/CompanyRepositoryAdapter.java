package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.adapter;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.repository.CompanyRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.CompanyEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.CompanyMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.CompanyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;
    private final CompanyMapper companyMapper;

    @Autowired
    public CompanyRepositoryAdapter(CompanyJpaRepository companyJpaRepository, CompanyMapper companyMapper) {
        this.companyJpaRepository = companyJpaRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public Company save(Company company) {
        CompanyEntity companyEntity = companyMapper.toEntity(company);
        CompanyEntity savedEntity = companyJpaRepository.save(companyEntity);
        return companyMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyJpaRepository.findById(id).map(companyMapper::toDomain);
    }

    @Override
    public Optional<Company> findByCnpj(String cnpj) {
        return companyJpaRepository.findByCnpj(cnpj).map(companyMapper::toDomain);
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
