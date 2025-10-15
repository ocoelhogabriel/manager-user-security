package com.ocoelhogabriel.manager_user_security.interfaces.controllers;

import com.ocoelhogabriel.manager_user_security.domain.entity.Logger;
import com.ocoelhogabriel.manager_user_security.domain.service.LoggerService;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.LoggerType;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.LoggerRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.LoggerResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.LoggerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/logs/v1")
@Tag(name = "Logs", description = "API for log management")
@SecurityRequirement(name = "bearerAuth")
public class LoggerController {

    private final LoggerService loggerService;
    private final LoggerMapper loggerMapper;

    public LoggerController(LoggerService loggerService, LoggerMapper loggerMapper) {
        this.loggerService = loggerService;
        this.loggerMapper = loggerMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    public ResponseEntity<LoggerResponse> createLog(@Valid @RequestBody LoggerRequest request) {
        Logger log = loggerMapper.toDomain(request);
        Logger createdLog = loggerService.logEntry(log);
        LoggerResponse response = loggerMapper.toResponse(createdLog);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<LoggerResponse> getLogById(@PathVariable Long id) {
        Optional<Logger> logOpt = loggerService.findById(id);
        if (logOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        LoggerResponse response = loggerMapper.toResponse(logOpt.get());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/serial/{serialNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get log entries by serial number",
        description = "Returns a list of log entries for a specific serial number",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of log entries",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<LoggerResponse>> getLogsBySerialNumber(@PathVariable String serialNumber) {
        List<Logger> logs = loggerService.findBySerialNumber(serialNumber);
        List<LoggerResponse> responses = loggerMapper.toResponseList(logs);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get log entries by type",
        description = "Returns a list of log entries for a specific type",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of log entries",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<LoggerResponse>> getLogsByType(@PathVariable LoggerType type) {
        List<Logger> logs = loggerService.findByType(type);
        List<LoggerResponse> responses = loggerMapper.toResponseList(logs);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/latest/{limit}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get latest log entries",
        description = "Returns a list of the latest log entries, limited by the provided count",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of latest log entries",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<LoggerResponse>> getLatestLogs(@PathVariable int limit) {
        List<Logger> logs = loggerService.findAllLogs().stream().limit(limit).toList();
        List<LoggerResponse> responses = loggerMapper.toResponseList(logs);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Search log entries by timestamp range",
        description = "Returns a list of log entries within a specified timestamp range",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of log entries",
                    content = @Content(schema = @Schema(implementation = LoggerResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<LoggerResponse>> searchLogsByTimestamp(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        List<Logger> logs = loggerService.findByTimeRange(startTime, endTime);
        List<LoggerResponse> responses = loggerMapper.toResponseList(logs);
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
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        loggerService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}
