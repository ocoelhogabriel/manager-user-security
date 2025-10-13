package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Company domain entity and CompanyEntity JPA entity.
 */
@Component
public class CompanyMapper {
    
    /**
     * Convert a JPA entity to a domain entity
     *
     * @param entity The JPA entity to convert
     * @return The domain entity
     */
    public Company toDomain(CompanyEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new Company.Builder()
                .withId(entity.getId())
                .withCnpj(entity.getCnpj())
                .withName(entity.getName())
                .withTradingName(entity.getTradingName())
                .withPhone(entity.getPhone())
                .build();
    }
    
    /**
     * Convert a domain entity to a JPA entity
     *
     * @param domain The domain entity to convert
     * @return The JPA entity
     */
    public CompanyEntity toEntity(Company domain) {
        if (domain == null) {
            return null;
        }
        
        return new CompanyEntity(
                domain.getId(),
                domain.getCnpj(),
                domain.getName(),
                domain.getTradingName(),
                domain.getPhone()
        );
    }
    
    /**
     * Update an existing JPA entity with values from a domain entity
     *
     * @param entity The JPA entity to update
     * @param domain The domain entity with new values
     * @return The updated JPA entity
     */
    public CompanyEntity updateEntity(CompanyEntity entity, Company domain) {
        if (entity == null || domain == null) {
            return entity;
        }
        
        entity.setCnpj(domain.getCnpj());
        entity.setName(domain.getName());
        entity.setTradingName(domain.getTradingName());
        entity.setPhone(domain.getPhone());
        
        return entity;
    }
}
