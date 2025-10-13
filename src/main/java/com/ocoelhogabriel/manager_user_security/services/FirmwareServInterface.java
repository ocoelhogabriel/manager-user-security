package com.ocoelhogabriel.manager_user_security.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ocoelhogabriel.manager_user_security.model.dto.FirmwareDTO;

public interface FirmwareServInterface {

	ResponseEntity<FirmwareDTO> update(Long codigo, MultipartFile file, String modelo) throws IOException;

	ResponseEntity<FirmwareDTO> save(MultipartFile file, String modelo) throws IOException;

	ResponseEntity<Void> delete(Long codigo) throws IOException;

	ResponseEntity<Resource> findByIdDownload(Long codigo) throws NoSuchAlgorithmException;

	ResponseEntity<Page<FirmwareDTO>> findAllPaginado(Pageable pageable);

	ResponseEntity<List<FirmwareDTO>> findAll();

	ResponseEntity<FirmwareDTO> findById(Long codigo);
}
