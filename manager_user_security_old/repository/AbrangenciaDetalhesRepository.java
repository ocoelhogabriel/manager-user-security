package com.ocoelhogabriel.manager_user_security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocoelhogabriel.manager_user_security.model.entity.AbrangenciaDetalhes;

public interface AbrangenciaDetalhesRepository extends JpaRepository<AbrangenciaDetalhes, Long> {

	List<AbrangenciaDetalhes> findByAbrangencia_Abrcod(Long codigo);

	List<AbrangenciaDetalhes> findByAbrangencia_abrcodAndRecurso_recnomContaining(Long codigo, String nome);

}
