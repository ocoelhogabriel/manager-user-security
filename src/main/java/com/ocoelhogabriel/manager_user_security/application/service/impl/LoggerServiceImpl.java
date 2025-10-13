package com.ocoelhogabriel.manager_user_security.application.service.impl;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.LoggerRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.LoggerService;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of LoggerService that handles business logic for logging operations.
 */
@Service
public class LoggerServiceImpl implements LoggerService {

    private final LoggerRepository loggerRepository;

    @Autowired
    public LoggerServiceImpl(LoggerRepository loggerRepository) {
        this.loggerRepository = loggerRepository;
    }

    @Override
    @Transactional
    public Logger createLogEntry(Logger logger) {
        if (!logger.isValid()) {
            throw new DomainException("Invalid logger data");
        }
        
        return loggerRepository.save(logger);
    }

    @Override
    @Transactional
    public Logger createLogEntry(String serialNumber, LoggerType type, String message) {
        Logger logger = new Logger.Builder()
                .withTimestamp(LocalDateTime.now())
                .withSerialNumber(serialNumber)
                .withType(type)
                .withMessage(message)
                .build();
                
        return loggerRepository.save(logger);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Logger> findById(Long id) {
        return loggerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Logger> findBySerialNumber(String serialNumber) {
        return loggerRepository.findBySerialNumber(serialNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Logger> findByType(LoggerType type) {
        return loggerRepository.findByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Logger> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return loggerRepository.findByTimestampBetween(startTime, endTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Logger> findAll() {
        return loggerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Logger> findLatestLogs(int limit) {
        return loggerRepository.findLatestLogs(limit);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!loggerRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Log entry not found with ID: " + id);
        }
        loggerRepository.deleteById(id);
    }
}
