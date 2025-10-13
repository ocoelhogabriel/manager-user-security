package com.ocoelhogabriel.manager_user_security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.model.entity.Pendencia;

public interface PendenciaRepository extends JpaRepository<Pendencia, Long>, JpaSpecificationExecutor<Pendencia> {
	Page<Pendencia> findByPendel(Integer del, Pageable page);

	Optional<Pendencia> findByPencodAndPendel(Long codigo, Integer del);

	List<Pendencia> findByPendel(Integer del);

	List<Pendencia> findByPentipAndPendel(String tipo, Integer del);

	List<Pendencia> findByPenstaAndPendel(String status, Integer del);

	List<Pendencia> findByPentipAndPenstaAndPendel(String tipo, String status, Integer del);
}
