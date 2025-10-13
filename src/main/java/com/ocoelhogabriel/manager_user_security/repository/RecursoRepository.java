package com.ocoelhogabriel.manager_user_security.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.ocoelhogabriel.manager_user_security.model.entity.Recurso;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {
	Optional<Recurso> findByRecnom(@NonNull String nome);

	List<Recurso> findByReccodIn(Collection<Long> list);

	Page<Recurso> findByReccodIn(Pageable page, Collection<Long> list);

	Page<Recurso> findByRecnomLike(String nome, Pageable page);

	Page<Recurso> findByRecnomLikeAndReccodIn(String nome, Pageable page, Collection<Long> list);

}
