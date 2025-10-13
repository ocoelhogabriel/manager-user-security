package com.ocoelhogabriel.manager_user_security.interfaces.api.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.CompanyRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.CompanyResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.CompanyUpdateRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Company domain entity and DTOs.
 */
@Component
public class CompanyMapper {
    
    /**
     * Converts a CompanyRequest DTO to a Company domain entity.
     *
     * @param request The company request DTO
     * @return The company domain entity
     */
    public Company toEntity(CompanyRequest request) {
        return new Company.Builder()
                .withCnpj(request.getCnpj())
                .withName(request.getName())
                .withTradingName(request.getTradingName())
                .withPhone(request.getPhone())
                .build();
    }
    
    /**
     * Converts a CompanyUpdateRequest DTO to a Company domain entity.
     *
     * @param request The company update request DTO
     * @return The company domain entity
     */
    public Company toEntity(CompanyUpdateRequest request) {
        return new Company.Builder()
                .withId(request.getId())
                .withCnpj(request.getCnpj())
                .withName(request.getName())
                .withTradingName(request.getTradingName())
                .withPhone(request.getPhone())
                .build();
    }
    
    /**
     * Converts a Company domain entity to a CompanyResponse DTO.
     *
     * @param entity The company domain entity
     * @return The company response DTO
     */
    public CompanyResponse toResponse(Company entity) {
        return new CompanyResponse(
                entity.getId(),
                entity.getCnpj(),
                entity.getName(),
                entity.getTradingName(),
                entity.getPhone()
        );
    }
    
    /**
     * Converts a list of Company domain entities to a list of CompanyResponse DTOs.
     *
     * @param entities The list of company domain entities
     * @return The list of company response DTOs
     */
    public List<CompanyResponse> toResponseList(List<Company> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
