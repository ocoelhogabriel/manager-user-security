package com.ocoelhogabriel.manager_user_security.infrastructure.web;

import com.ocoelhogabriel.manager_user_security.domain.model.Perfil;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.PerfilId;
import com.ocoelhogabriel.manager_user_security.domain.ports.in.PerfilUseCase;
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
 * Driving Adapter (Controller) for profile (Perfil) management.
 */
@RestController
@RequestMapping("/api/v1/perfis")
@Tag(name = "Perfis", description = "API for Profile Management")
public class PerfilController {

    private final PerfilUseCase perfilUseCase;

    public PerfilController(final PerfilUseCase perfilUseCase) {
        this.perfilUseCase = Objects.requireNonNull(perfilUseCase);
    }

    // --- DTOs for the API Layer ---
    public record CreatePerfilRequest(String nome, String descricao, Integer nivelAcesso) {}
    public record UpdatePerfilRequest(String nome, String descricao) {}
    public record PerfilResponse(Long id, String nome, String descricao, Integer nivelAcesso, boolean active, LocalDateTime createdAt) {}
    public record ApiPagedResult<T>(List<T> items, long total) {}

    @PostMapping
    @Operation(summary = "Create a new profile")
    public ResponseEntity<PerfilResponse> createPerfil(@RequestBody final CreatePerfilRequest request) {
        final var command = PerfilApiMapper.toCreateCommand(request);
        final Perfil createdPerfil = perfilUseCase.createPerfil(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(PerfilApiMapper.toResponse(createdPerfil));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a profile by its ID")
    public ResponseEntity<PerfilResponse> findPerfilById(@PathVariable final Long id) {
        final Perfil perfil = perfilUseCase.findById(new PerfilId(id));
        return ResponseEntity.ok(PerfilApiMapper.toResponse(perfil));
    }

    @GetMapping
    @Operation(summary = "Find all active profiles with pagination")
    public ResponseEntity<ApiPagedResult<PerfilResponse>> findPerfis(
        @RequestParam(defaultValue = "0") final int page,
        @RequestParam(defaultValue = "10") final int size) {
        final PagedResult<Perfil> pagedResult = perfilUseCase.findAllActive(new PageQuery(page, size));
        return ResponseEntity.ok(PerfilApiMapper.toPagedResponse(pagedResult));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a profile's information")
    public ResponseEntity<PerfilResponse> updatePerfil(@PathVariable final Long id, @RequestBody final UpdatePerfilRequest request) {
        final var command = PerfilApiMapper.toUpdateCommand(new PerfilId(id), request);
        final Perfil updatedPerfil = perfilUseCase.updatePerfil(command);
        return ResponseEntity.ok(PerfilApiMapper.toResponse(updatedPerfil));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate a profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivatePerfil(@PathVariable final Long id) {
        perfilUseCase.deactivatePerfil(new PerfilId(id));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate a profile")
    public ResponseEntity<PerfilResponse> activatePerfil(@PathVariable final Long id) {
        final Perfil activatedPerfil = perfilUseCase.activatePerfil(new PerfilId(id));
        return ResponseEntity.ok(PerfilApiMapper.toResponse(activatedPerfil));
    }

    private static class PerfilApiMapper {
        static PerfilUseCase.CreatePerfilCommand toCreateCommand(final CreatePerfilRequest request) {
            return new PerfilUseCase.CreatePerfilCommand(request.nome(), request.descricao(), request.nivelAcesso());
        }

        static PerfilUseCase.UpdatePerfilCommand toUpdateCommand(final PerfilId id, final UpdatePerfilRequest request) {
            return new PerfilUseCase.UpdatePerfilCommand(id, request.nome(), request.descricao());
        }

        static PerfilResponse toResponse(final Perfil perfil) {
            return new PerfilResponse(
                perfil.id().value(),
                perfil.nome().value(),
                perfil.descricao(),
                perfil.nivelAcesso().value(),
                perfil.isActive(),
                perfil.createdAt()
            );
        }

        static ApiPagedResult<PerfilResponse> toPagedResponse(final PagedResult<Perfil> pagedResult) {
            final List<PerfilResponse> responses = pagedResult.items().stream()
                .map(PerfilApiMapper::toResponse)
                .collect(Collectors.toList());
            return new ApiPagedResult<>(responses, pagedResult.total());
        }
    }
}
