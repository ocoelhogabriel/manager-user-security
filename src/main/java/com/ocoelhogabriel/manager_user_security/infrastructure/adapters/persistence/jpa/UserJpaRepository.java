package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario;

/**
 * Repositório JPA para UserEntity
 * Aplica Repository Pattern do Spring Data JPA
 * Interface específica para operações de persistência
 */
@Repository
public interface UserJpaRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca usuário por username
     */
    Optional<Usuario> findByUsername(String username);
    
    /**
     * Busca usuário por email
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Busca todos os usuários ativos
     */
    List<Usuario> findByActiveTrue();
    
    /**
     * Verifica se existe usuário com o username
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica se existe usuário com o email
     */
    boolean existsByEmail(String email);
    
    /**
     * Conta usuários ativos
     */
    long countByActiveTrue();
    
    /**
     * Busca usuários por status ativo
     */
    @Query("SELECT u FROM UserEntity u WHERE u.active = :active")
    List<Usuario> findByActiveStatus(@Param("active") Boolean active);
    
    /**
     * Busca usuários por username que contenha o texto
     */
    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:username% AND u.active = true")
    List<Usuario> findByUsernameContainingAndActive(@Param("username") String username);
}
