package com.ocoelhogabriel.manager_user_security.services.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ocoelhogabriel.manager_user_security.model.dto.FirmwareDTO;
import com.ocoelhogabriel.manager_user_security.model.entity.Firmware;
import com.ocoelhogabriel.manager_user_security.repository.FirmwareRepository;
import com.ocoelhogabriel.manager_user_security.services.FirmwareServInterface;
import com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class FirmwareServiceImpl implements FirmwareServInterface {

	@Autowired
	private FirmwareRepository firmwareRepository;

	@Override
	public ResponseEntity<FirmwareDTO> update(Long codigo, MultipartFile file, String modelo) throws IOException {
		Objects.requireNonNull(codigo, "Código do Firmware está nulo.");
		Objects.requireNonNull(file, "Arquivo do Firmware está nulo.");
		Objects.requireNonNull(modelo, "Modelo está nulo.");

		byte[] fileBytes = file.getBytes();
		Firmware firmware = new Firmware(codigo, modelo, file.getOriginalFilename(), file.getContentType(), fileBytes);
		Firmware savedFirmware = firmwareRepository.save(firmware);

		return MessageResponse.success(new FirmwareDTO(savedFirmware));
	}

	@Override
	public ResponseEntity<FirmwareDTO> save(MultipartFile file, String modelo) throws IOException {
		Objects.requireNonNull(file, "Arquivo do Firmware está nulo.");
		Objects.requireNonNull(modelo, "Modelo está nulo.");

		byte[] fileBytes = file.getBytes();
		Firmware firmware = new Firmware(null, modelo, file.getOriginalFilename(), file.getContentType(), fileBytes);
		Firmware savedFirmware = firmwareRepository.save(firmware);

		return MessageResponse.success(new FirmwareDTO(savedFirmware));
	}

	@Override
	public ResponseEntity<Void> delete(Long codigo) throws IOException {
		Objects.requireNonNull(codigo, "Código está nulo.");

		Firmware firmware = firmwareRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Firmware não encontrado ou já está deletado."));
		firmwareRepository.deleteById(firmware.getFircod());

		return MessageResponse.noContent();
	}

	@Override
	public ResponseEntity<Resource> findByIdDownload(Long codigo) throws NoSuchAlgorithmException {
		Objects.requireNonNull(codigo, "Código do Firmware está nulo.");

		Firmware firmware = firmwareRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Firmware não encontrado ou deletado."));

		byte[] fileBytes = firmware.getFirarq();
		if (fileBytes == null) {
			throw new EntityNotFoundException("Erro ao obter os bytes do arquivo");
		}

		byte[] hash = MessageDigest.getInstance("SHA-256").digest(fileBytes);
		String checksum = new BigInteger(1, hash).toString(16);
		Resource resource = new ByteArrayResource(fileBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + firmware.getFirnam() + "\"");
		headers.add(HttpHeaders.CONTENT_TYPE, firmware.getFirdesc());
		headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileBytes.length));
		headers.add("Checksum", checksum);

		return ResponseEntity.ok().headers(headers).body(resource);
	}

	@Override
	public ResponseEntity<Page<FirmwareDTO>> findAllPaginado(Pageable pageable) {
		Objects.requireNonNull(pageable, "Pageable do Firmware está nulo.");

		Page<Firmware> result = firmwareRepository.findAll(pageable);
		return MessageResponse.success(result.map(FirmwareDTO::new));
	}

	@Override
	public ResponseEntity<List<FirmwareDTO>> findAll() {
		List<Firmware> firmwares = firmwareRepository.findAll();
		List<FirmwareDTO> firmwareDTOs = firmwares.stream().map(FirmwareDTO::new).toList();

		return MessageResponse.success(firmwareDTOs);
	}

	@Override
	public ResponseEntity<FirmwareDTO> findById(Long codigo) {
		Objects.requireNonNull(codigo, "Código do Firmware está nulo.");

		Firmware firmware = firmwareRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Firmware não encontrada ou deletada."));

		return MessageResponse.success(new FirmwareDTO(firmware));
	}
}
