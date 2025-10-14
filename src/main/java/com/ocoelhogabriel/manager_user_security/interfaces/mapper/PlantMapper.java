package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.domain.service.CompanyService;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.PlantRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.PlantResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.PlantUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Plant domain entity and DTOs.
 */
@Component
public class PlantMapper {
    
    private final CompanyService companyService;
    
    @Autowired
    public PlantMapper(CompanyService companyService) {
        this.companyService = companyService;
    }
    
    /**
     * Converts a PlantRequest DTO to a Plant domain entity.
     *
     * @param request The plant request DTO
     * @return The plant domain entity
     */
    public Plant toEntity(PlantRequest request) {
        return new Plant.Builder()
                .withCompanyId(request.getCompanyId())
                .withName(request.getName())
                .build();
    }
    
    /**
     * Converts a PlantUpdateRequest DTO to a Plant domain entity.
     *
     * @param request The plant update request DTO
     * @return The plant domain entity
     */
    public Plant toEntity(PlantUpdateRequest request) {
        return new Plant.Builder()
                .withId(request.getId())
                .withCompanyId(request.getCompanyId())
                .withName(request.getName())
                .build();
    }
    
    /**
     * Converts a Plant domain entity to a PlantResponse DTO.
     *
     * @param entity The plant domain entity
     * @return The plant response DTO
     */
    public PlantResponse toResponse(Plant entity) {
        PlantResponse response = new PlantResponse(
                entity.getId(),
                entity.getCompanyId(),
                entity.getName()
        );
        
        // Add company name if available
        Optional<Company> company = companyService.findById(entity.getCompanyId());
        company.ifPresent(c -> response.setCompanyName(c.getName()));
        
        return response;
    }
    
    /**
     * Converts a list of Plant domain entities to a list of PlantResponse DTOs.
     *
     * @param entities The list of plant domain entities
     * @return The list of plant response DTOs
     */
    public List<PlantResponse> toResponseList(List<Plant> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
