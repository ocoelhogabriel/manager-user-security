package com.ocoelhogabriel.manager_user_security.interfaces.web.controllers;

import java.util.ArrayList;
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

import com.ocoelhogabriel.manager_user_security.application.services.PlantaService;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageTemplateKeys;
import com.ocoelhogabriel.manager_user_security.domain.entities.PlantaModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.PlantaDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador para operações relacionadas a plantas.
 * Segue padrões REST e utiliza o serviço PlantaService para as operações de negócio.
 */
@RestController
@RequestMapping("/api/planta")
@Tag(name = "Planta", description = "API para consulta e gerenciamento de plantas")
public class PlantaController extends SecurityRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantaController.class);
    
    private final PlantaService plantaService;
    
    public PlantaController(PlantaService plantaService) {
        this.plantaService = plantaService;
    }

    @PostMapping("/v1")
    @Operation(description = "Criar uma nova planta. Recebe os detalhes da planta e a armazena no sistema.")
    public ResponseEntity<PlantaDTO> criarPlanta(@Valid @RequestBody PlantaModel cadastro) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.LOG_PLANT_CREATE, cadastro));
        }
        
        try {
            PlantaDTO plantaCriada = plantaService.criarPlanta(cadastro);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageConstraints.PLANT_CREATED);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(plantaCriada);
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageFormatterUtil.format(MessageConstraints.PLANT_ERROR_CREATE, e.getMessage()), e);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v1/{codigo}")
    @Operation(description = "Buscar planta pelo código. Retorna os detalhes de uma planta específica com base no código fornecido.")
    public ResponseEntity<PlantaDTO> buscarPlantaPorCodigo(@PathVariable Long codigo) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.LOG_PLANT_SEARCH, codigo));
        }
        
        try {
            PlantaDTO planta = plantaService.buscarPlantaPorId(codigo);
            return ResponseEntity.ok(planta);
        } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatterUtil.formatTemplate(MessageTemplateKeys.PLANT_NOT_FOUND_ID, codigo));
            }
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/v1")
    @Operation(description = "Listar todas as plantas cadastradas. Retorna uma lista de todas as plantas existentes.")
    public ResponseEntity<List<PlantaDTO>> buscarListarPlanta() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageConstraints.LOG_PLANT_LIST);
        }
        
        try {
            List<PlantaDTO> plantas = plantaService.listarPlantas(null);
            return ResponseEntity.ok(plantas);
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageFormatterUtil.format(MessageConstraints.PLANT_LIST_ERROR, e.getMessage()), e);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v1/paginado")
    @Operation(description = "Busca paginada de plantas. Retorna uma lista paginada de plantas com opções de filtragem e ordenação.")
    public ResponseEntity<Page<PlantaDTO>> buscarPlantaPaginado(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina, 
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho, 
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "empresa", required = false) Long empresaId) {
        
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.LOG_PLANT_PAGED, 
                    pagina, tamanho, nome, empresaId));
        }
        
        try {
            // Busca todas as plantas (filtradas por empresa se necessário)
            List<PlantaDTO> todasPlantas = plantaService.listarPlantas(empresaId);
            
            // Filtrar por nome se fornecido
            if (nome != null && !nome.isEmpty()) {
                todasPlantas = todasPlantas.stream()
                        .filter(p -> p.getNome() != null && p.getNome().toLowerCase().contains(nome.toLowerCase()))
                        .toList();
            }
            
            // Implementar paginação
            int start = pagina * tamanho;
            int end = Math.min(start + tamanho, todasPlantas.size());
            
            // Garantir que start está dentro dos limites válidos
            if (start >= todasPlantas.size() && !todasPlantas.isEmpty()) {
                start = 0;
                end = Math.min(tamanho, todasPlantas.size());
            }
            
            List<PlantaDTO> paginados = start < todasPlantas.size() ? 
                    todasPlantas.subList(start, end) : new ArrayList<>();
            
            // Criar objeto Page
            Page<PlantaDTO> page = new PageImpl<>(
                    paginados, 
                    PageRequest.of(pagina, tamanho), 
                    todasPlantas.size());
            
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            LOGGER.error(MessageFormatterUtil.format(MessageConstraints.PLANT_LIST_ERROR, e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/v1/{codigo}")
    @Operation(description = "Atualizar uma planta existente. Atualiza os detalhes de uma planta com base no código fornecido.")
    public ResponseEntity<PlantaDTO> atualizarPlanta(
            @PathVariable Long codigo, 
            @Valid @RequestBody PlantaModel plantaModel) {
        
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.LOG_PLANT_UPDATE, codigo, plantaModel));
        }
        
        try {
            PlantaDTO plantaAtualizada = plantaService.atualizarPlanta(codigo, plantaModel);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageConstraints.PLANT_UPDATED);
            }
            return ResponseEntity.ok(plantaAtualizada);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrada")) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(MessageFormatterUtil.formatTemplate(MessageTemplateKeys.PLANT_NOT_FOUND_ID, codigo));
                }
                return ResponseEntity.notFound().build();
            }
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageFormatterUtil.format(MessageConstraints.PLANT_ERROR_UPDATE, e.getMessage()), e);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/v1/{codigo}")
    @Operation(description = "Deletar uma planta pelo código. Remove uma planta específica com base no código fornecido.")
    public ResponseEntity<Void> deletarPlanta(@PathVariable Long codigo) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.LOG_PLANT_DELETE, codigo));
        }
        
        try {
            boolean sucesso = plantaService.removerPlanta(codigo);
            if (sucesso) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(MessageConstraints.PLANT_DELETED);
                }
                return ResponseEntity.noContent().build();
            } else {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(MessageFormatterUtil.format(MessageConstraints.PLANT_ERROR_DELETE, "Operação retornou falha"));
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrada")) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(MessageFormatterUtil.formatTemplate(MessageTemplateKeys.PLANT_NOT_FOUND_ID, codigo));
                }
                return ResponseEntity.notFound().build();
            }
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageFormatterUtil.format(MessageConstraints.PLANT_ERROR_DELETE, e.getMessage()), e);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}