package com.ocoelhogabriel.manager_user_security.interfaces.web.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

import com.ocoelhogabriel.manager_user_security.application.services.RecursoService;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageTemplateKeys;
import com.ocoelhogabriel.manager_user_security.domain.entities.RecursoModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.RecursoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recurso")
@Tag(name = "Recurso", description = "API para consulta e gerenciamento de recursos")
public class RecursoController extends SecurityRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecursoController.class);
    
    private final RecursoService recursoService;

    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    @PostMapping("/v1")
    @Operation(description = "Criar um novo recurso. Recebe os detalhes do recurso e o armazena no sistema. Obs: Gerado automaticamente ao iniciar a aplicação.")
    public ResponseEntity<RecursoDTO> criarRecurso(@RequestBody RecursoModel cadastro) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.RESOURCE_SAVING, cadastro));
        }
        
        try {
            RecursoModel saved = recursoService.save(cadastro);
            RecursoDTO dto = convertToDTO(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            LOGGER.error(MessageFormatterUtil.format(MessageConstraints.LOG_OPERATION_ERROR, "criar recurso", e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v1/{codigo}")
    @Operation(description = "Buscar recurso pelo código. Retorna os detalhes de um recurso específico com base no código fornecido.")
    public ResponseEntity<RecursoDTO> buscarRecursoPorCodigo(@PathVariable Long codigo) throws EntityNotFoundException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.RESOURCE_FINDING_BY_ID, codigo));
        }
        
        RecursoModel recurso = recursoService.findById(codigo);
        if (recurso == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.RESOURCE_NOT_FOUND_ID, codigo));
            }
            throw new EntityNotFoundException(
                MessageFormatterUtil.formatTemplate(MessageTemplateKeys.RESOURCE_NOT_FOUND_ID, codigo));
        }
        
        RecursoDTO dto = convertToDTO(recurso);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/v1")
    @Operation(description = "Listar todos os recursos cadastrados. Retorna uma lista de todos os recursos existentes.")
    public ResponseEntity<List<RecursoDTO>> buscarListarRecurso() throws EntityNotFoundException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageConstraints.RESOURCE_RETRIEVING_ALL);
        }
        
        List<RecursoModel> recursos = recursoService.findAll();
        List<RecursoDTO> dtos = recursos.stream()
                .map(this::convertToDTO)
                .toList();
        
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/v1/paginado")
    @Operation(description = "Busca paginada de recursos. Retorna uma lista paginada de recursos com opções de filtragem e ordenação. Obs: O campo 'ordenarPor' requer os seguintes dados: código, nome, descrição.")
    public ResponseEntity<Page<RecursoDTO>> buscarRecursoPaginado(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina, 
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho, 
            @RequestParam(value = "nome", required = false) String nome) throws EntityNotFoundException {
        
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Buscando recursos paginados: página {}, tamanho {}, filtro: {}", pagina, tamanho, nome);
        }
        
        // Buscar todos os recursos e fazer filtragem na aplicação (abordagem simplificada)
        List<RecursoModel> recursos = recursoService.findAll();
        
        // Filtragem por nome se necessário
        if (nome != null && !nome.isEmpty()) {
            recursos = recursos.stream()
                .filter(r -> r.getNome() != null && r.getNome().toString().contains(nome.toUpperCase()))
                .toList();
        }
        
        // Cálculo para paginação manual
        int start = pagina * tamanho;
        int end = Math.min(start + tamanho, recursos.size());
        List<RecursoModel> paginados = recursos.subList(start, end);
        
        // Converter para DTOs
        List<RecursoDTO> dtosPaginados = paginados.stream()
                .map(this::convertToDTO)
                .toList();
        
        // Criar objeto Page manualmente
        Page<RecursoDTO> page = new PageImpl<>(
                dtosPaginados, 
                PageRequest.of(pagina, tamanho), 
                recursos.size());
        
        return ResponseEntity.ok(page);
    }

    @PutMapping("/v1/{codigo}")
    @Operation(description = "Atualizar um recurso existente. Atualiza os detalhes de um recurso com base no código fornecido. Obs: Como recurso é gerado automaticamente, se alterado pode ocasionar algum problema nas funcionalidades.")
    public ResponseEntity<RecursoDTO> atualizarRecurso(
            @Valid @PathVariable Long codigo, 
            @Valid @RequestBody RecursoModel entity) {
        
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.LOG_PLANT_UPDATE, codigo, entity));
        }
        
        // Verificar se o recurso existe
        RecursoModel existingRecurso = recursoService.findById(codigo);
        if (existingRecurso == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.RESOURCE_NOT_FOUND_ID, codigo));
            }
            return ResponseEntity.notFound().build();
        }
        
        try {
            // Atualizar campos
            existingRecurso.setNome(entity.getNome());
            existingRecurso.setDescricao(entity.getDescricao());
            
            // Salvar e converter para DTO
            RecursoModel updated = recursoService.save(existingRecurso);
            RecursoDTO dto = convertToDTO(updated);
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            LOGGER.error(MessageFormatterUtil.format(MessageConstraints.LOG_OPERATION_ERROR, 
                "atualizar recurso " + codigo, e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/v1/{codigo}")
    @Operation(description = "Deletar um recurso pelo código. Remove um recurso específico com base no código fornecido. Obs: Como recurso é gerado automaticamente, se alterado pode ocasionar algum problema nas funcionalidades.")
    public ResponseEntity<Void> deletarRecurso(@Valid @PathVariable Long codigo) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.RESOURCE_DELETING, codigo));
        }
        
        // Verificar se o recurso existe
        RecursoModel recurso = recursoService.findById(codigo);
        if (recurso == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.RESOURCE_NOT_FOUND_ID, codigo));
            }
            return ResponseEntity.notFound().build();
        }
        
        try {
            // Excluir o recurso
            recursoService.deleteById(codigo);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LOGGER.error(MessageFormatterUtil.format(MessageConstraints.LOG_OPERATION_ERROR, 
                "deletar recurso " + codigo, e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Converte um RecursoModel para RecursoDTO
     * 
     * @param model O modelo a ser convertido
     * @return DTO convertido
     */
    private RecursoDTO convertToDTO(RecursoModel model) {
        if (model == null) {
            return null;
        }
        
        RecursoDTO dto = new RecursoDTO();
        // Definimos o nome do recurso a partir do enum
        if (model.getNome() != null) {
            dto.setNome(model.getNome().getNome());
        }
        dto.setDescricao(model.getDescricao());
        
        // Nota: Assumimos que o código/ID seria atribuído pelo serviço
        // Idealmente, o serviço deveria retornar o objeto com o ID preenchido após save
        
        return dto;
    }
}
