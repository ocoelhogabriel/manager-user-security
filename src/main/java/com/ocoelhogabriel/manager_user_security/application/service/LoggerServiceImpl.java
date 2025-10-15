package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.LoggerRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.LoggerService;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public Logger logEntry(Logger logger) {
        if (!logger.isValid()) {
            throw new DomainException("Invalid logger data");
        }
        
        return loggerRepository.save(logger);
    }

    @Override
    @Transactional
    public Logger log(String serialNumber, LoggerType type, String message) {
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
    public List<Logger> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return loggerRepository.findByTimestampBetween(startTime, endTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Logger> findAllLogs() {
        return loggerRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteLog(Long id) {
        if (!loggerRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Log entry not found with ID: " + id);
        }
        loggerRepository.deleteById(id);
    }
}
