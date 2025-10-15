package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.impl;

import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;
import com.ocoelhogabriel.manager_user_security.domain.repository.CoverageRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.CoverageEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.CoverageJpaRepository;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.CoverageMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the CoverageRepository interface using Spring Data JPA.
 */
@Repository
public class CoverageRepositoryImpl implements CoverageRepository {

    private final CoverageJpaRepository coverageJpaRepository;
    private final CoverageMapper coverageMapper;

    @Autowired
    public CoverageRepositoryImpl(CoverageJpaRepository coverageJpaRepository, CoverageMapper coverageMapper) {
        this.coverageJpaRepository = coverageJpaRepository;
        this.coverageMapper = coverageMapper;
    }

    @Override
    @Transactional
    public Coverage save(Coverage coverage) {
        CoverageEntity coverageEntity = coverageMapper.toPersistenceEntity(coverage);
        coverageEntity = coverageJpaRepository.save(coverageEntity);
        return coverageMapper.toDomain(coverageEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Coverage> findById(Long id) {
        return coverageJpaRepository.findById(id)
                .map(coverageMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> findByUserId(Long userId) {
        return coverageMapper.toDomainList(coverageJpaRepository.findByUserId(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> findByCompanyId(Long companyId) {
        return coverageMapper.toDomainList(coverageJpaRepository.findByCompanyId(companyId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> findByPlantId(Long plantId) {
        return coverageMapper.toDomainList(coverageJpaRepository.findByPlantId(plantId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> findByUserIdAndCompanyId(Long userId, Long companyId) {
        return coverageMapper.toDomainList(coverageJpaRepository.findByUserIdAndCompanyId(userId, companyId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> findActiveByUserId(Long userId) {
        return coverageMapper.toDomainList(coverageJpaRepository.findByUserIdAndActive(userId, true));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        coverageJpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coverage> findAll() {
        return coverageMapper.toDomainList(coverageJpaRepository.findAll());
    }
}
