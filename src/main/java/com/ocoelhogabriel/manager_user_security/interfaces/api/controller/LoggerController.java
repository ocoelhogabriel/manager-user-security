package com.ocoelhogabriel.manager_user_security.interfaces.api.controller;

import com.ocoelhogabriel.manager_user_security.application.usecase.LoggerUseCase;
import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.LoggerRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.LoggerResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.api.mapper.LoggerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for logger operations.
 */
@RestController
@RequestMapping("/api/logs/v1")
@Tag(name = "Logs", description = "API for system logging")
@SecurityRequirement(name = "bearerAuth")
public class LoggerController {

    private final LoggerUseCase loggerUseCase;
    private final LoggerMapper loggerMapper;

    @Autowired
    public LoggerController(LoggerUseCase loggerUseCase, LoggerMapper loggerMapper) {
        this.loggerUseCase = loggerUseCase;
        this.loggerMapper = loggerMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM')")
    @Operation(
        summary = "Create a new log entry",
        description = "Creates a new log entry with the provided details",
        responses = {
            @ApiResponse(responseCode = "201", description = "Log entry created successfully",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<LoggerResponse> createLogEntry(@Valid @RequestBody LoggerRequest request) {
        Logger logger = loggerMapper.toEntity(request);
        Logger createdLogger = loggerUseCase.createLogEntry(logger);
        LoggerResponse response = loggerMapper.toResponse(createdLogger);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    @Operation(
        summary = "Get a log entry by ID",
        description = "Returns a log entry based on the provided ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Log entry found",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "404", description = "Log entry not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<LoggerResponse> getLogEntry(@PathVariable Long id) {
        Logger logger = loggerUseCase.getLogEntryById(id);
        LoggerResponse response = loggerMapper.toResponse(logger);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/serial/{serialNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    @Operation(
        summary = "Get log entries by serial number",
        description = "Returns a list of log entries for the specified serial number",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of log entries",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<LoggerResponse>> getLogEntriesBySerialNumber(@PathVariable String serialNumber) {
        List<Logger> logEntries = loggerUseCase.getLogEntriesBySerialNumber(serialNumber);
        List<LoggerResponse> responses = loggerMapper.toResponseList(logEntries);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    @Operation(
        summary = "Get log entries by type",
        description = "Returns a list of log entries of the specified type",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of log entries",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<LoggerResponse>> getLogEntriesByType(@PathVariable LoggerType type) {
        List<Logger> logEntries = loggerUseCase.getLogEntriesByType(type);
        List<LoggerResponse> responses = loggerMapper.toResponseList(logEntries);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/range")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    @Operation(
        summary = "Get log entries by time range",
        description = "Returns a list of log entries within the specified time range",
        parameters = {
            @Parameter(name = "startTime", description = "Start time (ISO format)"),
            @Parameter(name = "endTime", description = "End time (ISO format)")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "List of log entries",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<LoggerResponse>> getLogEntriesByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Logger> logEntries = loggerUseCase.getLogEntriesByTimeRange(startTime, endTime);
        List<LoggerResponse> responses = loggerMapper.toResponseList(logEntries);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/latest")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    @Operation(
        summary = "Get latest log entries",
        description = "Returns the latest log entries, limited by the specified count",
        parameters = {
            @Parameter(name = "limit", description = "Maximum number of entries to return")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "List of log entries",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<LoggerResponse>> getLatestLogEntries(
            @RequestParam(defaultValue = "20") int limit) {
        List<Logger> logEntries = loggerUseCase.getLatestLogEntries(limit);
        List<LoggerResponse> responses = loggerMapper.toResponseList(logEntries);
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get all log entries",
        description = "Returns a list of all log entries (use with caution - may return large amounts of data)",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of log entries",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<LoggerResponse>> getAllLogEntries() {
        List<Logger> logEntries = loggerUseCase.getAllLogEntries();
        List<LoggerResponse> responses = loggerMapper.toResponseList(logEntries);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete a log entry",
        description = "Deletes a log entry based on the provided ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Log entry deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Log entry not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<Void> deleteLogEntry(@PathVariable Long id) {
        loggerUseCase.deleteLogEntry(id);
        return ResponseEntity.noContent().build();
    }
}
