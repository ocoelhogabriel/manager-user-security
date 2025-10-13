package com.ocoelhogabriel.manager_user_security.domain.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Logger operations in the domain layer.
 */
public interface LoggerService {
    
    /**
     * Log a new entry
     *
     * @param logger The logger entry to create
     * @return The created logger entry with ID
     */
    Logger logEntry(Logger logger);
    
    /**
     * Find a logger entry by its ID
     *
     * @param id The logger ID
     * @return An Optional containing the logger if found
     */
    Optional<Logger> findById(Long id);
    
    /**
     * Find all logger entries
     *
     * @return A list of all logger entries
     */
    List<Logger> findAllLogs();
    
    /**
     * Find logger entries by type
     *
     * @param type The logger type
     * @return A list of logger entries with the specified type
     */
    List<Logger> findByType(LoggerType type);
    
    /**
     * Find logger entries by serial number
     *
     * @param serialNumber The serial number
     * @return A list of logger entries with the specified serial number
     */
    List<Logger> findBySerialNumber(String serialNumber);
    
    /**
     * Find logger entries by time range
     *
     * @param startTime The start time (inclusive)
     * @param endTime The end time (inclusive)
     * @return A list of logger entries within the specified time range
     */
    List<Logger> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Delete a logger entry by its ID
     *
     * @param id The logger ID
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if logger not found
     */
    void deleteLog(Long id);
    
    /**
     * Create a convenience method for logging with different log levels
     *
     * @param serialNumber The serial number
     * @param type The logger type
     * @param message The log message
     * @return The created logger entry with ID
     */
    Logger log(String serialNumber, LoggerType type, String message);
}
