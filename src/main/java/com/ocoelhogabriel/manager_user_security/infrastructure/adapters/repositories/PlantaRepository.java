package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Planta;

public interface PlantaRepository extends JpaRepository<Planta, Long>, JpaSpecificationExecutor<Planta> {

	void removeByPlacod(Long codigo);

}
