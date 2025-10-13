package com.ocoelhogabriel.manager_user_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.model.entity.Silo;

public interface SiloRepository extends JpaRepository<Silo, Long>, JpaSpecificationExecutor<Silo> {

	void deleteByPlanta_Placod(Long codigo);

}
