package com.ocoelhogabriel.manager_user_security.application.usecase;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.service.LoggerService;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case for logger operations. This class orchestrates the application logic
 * and delegates to domain services.
 */
@Component
public class LoggerUseCase {

    private final LoggerService loggerService;

    @Autowired
    public LoggerUseCase(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    /**
     * Creates a new log entry.
     *
     * @param logger The log entry to create
     * @return The created log entry with ID
     * @throws DomainException if log data is invalid
     */
    public Logger createLogEntry(Logger logger) {
        return loggerService.createLogEntry(logger);
    }

    /**
     * Creates a new log entry with the specified details.
     *
     * @param serialNumber The serial number
     * @param type The log type
     * @param message The log message
     * @return The created log entry with ID
     */
    public Logger createLogEntry(String serialNumber, LoggerType type, String message) {
        return loggerService.createLogEntry(serialNumber, type, message);
    }

    /**
     * Gets a log entry by its ID.
     *
     * @param id The log entry ID
     * @return The log entry
     * @throws ResourceNotFoundException if log entry not found
     */
    public Logger getLogEntryById(Long id) {
        return loggerService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Log entry not found with ID: " + id));
    }

    /**
     * Gets all log entries for a serial number.
     *
     * @param serialNumber The serial number
     * @return List of log entries for the serial number
     */
    public List<Logger> getLogEntriesBySerialNumber(String serialNumber) {
        return loggerService.findBySerialNumber(serialNumber);
    }

    /**
     * Gets all log entries of a specific type.
     *
     * @param type The log type
     * @return List of log entries of the specified type
     */
    public List<Logger> getLogEntriesByType(LoggerType type) {
        return loggerService.findByType(type);
    }

    /**
     * Gets all log entries within a time range.
     *
     * @param startTime The start time
     * @param endTime The end time
     * @return List of log entries within the specified time range
     */
    public List<Logger> getLogEntriesByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return loggerService.findByTimestampBetween(startTime, endTime);
    }

    /**
     * Gets all log entries.
     *
     * @return List of all log entries
     */
    public List<Logger> getAllLogEntries() {
        return loggerService.findAll();
    }

    /**
     * Gets the latest log entries.
     *
     * @param limit Maximum number of entries to return
     * @return List of the latest log entries
     */
    public List<Logger> getLatestLogEntries(int limit) {
        return loggerService.findLatestLogs(limit);
    }

    /**
     * Deletes a log entry by its ID.
     *
     * @param id The log entry ID
     * @throws ResourceNotFoundException if log entry not found
     */
    public void deleteLogEntry(Long id) {
        loggerService.deleteById(id);
    }
}
