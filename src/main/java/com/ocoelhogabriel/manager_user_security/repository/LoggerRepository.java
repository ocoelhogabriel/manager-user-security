package com.ocoelhogabriel.manager_user_security.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.manager_user_security.model.entity.LoggerEntity;

public interface LoggerRepository extends JpaRepository<LoggerEntity, Date>, JpaSpecificationExecutor<LoggerEntity> {

	List<LoggerEntity> findBySmocod(Long smocod);

	Page<LoggerEntity> findBySmocod(Long smocod, Pageable page);
}
