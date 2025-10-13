package com.ocoelhogabriel.manager_user_security.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocoelhogabriel.manager_user_security.model.MedicaoModel;
import com.ocoelhogabriel.manager_user_security.model.MedicaoPayloadModel;
import com.ocoelhogabriel.manager_user_security.model.dto.MedicaoDTO;
import com.ocoelhogabriel.manager_user_security.services.MedicaoServInterface;
import com.ocoelhogabriel.manager_user_security.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/medicao")
@Tag(name = "Medições", description = "API para gerenciamento de medições")
public class MedicaoController extends SecurityRestController {

	@Autowired
	private MedicaoServInterface medicaoService;

	@GetMapping("/v1")
	@Operation(description = "Buscar todas as medições registradas. Retorna uma lista de todas as medições existentes.")
	public ResponseEntity<List<MedicaoDTO>> getMedicao() throws IOException {
		return medicaoService.findAllMedicaoDTO();
	}

	@GetMapping("/v1/{data}")
	@Operation(description = "Buscar todas as medições registradas. Retorna uma lista de todas as medições existentes.")
	public ResponseEntity<MedicaoDTO> getDateMedicao(@PathVariable String data) throws IOException, ParseException {
		return medicaoService.findByData(data);
	}

	@PostMapping("/v1/teste")
	@Operation(description = "Criar uma nova medição. Recebe os detalhes da medição e a armazena no sistema.")
	public ResponseEntity<MedicaoDTO> createMedicao(@RequestBody MedicaoModel medicaoDTO)
			throws IOException, ParseException {
		return medicaoService.save(medicaoDTO);
	}

	@PreAuthorize("permitAll()")
	@PostMapping("/v1")
	@Operation(description = "Criar uma nova medição. Recebe os detalhes da medição e a armazena no sistema.")
	public ResponseEntity<MedicaoDTO> createMedicao(@RequestBody MedicaoPayloadModel medicaoDTO)
			throws IOException, ParseException {
		return medicaoService.saveData(medicaoDTO);
	}

	@PreAuthorize("permitAll()")
	@PostMapping("/v2")
	@Operation(description = "Criar uma nova medição. Recebe os detalhes da medição e a armazena no sistema.")
	public ResponseEntity<MedicaoDTO> createMedicaoV2(@RequestBody MedicaoPayloadModel medicaoDTO)
			throws IOException, ParseException {
		return medicaoService.saveDataByNSE(medicaoDTO);
	}

	@PutMapping("/v1")
	@Operation(description = "Atualizar uma medição existente. Atualiza os detalhes de uma medição com base nas informações fornecidas.")
	public ResponseEntity<MedicaoDTO> updateMedicao(@RequestBody MedicaoModel medicaoDTO)
			throws IOException, ParseException {
		return medicaoService.update(medicaoDTO);
	}

	@DeleteMapping("/v1")
	@Operation(description = "Deletar uma medição pelo ID. Remove uma medição específica com base no ID fornecido.")
	public ResponseEntity<Void> deleteMedicao(@RequestParam(name = "dataMedicao", required = true) String dataMedicao)
			throws IOException, ParseException {
		return medicaoService.deleteByMsidth(dataMedicao);
	}

	@GetMapping("/v1/paginado")
	public ResponseEntity<Page<MedicaoDTO>> findAllPaginado(
			@RequestParam(value = "filtro", required = false) String filtro,
			@RequestParam(value = "modulo", required = false) Long modulo,
			@RequestParam(value = "dataInicio", required = false) String dataInicio,
			@RequestParam(value = "dataFim", required = false) String dataFim,
			@RequestParam(value = "pagina", defaultValue = "0") int pagina,
			@RequestParam(value = "tamanho", defaultValue = "10") int tamanho,
			@RequestParam(value = "ordenarPor", defaultValue = "data") String ordenarPor,
			@RequestParam(value = "direcao", defaultValue = "ASC") String direcao) {

		return medicaoService.medicaoFindAllPaginado(filtro, modulo, dataInicio, dataFim,
				Utils.consultaPage(MedicaoDTO.filtrarDirecao(ordenarPor), direcao, pagina, tamanho));
	}

	@GetMapping("/v1/modulo")
	public ResponseEntity<List<MedicaoDTO>> findAllPaginado(
			@RequestParam(value = "modulo", required = false) Long modulo,
			@RequestParam(value = "dataInicio", required = false) String dataInicio,
			@RequestParam(value = "direcao", defaultValue = "ASC") String direcao,
			@RequestParam(value = "dataFim", required = false) String dataFim) {

		return medicaoService.medicaoFindAllForModule(modulo, dataInicio, dataFim, direcao);
	}

}
