package com.ocoelhogabriel.manager_user_security.interfaces.controllers;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.service.CompanyService;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.CompanyRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.CompanyResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.CompanyUpdateRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.CompanyMapper;
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

import java.util.List;

/**
 * REST controller for company operations.
 */
@RestController
@RequestMapping("/api/companies/v1")
@Tag(name = "Companies", description = "API for company management")
@SecurityRequirement(name = "bearerAuth")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create a new company",
        description = "Creates a new company with the provided details",
        responses = {
            @ApiResponse(responseCode = "201", description = "Company created successfully",
                    content = @Content(schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Company with same CNPJ already exists"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest request) {
        Company company = companyMapper.toDomain(request);
        Company createdCompany = companyService.registerCompany(company);
        CompanyResponse response = companyMapper.toResponse(createdCompany);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update an existing company",
        description = "Updates an existing company with the provided details",
        responses = {
            @ApiResponse(responseCode = "200", description = "Company updated successfully",
                    content = @Content(schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "409", description = "Company with same CNPJ already exists"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<CompanyResponse> updateCompany(@Valid @RequestBody CompanyUpdateRequest request) {
        Company company = companyMapper.toDomain(request);
        Company updatedCompany = companyService.updateCompany(company);
        CompanyResponse response = companyMapper.toResponse(updatedCompany);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
        summary = "Get a company by ID",
        description = "Returns a company based on the provided ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Company found",
                    content = @Content(schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        CompanyResponse response = companyMapper.toResponse(company);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cnpj/{cnpj}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
        summary = "Get a company by CNPJ",
        description = "Returns a company based on the provided CNPJ",
        responses = {
            @ApiResponse(responseCode = "200", description = "Company found",
                    content = @Content(schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<CompanyResponse> getCompanyByCnpj(@PathVariable String cnpj) {
        Company company = companyService.findByCnpj(cnpj)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with CNPJ: " + cnpj));
        CompanyResponse response = companyMapper.toResponse(company);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
        summary = "Get all companies",
        description = "Returns a list of all companies",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of companies",
                    content = @Content(schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<Company> companies = companyService.findAllCompanies();
        List<CompanyResponse> responses = companyMapper.toResponseList(companies);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete a company",
        description = "Deletes a company based on the provided ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Company deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
