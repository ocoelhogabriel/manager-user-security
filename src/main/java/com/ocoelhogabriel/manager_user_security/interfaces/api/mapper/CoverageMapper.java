package com.ocoelhogabriel.manager_user_security.interfaces.api.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.CoverageRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.CoverageResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.CoverageUpdateRequest;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Coverage domain entities and API DTOs.
 */
@Component
public class CoverageMapper {

    /**
     * Convert a Coverage domain entity to a CoverageResponse DTO.
     *
     * @param coverage The domain entity
     * @return The response DTO
     */
    public CoverageResponse toResponse(Coverage coverage) {
        if (coverage == null) {
            return null;
        }
        
        CoverageResponse.Builder builder = CoverageResponse.builder()
                .id(coverage.getId())
                .active(coverage.isActive())
                .description(coverage.getDescription());
                
        if (coverage.getUser() != null) {
            builder.userId(coverage.getUser().getId())
                   .username(coverage.getUser().getUsername());
        }
        
        if (coverage.getCompany() != null) {
            builder.companyId(coverage.getCompany().getId())
                   .companyName(coverage.getCompany().getName());
        }
        
        if (coverage.getPlant() != null) {
            builder.plantId(coverage.getPlant().getId())
                   .plantName(coverage.getPlant().getName());
        }
        
        // Add timestamps if available (these would come from the JPA entity)
        if (coverage instanceof CoverageWithTimestamps) {
            CoverageWithTimestamps coverageWithTimestamps = (CoverageWithTimestamps) coverage;
            builder.createdAt(coverageWithTimestamps.getCreatedAt())
                   .updatedAt(coverageWithTimestamps.getUpdatedAt());
        }
        
        return builder.build();
    }
    
    /**
     * Convert a list of Coverage domain entities to a list of CoverageResponse DTOs.
     *
     * @param coverages The list of domain entities
     * @return The list of response DTOs
     */
    public List<CoverageResponse> toResponseList(List<Coverage> coverages) {
        if (coverages == null) {
            return List.of();
        }
        
        return coverages.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Create Coverage domain entity builder from a CoverageRequest DTO.
     * Note: This doesn't fully build the entity as it requires User, Company, and Plant objects,
     * which should be provided by the service/use case.
     *
     * @param request The request DTO
     * @return A pre-configured builder for the Coverage entity
     */
    public Coverage.Builder toDomainBuilder(CoverageRequest request) {
        if (request == null) {
            return null;
        }
        
        return Coverage.builder()
                .description(request.getDescription())
                .active(request.isActive());
    }
    
    /**
     * Create Coverage domain entity builder from a CoverageUpdateRequest DTO.
     * Note: This only sets the fields that are present in the update request.
     *
     * @param request The update request DTO
     * @return A partially configured builder for the Coverage entity
     */
    public Coverage.Builder toDomainUpdateBuilder(CoverageUpdateRequest request) {
        if (request == null) {
            return null;
        }
        
        Coverage.Builder builder = Coverage.builder();
        
        if (request.getDescription() != null) {
            builder.description(request.getDescription());
        }
        
        if (request.getActive() != null) {
            builder.active(request.getActive());
        }
        
        return builder;
    }
    
    /**
     * Interface to handle timestamps for Coverage entities.
     * This is used as a bridge since the domain entity doesn't have timestamps,
     * but we want to include them in the response.
     */
    public interface CoverageWithTimestamps {
        LocalDateTime getCreatedAt();
        LocalDateTime getUpdatedAt();
    }
}
