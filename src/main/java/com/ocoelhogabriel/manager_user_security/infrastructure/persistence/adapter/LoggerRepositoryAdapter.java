package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.adapter;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.domain.repository.LoggerRepository;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.LoggerJpaRepository;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.LoggerMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class LoggerRepositoryAdapter implements LoggerRepository {

    private final LoggerJpaRepository loggerJpaRepository;
    private final LoggerMapper loggerMapper;

    public LoggerRepositoryAdapter(LoggerJpaRepository loggerJpaRepository, LoggerMapper loggerMapper) {
        this.loggerJpaRepository = loggerJpaRepository;
        this.loggerMapper = loggerMapper;
    }

    @Override
    public Logger save(Logger logger) {
        var entity = loggerMapper.toPersistenceEntity(logger);
        var savedEntity = loggerJpaRepository.save(entity);
        return loggerMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Logger> findById(Long id) {
        return loggerJpaRepository.findById(id).map(loggerMapper::toDomain);
    }

    @Override
    public List<Logger> findBySerialNumber(String serialNumber) {
        return loggerJpaRepository.findBySerialNumber(serialNumber).stream()
                .map(loggerMapper::toDomain)
                .toList();
    }

    @Override
    public List<Logger> findByType(LoggerType type) {
        return loggerJpaRepository.findByType(type).stream()
                .map(loggerMapper::toDomain)
                .toList();
    }

    @Override
    public List<Logger> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return loggerJpaRepository.findByTimestampBetween(startTime, endTime).stream()
                .map(loggerMapper::toDomain)
                .toList();
    }

    @Override
    public List<Logger> findAll() {
        return loggerJpaRepository.findAll().stream()
                .map(loggerMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        loggerJpaRepository.deleteById(id);
    }

    @Override
    public List<Logger> findLatestLogs(int limit) {
        return loggerJpaRepository.findAllByOrderByTimestampDesc(PageRequest.of(0, limit)).stream()
                .map(loggerMapper::toDomain)
                .toList();
    }
}
