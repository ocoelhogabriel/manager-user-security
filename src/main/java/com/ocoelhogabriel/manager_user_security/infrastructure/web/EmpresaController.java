package com.ocoelhogabriel.manager_user_security.infrastructure.web;

import com.ocoelhogabriel.manager_user_security.domain.model.Empresa;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.EmpresaId;
import com.ocoelhogabriel.manager_user_security.domain.ports.in.EmpresaUseCase;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Driving Adapter (Controller) for company (Empresa) management.
 */
@RestController
@RequestMapping("/api/v1/empresas")
@Tag(name = "Empresas", description = "API for Company Management")
public class EmpresaController {

    private final EmpresaUseCase empresaUseCase;

    public EmpresaController(final EmpresaUseCase empresaUseCase) {
        this.empresaUseCase = Objects.requireNonNull(empresaUseCase);
    }

    // --- DTOs for the API Layer ---
    public record CreateEmpresaRequest(String cnpj, String name, String fantasyName, String phone) {}
    public record UpdateEmpresaRequest(String name, String fantasyName, String phone) {}
    public record EmpresaResponse(Long id, String cnpj, String name, String fantasyName, String phone, boolean active, LocalDateTime createdAt) {}
    public record ApiPagedResult<T>(List<T> items, long total) {}

    @PostMapping
    @Operation(summary = "Create a new company")
    public ResponseEntity<EmpresaResponse> createEmpresa(@RequestBody final CreateEmpresaRequest request) {
        final var command = EmpresaApiMapper.toCreateCommand(request);
        final Empresa createdEmpresa = empresaUseCase.createEmpresa(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmpresaApiMapper.toResponse(createdEmpresa));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a company by its ID")
    public ResponseEntity<EmpresaResponse> findEmpresaById(@PathVariable final Long id) {
        final Empresa empresa = empresaUseCase.findById(new EmpresaId(id));
        return ResponseEntity.ok(EmpresaApiMapper.toResponse(empresa));
    }

    @GetMapping
    @Operation(summary = "Find all companies with pagination")
    public ResponseEntity<ApiPagedResult<EmpresaResponse>> findEmpresas(
        @RequestParam(defaultValue = "0") final int page,
        @RequestParam(defaultValue = "10") final int size) {
        final PagedResult<Empresa> pagedResult = empresaUseCase.findAll(new PageQuery(page, size));
        return ResponseEntity.ok(EmpresaApiMapper.toPagedResponse(pagedResult));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a company's information")
    public ResponseEntity<EmpresaResponse> updateEmpresa(@PathVariable final Long id, @RequestBody final UpdateEmpresaRequest request) {
        final var command = EmpresaApiMapper.toUpdateCommand(new EmpresaId(id), request);
        final Empresa updatedEmpresa = empresaUseCase.updateEmpresa(command);
        return ResponseEntity.ok(EmpresaApiMapper.toResponse(updatedEmpresa));
    }

    private static class EmpresaApiMapper {
        static EmpresaUseCase.CreateEmpresaCommand toCreateCommand(final CreateEmpresaRequest request) {
            return new EmpresaUseCase.CreateEmpresaCommand(request.cnpj(), request.name(), request.fantasyName(), request.phone());
        }

        static EmpresaUseCase.UpdateEmpresaCommand toUpdateCommand(final EmpresaId id, final UpdateEmpresaRequest request) {
            return new EmpresaUseCase.UpdateEmpresaCommand(id, request.name(), request.fantasyName(), request.phone());
        }

        static EmpresaResponse toResponse(final Empresa empresa) {
            return new EmpresaResponse(
                empresa.id().value(),
                empresa.cnpj().value(),
                empresa.name().value(),
                empresa.fantasyName(),
                empresa.phone(),
                empresa.isActive(),
                empresa.createdAt()
            );
        }

        static ApiPagedResult<EmpresaResponse> toPagedResponse(final PagedResult<Empresa> pagedResult) {
            final List<EmpresaResponse> responses = pagedResult.items().stream()
                .map(EmpresaApiMapper::toResponse)
                .collect(Collectors.toList());
            return new ApiPagedResult<>(responses, pagedResult.total());
        }
    }
}
