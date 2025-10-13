package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.domain.model.Perfil;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Name;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.NivelAcesso;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.PerfilId;
import com.ocoelhogabriel.manager_user_security.domain.ports.in.PerfilUseCase;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.PerfilRepository;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Application Service implementing the PerfilUseCase input port.
 * This class orchestrates the business logic for profile management.
 */
public class PerfilService implements PerfilUseCase {

    private final PerfilRepository perfilRepository;

    public PerfilService(final PerfilRepository perfilRepository) {
        this.perfilRepository = Objects.requireNonNull(perfilRepository);
    }

    @Override
    public Perfil createPerfil(CreatePerfilCommand command) {
        final Name nome = new Name(command.nome());
        if (perfilRepository.existsByNome(nome)) {
            throw new PerfilAlreadyExistsException("A profile with the name '" + nome.value() + "' already exists.");
        }

        final Perfil newPerfil = Perfil.builder()
            .nome(nome)
            .descricao(command.descricao())
            .nivelAcesso(new NivelAcesso(command.nivelAcesso()))
            .active(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        return perfilRepository.save(newPerfil);
    }

    @Override
    public Perfil findById(PerfilId id) {
        return perfilRepository.findById(id)
            .orElseThrow(() -> new PerfilNotFoundException("Profile not found with ID: " + id.value()));
    }

    @Override
    public PagedResult<Perfil> findAllActive(PageQuery query) {
        return perfilRepository.findAllActive(query);
    }

    @Override
    public Perfil updatePerfil(UpdatePerfilCommand command) {
        final Perfil existingPerfil = findById(command.id());
        final Name newName = new Name(command.nome());

        // Check if the new name is being changed to another existing profile's name
        if (!existingPerfil.nome().equals(newName) && perfilRepository.existsByNome(newName)) {
            throw new PerfilAlreadyExistsException("A profile with the name '" + newName.value() + "' already exists.");
        }

        final Perfil updatedPerfil = existingPerfil.updateInfo(newName, command.descricao());
        return perfilRepository.save(updatedPerfil);
    }

    @Override
    public Perfil deactivatePerfil(PerfilId id) {
        final Perfil perfil = findById(id);
        final Perfil deactivatedPerfil = perfil.deactivate();
        return perfilRepository.save(deactivatedPerfil);
    }

    @Override
    public Perfil activatePerfil(PerfilId id) {
        final Perfil perfil = findById(id);
        final Perfil activatedPerfil = perfil.activate();
        return perfilRepository.save(activatedPerfil);
    }
}
