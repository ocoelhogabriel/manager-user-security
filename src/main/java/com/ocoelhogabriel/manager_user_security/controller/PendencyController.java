package com.ocoelhogabriel.manager_user_security.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocoelhogabriel.manager_user_security.model.PendenciaDelete;
import com.ocoelhogabriel.manager_user_security.model.PendenciaModel;
import com.ocoelhogabriel.manager_user_security.model.dto.KeepAliveDTO;
import com.ocoelhogabriel.manager_user_security.model.dto.PendenciasDTO;
import com.ocoelhogabriel.manager_user_security.model.enums.StatusEnum;
import com.ocoelhogabriel.manager_user_security.services.PendenciaServInterface;
import com.ocoelhogabriel.manager_user_security.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("api/pendencia")
@Tag(name = "Pendências", description = "API para controle e gerenciamento de pendências")
public class PendencyController extends SecurityRestController {

	@Autowired
	private PendenciaServInterface pendenciaService;

	@GetMapping("/v1/keepAlive/{numeroDeSerie}")
	@Operation(description = "KeepAlive do módulo. Atualiza a DataHora e verifica pendências do módulo pelo número de série.")
	public ResponseEntity<KeepAliveDTO> buscarPendenciaDeModuloPorNumSerie(@Valid @PathVariable @NonNull String numeroDeSerie) throws EntityNotFoundException, IOException {
		return pendenciaService.findByKeepAlive(numeroDeSerie);
	}

	@GetMapping("/v1/paginado")
	@Operation(description = "Busca paginada de pendências. Retorna uma lista paginada de pendências com opções de filtragem e ordenação.")
	public ResponseEntity<Page<PendenciasDTO>> buscarPendenciaPaginado(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho, @RequestParam(value = "filtro", required = false) String filtro,
			@RequestParam(value = "modulo", required = false) Long modulo, @RequestParam(value = "ordenarPor", defaultValue = "id") String ordenarPor, @RequestParam(value = "direcao", defaultValue = "ASC", required = false) String direcao) throws EntityNotFoundException {
		String ordenarEntity = PendenciasDTO.consultaPagable(ordenarPor);
		if (ordenarEntity == null) {
			return ResponseEntity.badRequest().body(Page.empty());
		}
		return pendenciaService.findAllPaginado(filtro, modulo, Utils.consultaPage(ordenarEntity, direcao, pagina, tamanho));
	}

	@GetMapping("/v1")
	@Operation(description = "Listar todas as pendências cadastradas. Retorna uma lista de todas as pendências existentes.")
	public ResponseEntity<List<PendenciasDTO>> buscarListaPendencia() {
		return pendenciaService.findByAll();
	}

	@PostMapping("/v1")
	@Operation(description = "Criar uma nova pendência. Recebe os detalhes da pendência e a armazena no sistema.")
	public ResponseEntity<PendenciasDTO> criarPendencia(@Valid @RequestBody PendenciaModel entity) throws EntityNotFoundException, IOException {
		return pendenciaService.save(entity);
	}

	@PutMapping("/v1")
	@Operation(description = "Atualizar uma pendência existente. Atualiza o status de uma pendência com base no ID fornecido.")
	public ResponseEntity<PendenciasDTO> atualizarPendencia(@Valid @RequestParam("idPendencia") Long idPendencia, @Valid @RequestParam("status") StatusEnum status) throws ParseException {
		return pendenciaService.update(idPendencia, status);
	}

	@DeleteMapping("/v1")
	@Operation(description = "Deletar uma pendência existente. Remove uma pendência com base no ID fornecido.")
	public ResponseEntity<PendenciasDTO> deletarPendencia(@RequestBody PendenciaDelete delete) throws ParseException, IOException {
		return pendenciaService.delete(delete);
	}
}
