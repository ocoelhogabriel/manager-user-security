package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Empresa entity.
 */
public interface EmpresaJpaRepository extends JpaRepository<Empresa, Long> {

    /**
     * Checks if a company with the given CNPJ exists.
     * @param cnpj The CNPJ to check.
     * @return true if a company with the CNPJ exists, false otherwise.
     */
    boolean existsByCnpj(Long cnpj);
}
