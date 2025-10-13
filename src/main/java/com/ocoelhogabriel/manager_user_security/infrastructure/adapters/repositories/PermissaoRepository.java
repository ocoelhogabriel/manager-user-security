package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Permissao;

import jakarta.transaction.Transactional;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

	@Transactional
	void deleteByPerfil_Percod(Long percod);

	Optional<List<Permissao>> findByPerfil_percod(Long codigo);

	Optional<List<Permissao>> findByPerfil_PercodAndPemcodIn(Long codigo, Collection<Long> list);

	Optional<Permissao> findByPerfil_percodAndRecurso_recnom(Long codigo, String recurso);

	List<Permissao> findByPemcodIn(Collection<Long> list);

	Page<Permissao> findByPemcodIn(Pageable page, Collection<Long> list);

	Page<Permissao> findByRecurso_RecnomLike(String nome, Pageable page);

	Page<Permissao> findByRecurso_RecnomLikeAndPemcodIn(String nome, Pageable page, Collection<Long> list);

}
