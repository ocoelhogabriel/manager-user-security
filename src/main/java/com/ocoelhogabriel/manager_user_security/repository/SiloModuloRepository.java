package com.ocoelhogabriel.manager_user_security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.model.entity.SiloModulo;

public interface SiloModuloRepository extends JpaRepository<SiloModulo, Long>, JpaSpecificationExecutor<SiloModulo> {

	Optional<SiloModulo> findBySmonse(String nse);

}
