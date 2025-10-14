package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Company;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.CompanyRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.CompanyResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.CompanyUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper for converting between Company domain entity and DTOs using MapStruct.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    Company toEntity(CompanyRequest request);

    Company toEntity(CompanyUpdateRequest request);

    CompanyResponse toResponse(Company entity);

    List<CompanyResponse> toResponseList(List<Company> entities);
}