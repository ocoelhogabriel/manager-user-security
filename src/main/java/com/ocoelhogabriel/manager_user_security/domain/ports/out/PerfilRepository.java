package com.ocoelhogabriel.manager_user_security.domain.ports.out;

import com.ocoelhogabriel.manager_user_security.domain.model.Perfil;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Name;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.PerfilId;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PageQuery;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository.PagedResult;

import java.util.Optional;

/**
 * Repository interface (Driven Port) for the Perfil entity.
 */
public interface PerfilRepository {

    /**
     * Saves (creates or updates) a profile.
     * @param perfil The profile entity to save.
     * @return The saved profile entity.
     */
    Perfil save(Perfil perfil);

    /**
     * Finds a profile by its unique ID.
     * @param id The profile ID.
     * @return An Optional containing the profile if found, otherwise empty.
     */
    Optional<Perfil> findById(PerfilId id);

    /**
     * Finds all active profiles with pagination.
     * @param query The pagination query.
     * @return A paginated result of active profiles.
     */
    PagedResult<Perfil> findAllActive(PageQuery query);

    /**
     * Checks if a profile exists with the given name.
     * @param nome The name to check.
     * @return true if a profile with the name exists, false otherwise.
     */
    boolean existsByNome(Name nome);
}
