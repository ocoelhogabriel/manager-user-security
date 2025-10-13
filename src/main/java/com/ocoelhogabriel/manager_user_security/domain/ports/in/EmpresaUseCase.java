package com.ocoelhogabriel.manager_user_security.domain.ports.in;

import com.ocoelhogabriel.manager_user_security.domain.model.Empresa;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.EmpresaId;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;

/**
 * Input Port defining the use cases for company (Empresa) management.
 */
public interface EmpresaUseCase {

    // --- Commands for mutations ---

    record CreateEmpresaCommand(String cnpj, String name, String fantasyName, String phone) {}

    record UpdateEmpresaCommand(EmpresaId id, String name, String fantasyName, String phone) {}

    // --- Use Case Methods ---

    /**
     * Creates a new company.
     * @param command The command containing company data.
     * @return The newly created Empresa entity.
     * @throws EmpresaAlreadyExistsException if a company with the same CNPJ already exists.
     */
    Empresa createEmpresa(CreateEmpresaCommand command);

    /**
     * Finds a company by its ID.
     * @param id The company's ID.
     * @return The Empresa entity.
     * @throws EmpresaNotFoundException if no company is found with the given ID.
     */
    Empresa findById(EmpresaId id);

    /**
     * Finds all active companies with pagination.
     * @param query The pagination query.
     * @return A paginated result of active companies.
     */
    PagedResult<Empresa> findAll(PageQuery query);

    /**
     * Updates a company's information.
     * @param command The command containing the company ID and new data.
     * @return The updated Empresa entity.
     * @throws EmpresaNotFoundException if the company to update is not found.
     */
    Empresa updateEmpresa(UpdateEmpresaCommand command);

    // --- Custom Domain Exceptions ---

    class EmpresaNotFoundException extends RuntimeException {
        public EmpresaNotFoundException(String message) {
            super(message);
        }
    }

    class EmpresaAlreadyExistsException extends RuntimeException {
        public EmpresaAlreadyExistsException(String message) {
            super(message);
        }
    }
}
