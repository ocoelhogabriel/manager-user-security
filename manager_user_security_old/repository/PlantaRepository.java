package com.ocoelhogabriel.manager_user_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.model.entity.Planta;

public interface PlantaRepository extends JpaRepository<Planta, Long>, JpaSpecificationExecutor<Planta> {

	void removeByPlacod(Long codigo);

}
