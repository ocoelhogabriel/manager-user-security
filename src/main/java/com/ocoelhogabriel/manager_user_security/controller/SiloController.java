package com.ocoelhogabriel.manager_user_security.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocoelhogabriel.manager_user_security.model.SiloModel;
import com.ocoelhogabriel.manager_user_security.model.dto.SiloDTO;
import com.ocoelhogabriel.manager_user_security.services.SiloServInterface;
import com.ocoelhogabriel.manager_user_security.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/silo")
@Tag(name = "Silo", description = "API para gerenciamento de silos")
public class SiloController extends SecurityRestController {

	@Autowired
	private SiloServInterface silo;

	@GetMapping("/v1")
	@Operation(description = "Listar silos cadastrados. Retorna uma lista de todos os silos existentes no sistema.")
	public ResponseEntity<List<SiloDTO>> getSilo() throws IOException {
		return silo.findAllSiloDTO();
	}

	@GetMapping("/v1/{codigo}")
	@Operation(description = "Buscar pelo silo cadastrado. Retorna um silo existente.")
	public ResponseEntity<SiloDTO> getIdSilo(@Valid @PathVariable Long codigo) {
		return silo.findById(codigo);
	}

	@PostMapping("/v1")
	@Operation(description = "Cadastro de um novo silo. Recebe os detalhes do silo e o armazena no sistema.")
	public ResponseEntity<SiloDTO> createSilo(@RequestBody SiloModel siloDTO) throws IOException {
		return silo.save(siloDTO);
	}

	@PutMapping("/v1/{codigo}")
	@Operation(description = "Atualização de um silo existente. Atualiza os detalhes de um silo com base no código fornecido.")
	public ResponseEntity<SiloDTO> updateSilo(@PathVariable Long codigo, @RequestBody SiloModel siloDTO) throws IOException {
		return silo.update(codigo, siloDTO);
	}

	@DeleteMapping("/v1/{codigo}")
	@Operation(description = "Deletar um silo pelo código. Remove um silo específico com base no código fornecido.")
	public ResponseEntity<Void> deleteSilo(@PathVariable Long codigo) throws IOException {
		return silo.deleteByPlacod(codigo);
	}

	@Operation(description = "Recupera uma lista paginada de objetos SiloDTO com filtragem e ordenação opcionais.")
	@Parameter(name = "filtro", description = "Termo de filtro opcional para buscar Silos.")
	@Parameter(name = "pagina", description = "Número da página a ser recuperada, começando em 0.")
	@Parameter(name = "tamanho", description = "Número de itens por página.")
	@Parameter(name = "ordenarPor", description = "Campo pelo qual os resultados serão ordenados. (codigo, tipoSilo, planta, nome)")
	@Parameter(name = "direcao", description = "Direção da ordenação, podendo ser ASC (ascendente) ou DESC (descendente).")
	@GetMapping("/v1/paginado")
	public ResponseEntity<Page<SiloDTO>> findAllPaginado(@RequestParam(value = "filtro", required = false) String filtro, @RequestParam(value = "pagina", defaultValue = "0") int pagina, @RequestParam(value = "tamanho", defaultValue = "10") int tamanho,
			@RequestParam(value = "ordenarPor", defaultValue = "codigo") String ordenarPor, @RequestParam(value = "direcao", defaultValue = "ASC") String direcao) {

		return silo.siloFindAllPaginado(filtro, Utils.consultaPage(SiloDTO.filtrarDirecao(ordenarPor), direcao, pagina, tamanho));
	}

}
