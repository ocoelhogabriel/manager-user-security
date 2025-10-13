package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository JPA para Usuario - Infrastructure Layer
 * Implementação concreta para acesso a dados
 */
public interface UsuarioJpaRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    List<Usuario> findByUsucodIn(Collection<Long> list);

    Page<Usuario> findByUsucodIn(Pageable pageable, Collection<Long> list);

    Page<Usuario> findByUsulogLike(String usulog, Pageable pageable);

    Page<Usuario> findByUsulogLikeAndUsucodIn(String usulog, Pageable pageable, Collection<Long> list);

    Optional<Usuario> findByUsulog(String usulog);

    Optional<Usuario> findByEmail(String email);

    boolean existsByCpf(Long cpf);

    boolean existsByEmail(String email);

    Page<Usuario> findByAtivo(boolean ativo, Pageable pageable);

    long countByAtivo(boolean ativo);
}
