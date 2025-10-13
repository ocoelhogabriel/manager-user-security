package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository;

import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.LoggerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA repository for LoggerEntity.
 */
@Repository
public interface LoggerJpaRepository extends JpaRepository<LoggerEntity, Long> {
    
    /**
     * Find all logger entries by type
     * 
     * @param type The logger type to search for
     * @return A list of logger entries with the specified type
     */
    List<LoggerEntity> findByType(LoggerType type);
    
    /**
     * Find all logger entries by serial number
     * 
     * @param serialNumber The serial number to search for
     * @return A list of logger entries with the specified serial number
     */
    List<LoggerEntity> findBySerialNumber(String serialNumber);
    
    /**
     * Find all logger entries within a time range
     * 
     * @param startTime The start time (inclusive)
     * @param endTime The end time (inclusive)
     * @return A list of logger entries within the specified time range
     */
    List<LoggerEntity> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find latest log entries ordered by timestamp descending
     *
     * @return A list of the latest log entries
     */
    List<LoggerEntity> findTop100ByOrderByTimestampDesc();
}
