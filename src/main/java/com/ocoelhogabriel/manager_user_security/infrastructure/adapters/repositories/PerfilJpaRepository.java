package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Perfil entity.
 */
public interface PerfilJpaRepository extends JpaRepository<Perfil, Long> {

    /**
     * Checks if a profile with the given name exists.
     * @param nome The name to check.
     * @return true if a profile with the name exists, false otherwise.
     */
    boolean existsByNome(String nome);

    /**
     * Finds all profiles with the given active status, with pagination.
     * @param ativo The active status.
     * @param pageable The pagination information.
     * @return A paginated result of profiles.
     */
    Page<Perfil> findByAtivo(boolean ativo, Pageable pageable);
}
