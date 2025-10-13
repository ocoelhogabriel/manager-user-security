package com.ocoelhogabriel.manager_user_security.domain.ports.out;

import com.ocoelhogabriel.manager_user_security.domain.model.Empresa;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.CNPJ;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.EmpresaId;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;

import java.util.Optional;

/**
 * Repository interface (Driven Port) for the Empresa entity.
 */
public interface EmpresaRepository {

    /**
     * Saves (creates or updates) a company.
     * @param empresa The company entity to save.
     * @return The saved company entity.
     */
    Empresa save(Empresa empresa);

    /**
     * Finds a company by its unique ID.
     * @param id The company ID.
     * @return An Optional containing the company if found, otherwise empty.
     */
    Optional<Empresa> findById(EmpresaId id);

    /**
     * Finds all companies with pagination.
     * @param query The pagination query.
     * @return A paginated result of companies.
     */
    PagedResult<Empresa> findAll(PageQuery query);

    /**
     * Checks if a company exists with the given CNPJ.
     * @param cnpj The CNPJ to check.
     * @return true if a company with the CNPJ exists, false otherwise.
     */
    boolean existsByCnpj(CNPJ cnpj);
}
