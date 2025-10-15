package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.impl;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.domain.repository.LoggerRepository;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.LoggerEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.LoggerJpaRepository;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.LoggerMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the LoggerRepository interface using JPA.
 */
@Repository
public class LoggerRepositoryImpl implements LoggerRepository {
    
    private final LoggerJpaRepository loggerJpaRepository;
    private final LoggerMapper loggerMapper;
    
    public LoggerRepositoryImpl(LoggerJpaRepository loggerJpaRepository, LoggerMapper loggerMapper) {
        this.loggerJpaRepository = loggerJpaRepository;
        this.loggerMapper = loggerMapper;
    }
    
    @Override
    public Logger save(Logger logger) {
        LoggerEntity entity = loggerMapper.toPersistenceEntity(logger);
        LoggerEntity savedEntity = loggerJpaRepository.save(entity);
        return loggerMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Logger> findById(Long id) {
        return loggerJpaRepository.findById(id)
                .map(loggerMapper::toDomain);
    }
    
    @Override
    public List<Logger> findAll() {
        return loggerJpaRepository.findAll().stream()
                .map(loggerMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Logger> findByType(LoggerType type) {
        return loggerJpaRepository.findByType(type).stream()
                .map(loggerMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Logger> findBySerialNumber(String serialNumber) {
        return loggerJpaRepository.findBySerialNumber(serialNumber).stream()
                .map(loggerMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Logger> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return loggerJpaRepository.findByTimestampBetween(startTime, endTime).stream()
                .map(loggerMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        loggerJpaRepository.deleteById(id);
    }
    
    @Override
    public List<Logger> findLatestLogs(int limit) {
        return loggerJpaRepository.findTop100ByOrderByTimestampDesc().stream()
                .limit(limit)
                .map(loggerMapper::toDomain)
                .collect(Collectors.toList());
    }
}
