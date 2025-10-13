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

import com.ocoelhogabriel.manager_user_security.application.services.AbrangenciaService;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.entities.AbrangenciaModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.AbrangenciaDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/abrangencia")
@Tag(name = "Abrangência", description = "API para consulta e gerenciamento de abrangências")
public class AbrangenciaController extends SecurityRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbrangenciaController.class);

    private final AbrangenciaService abrangenciaService;

    public AbrangenciaController(AbrangenciaService abrangenciaService) {
        this.abrangenciaService = abrangenciaService;
    }

    @PostMapping("/v1")
    @Operation(description = "Criar uma nova abrangência. Recebe os detalhes da abrangência e a armazena no sistema.")
    public ResponseEntity<AbrangenciaDTO> criarAbrangencia(@Valid @RequestBody AbrangenciaModel cadastro) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.SCOPE_SAVING, cadastro));
        }

        try {
            AbrangenciaModel saved = abrangenciaService.save(cadastro);
            AbrangenciaDTO dto = convertToDTO(saved);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageConstraints.SCOPE_CREATED);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageFormatterUtil.format(MessageConstraints.LOG_OPERATION_ERROR, "criar abrangência",
                        e.getMessage()), e);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v1/{codigo}")
    @Operation(description = "Buscar abrangência pelo código. Retorna os detalhes de uma abrangência específica com base no código fornecido.")
    public ResponseEntity<AbrangenciaDTO> buscarAbrangenciaPorCodigo(@PathVariable Long codigo)
            throws EntityNotFoundException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.SCOPE_FIND_BY_ID, codigo));
        }

        AbrangenciaModel abrangencia = abrangenciaService.findById(codigo);
        if (abrangencia == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.SCOPE_NOT_FOUND_ID, codigo));
            }
            throw new EntityNotFoundException(
                    MessageFormatterUtil.format(MessageConstraints.SCOPE_NOT_FOUND_ID, codigo));
        }

        AbrangenciaDTO dto = convertToDTO(abrangencia);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/v1")
    @Operation(description = "Listar todas as abrangências cadastradas. Retorna uma lista de todas as abrangências existentes.")
    public ResponseEntity<List<AbrangenciaDTO>> buscarListarAbrangencia() throws EntityNotFoundException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageConstraints.SCOPE_FIND_ALL);
        }

        List<AbrangenciaModel> abrangencias = abrangenciaService.findAll();
        List<AbrangenciaDTO> dtos = abrangencias.stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/v1/paginado")
    @Operation(description = "Busca paginada de abrangências. Retorna uma lista paginada de abrangências com opções de filtragem e ordenação.")
    public ResponseEntity<Page<AbrangenciaDTO>> buscarAbrangenciaPaginado(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho,
            @RequestParam(value = "nome", required = false) String nome) throws EntityNotFoundException {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Buscando abrangências paginadas: página {}, tamanho {}, filtro: {}", pagina, tamanho, nome);
        }

        List<AbrangenciaModel> abrangencias = abrangenciaService.findAll();

        // Filtragem por nome se necessário
        if (nome != null && !nome.isEmpty()) {
            abrangencias = abrangencias.stream()
                    .filter(a -> a.getNome() != null && a.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }

        // Cálculo para paginação manual
        int start = pagina * tamanho;
        int end = Math.min(start + tamanho, abrangencias.size());

        // Garantir que start está dentro dos limites válidos
        if (start >= abrangencias.size()) {
            start = 0;
            end = Math.min(tamanho, abrangencias.size());
        }

        List<AbrangenciaModel> paginados = abrangencias.subList(start, end);

        // Converter para DTOs
        List<AbrangenciaDTO> dtosPaginados = paginados.stream()
                .map(this::convertToDTO)
                .toList();

        // Criar objeto Page manualmente
        Page<AbrangenciaDTO> page = new PageImpl<>(
                dtosPaginados,
                PageRequest.of(pagina, tamanho),
                abrangencias.size());

        return ResponseEntity.ok(page);
    }

    @PutMapping("/v1/{codigo}")
    @Operation(description = "Atualizar uma abrangência existente. Atualiza os detalhes de uma abrangência com base no código fornecido.")
    public ResponseEntity<AbrangenciaDTO> atualizarAbrangencia(
            @Valid @PathVariable Long codigo,
            @Valid @RequestBody AbrangenciaModel entity) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.LOG_OPERATION_START,
                    "atualizar abrangência com código " + codigo));
        }

        // Verificar se a abrangência existe
        AbrangenciaModel existingAbrangencia = abrangenciaService.findById(codigo);
        if (existingAbrangencia == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.SCOPE_NOT_FOUND_ID, codigo));
            }
            return ResponseEntity.notFound().build();
        }

        try {
            // Atualizar campos
            existingAbrangencia.setNome(entity.getNome());
            existingAbrangencia.setDescricao(entity.getDescricao());
            existingAbrangencia.setRecursos(entity.getRecursos());

            // Salvar e converter para DTO
            AbrangenciaModel updated = abrangenciaService.save(existingAbrangencia);
            AbrangenciaDTO dto = convertToDTO(updated);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageConstraints.SCOPE_UPDATED);
            }

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageFormatterUtil.format(MessageConstraints.LOG_OPERATION_ERROR,
                        "atualizar abrangência " + codigo, e.getMessage()), e);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/v1/{codigo}")
    @Operation(description = "Deletar uma abrangência pelo código. Remove uma abrangência específica com base no código fornecido.")
    public ResponseEntity<Void> deletarAbrangencia(@Valid @PathVariable Long codigo) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormatterUtil.format(MessageConstraints.SCOPE_DELETING, codigo));
        }

        // Verificar se a abrangência existe
        AbrangenciaModel abrangencia = abrangenciaService.findById(codigo);
        if (abrangencia == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.SCOPE_NOT_FOUND_ID, codigo));
            }
            return ResponseEntity.notFound().build();
        }

        try {
            // Excluir a abrangência
            abrangenciaService.deleteById(codigo);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageConstraints.SCOPE_DELETED);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(MessageFormatterUtil.format(MessageConstraints.LOG_OPERATION_ERROR,
                        "deletar abrangência " + codigo, e.getMessage()), e);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Converte um AbrangenciaModel para AbrangenciaDTO
     * 
     * @param model O modelo a ser convertido
     * @return DTO convertido
     */
    private AbrangenciaDTO convertToDTO(AbrangenciaModel model) {
        if (model == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Tentativa de converter modelo nulo para DTO");
            }
            return null;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Convertendo abrangência para DTO: {}", model.getNome());
        }

        AbrangenciaDTO dto = new AbrangenciaDTO();
        dto.setNome(model.getNome());
        dto.setDescricao(model.getDescricao());

        // Nota: O ID/código não está disponível diretamente no modelo
        // O campo código no DTO precisaria ser preenchido pelo serviço

        return dto;
    }
}