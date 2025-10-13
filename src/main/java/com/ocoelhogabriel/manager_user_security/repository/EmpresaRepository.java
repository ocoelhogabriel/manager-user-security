package com.ocoelhogabriel.manager_user_security.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.model.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>, JpaSpecificationExecutor<Empresa> {

	Optional<Empresa> findByEmpcnp(Long empcnp);

	Page<Empresa> findByEmpcodIn(Pageable pageable, Collection<Long> list);

	Page<Empresa> findByEmpnomLike(String empnom, Pageable pageable);

	Page<Empresa> findByEmpnomLikeAndEmpcodIn(String empnom, Pageable pageable, Collection<Long> list);

	List<Empresa> findByEmpcodIn(Collection<Long> abdcods);

}
