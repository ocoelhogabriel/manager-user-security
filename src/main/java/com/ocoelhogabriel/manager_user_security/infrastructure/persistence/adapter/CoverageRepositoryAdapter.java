package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.adapter;

import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;
import com.ocoelhogabriel.manager_user_security.domain.repository.CoverageRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.CoverageJpaRepository;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.CoverageMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CoverageRepositoryAdapter implements CoverageRepository {

    private final CoverageJpaRepository coverageJpaRepository;
    private final CoverageMapper coverageMapper;

    public CoverageRepositoryAdapter(CoverageJpaRepository coverageJpaRepository, CoverageMapper coverageMapper) {
        this.coverageJpaRepository = coverageJpaRepository;
        this.coverageMapper = coverageMapper;
    }

    @Override
    public Coverage save(Coverage coverage) {
        var entity = coverageMapper.toPersistenceEntity(coverage);
        var savedEntity = coverageJpaRepository.save(entity);
        return coverageMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Coverage> findById(Long id) {
        return coverageJpaRepository.findById(id).map(coverageMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        coverageJpaRepository.deleteById(id);
    }

    @Override
    public List<Coverage> findByUserId(Long userId) {
        return coverageJpaRepository.findByUserId(userId).stream()
                .map(coverageMapper::toDomain)
                .toList();
    }

    @Override
    public List<Coverage> findByCompanyId(Long companyId) {
        return coverageJpaRepository.findByCompanyId(companyId).stream()
                .map(coverageMapper::toDomain)
                .toList();
    }

    @Override
    public List<Coverage> findByPlantId(Long plantId) {
        return coverageJpaRepository.findByPlantId(plantId).stream()
                .map(coverageMapper::toDomain)
                .toList();
    }

    @Override
    public List<Coverage> findByUserIdAndCompanyId(Long userId, Long companyId) {
        return coverageJpaRepository.findByUserIdAndCompanyId(userId, companyId).stream()
                .map(coverageMapper::toDomain)
                .toList();
    }

    @Override
    public List<Coverage> findActiveByUserId(Long userId) {
        return coverageJpaRepository.findByUserIdAndActive(userId, true).stream()
                .map(coverageMapper::toDomain)
                .toList();
    }

    @Override
    public List<Coverage> findAll() {
        return coverageJpaRepository.findAll().stream()
                .map(coverageMapper::toDomain)
                .toList();
    }
}
