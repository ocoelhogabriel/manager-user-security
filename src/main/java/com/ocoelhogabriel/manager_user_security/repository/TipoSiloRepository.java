package com.ocoelhogabriel.manager_user_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.model.entity.TipoSilo;

public interface TipoSiloRepository extends JpaRepository<TipoSilo, Long>, JpaSpecificationExecutor<TipoSilo> {

	void deleteByTsicod(Integer tsicod);

}
