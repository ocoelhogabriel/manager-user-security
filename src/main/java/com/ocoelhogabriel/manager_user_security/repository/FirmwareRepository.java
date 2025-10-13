package com.ocoelhogabriel.manager_user_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocoelhogabriel.manager_user_security.model.entity.Firmware;

public interface FirmwareRepository extends JpaRepository<Firmware, Long> {

}
