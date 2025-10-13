package com.ocoelhogabriel.manager_user_security.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.model.entity.Medicao;
import com.ocoelhogabriel.manager_user_security.model.entity.SiloModulo;

public interface MedicaoRepository extends JpaRepository<Medicao, Date>, JpaSpecificationExecutor<Medicao> {

	void deleteByMsidth(Date msidth);

	Optional<Medicao> findByMsidth(Date msidth);

	Optional<Medicao> findFirstByModuloOrderByMsidthDesc(SiloModulo silomodulo);

}
