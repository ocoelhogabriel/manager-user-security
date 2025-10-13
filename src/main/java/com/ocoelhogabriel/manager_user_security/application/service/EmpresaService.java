package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.domain.model.Empresa;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.CNPJ;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.EmpresaId;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Name;
import com.ocoelhogabriel.manager_user_security.domain.ports.in.EmpresaUseCase;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.EmpresaRepository;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Application Service implementing the EmpresaUseCase input port.
 * This class orchestrates the business logic for company management.
 */
public class EmpresaService implements EmpresaUseCase {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(final EmpresaRepository empresaRepository) {
        this.empresaRepository = Objects.requireNonNull(empresaRepository);
    }

    @Override
    public Empresa createEmpresa(CreateEmpresaCommand command) {
        final CNPJ cnpj = new CNPJ(command.cnpj());
        if (empresaRepository.existsByCnpj(cnpj)) {
            throw new EmpresaAlreadyExistsException("A company with the CNPJ '" + cnpj.value() + "' already exists.");
        }

        final Empresa newEmpresa = Empresa.builder()
            .cnpj(cnpj)
            .name(new Name(command.name()))
            .fantasyName(command.fantasyName())
            .phone(command.phone())
            .active(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        return empresaRepository.save(newEmpresa);
    }

    @Override
    public Empresa findById(EmpresaId id) {
        return empresaRepository.findById(id)
            .orElseThrow(() -> new EmpresaNotFoundException("Company not found with ID: " + id.value()));
    }

    @Override
    public PagedResult<Empresa> findAll(PageQuery query) {
        return empresaRepository.findAll(query);
    }

    @Override
    public Empresa updateEmpresa(UpdateEmpresaCommand command) {
        final Empresa existingEmpresa = findById(command.id());
        final Name newName = new Name(command.name());

        final Empresa updatedEmpresa = existingEmpresa.updateInfo(
            newName,
            command.fantasyName(),
            command.phone()
        );

        return empresaRepository.save(updatedEmpresa);
    }
}
