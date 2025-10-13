package com.ocoelhogabriel.manager_user_security.domain.repository;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Logger domain entity.
 * This is part of the domain layer and doesn't depend on infrastructure.
 */
public interface LoggerRepository {
    
    /**
     * Save a log entry
     *
     * @param logger The logger entry to save
     * @return The saved logger with ID
     */
    Logger save(Logger logger);
    
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
    List<Logger> findAll();
    
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
    List<Logger> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Delete a logger entry by its ID
     *
     * @param id The logger ID
     */
    void deleteById(Long id);
    
    /**
     * Find latest logger entries limited by count
     *
     * @param limit Maximum number of entries to return
     * @return A list of the latest logger entries
     */
    List<Logger> findLatestLogs(int limit);
}
