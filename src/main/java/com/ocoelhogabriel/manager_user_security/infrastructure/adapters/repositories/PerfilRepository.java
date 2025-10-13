package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long>, JpaSpecificationExecutor<Perfil> {

	List<Perfil> findByPercodIn(Collection<Long> list);

	Page<Perfil> findByPercodIn(Pageable page, Collection<Long> list);

	Page<Perfil> findByPernomLike(String nome, Pageable page);

	Page<Perfil> findByPernomLikeAndPercodIn(String nome, Pageable page, Collection<Long> list);

	Optional<Perfil> findByPernom(String nome);
}
