package com.ocoelhogabriel.manager_user_security.domain.ports.in;

import com.ocoelhogabriel.manager_user_security.domain.model.Perfil;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.PerfilId;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;

/**
 * Input Port defining the use cases for profile (Perfil) management.
 */
public interface PerfilUseCase {

    // --- Commands for mutations ---

    record CreatePerfilCommand(String nome, String descricao, Integer nivelAcesso) {}

    record UpdatePerfilCommand(PerfilId id, String nome, String descricao) {}

    // --- Use Case Methods ---

    /**
     * Creates a new profile.
     * @param command The command containing profile data.
     * @return The newly created Perfil entity.
     * @throws PerfilAlreadyExistsException if a profile with the same name already exists.
     */
    Perfil createPerfil(CreatePerfilCommand command);

    /**
     * Finds a profile by its ID.
     * @param id The profile's ID.
     * @return The Perfil entity.
     * @throws PerfilNotFoundException if no profile is found with the given ID.
     */
    Perfil findById(PerfilId id);

    /**
     * Finds all active profiles with pagination.
     * @param query The pagination query.
     * @return A paginated result of active profiles.
     */
    PagedResult<Perfil> findAllActive(PageQuery query);

    /**
     * Updates a profile's information.
     * @param command The command containing the profile ID and new data.
     * @return The updated Perfil entity.
     * @throws PerfilNotFoundException if the profile to update is not found.
     */
    Perfil updatePerfil(UpdatePerfilCommand command);

    /**
     * Deactivates a profile.
     * @param id The ID of the profile to deactivate.
     * @return The deactivated Perfil entity.
     * @throws PerfilNotFoundException if the profile to deactivate is not found.
     */
    Perfil deactivatePerfil(PerfilId id);

    /**
     * Activates a profile.
     * @param id The ID of the profile to activate.
     * @return The activated Perfil entity.
     * @throws PerfilNotFoundException if the profile to activate is not found.
     */
    Perfil activatePerfil(PerfilId id);

    // --- Custom Domain Exceptions ---

    class PerfilNotFoundException extends RuntimeException {
        public PerfilNotFoundException(String message) {
            super(message);
        }
    }

    class PerfilAlreadyExistsException extends RuntimeException {
        public PerfilAlreadyExistsException(String message) {
            super(message);
        }
    }
}
