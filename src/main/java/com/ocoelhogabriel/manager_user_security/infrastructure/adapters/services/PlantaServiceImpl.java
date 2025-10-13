package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.application.services.EmpresaService;
import com.ocoelhogabriel.manager_user_security.application.services.PlantaService;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.entities.EmpresaModel;
import com.ocoelhogabriel.manager_user_security.domain.entities.PlantaModel;
import com.ocoelhogabriel.manager_user_security.exception.CustomMessageExcep;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.EmpresaDTO;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.PlantaDTO;
import com.ocoelhogabriel.manager_user_security.utils.logging.LogCategory;
import com.ocoelhogabriel.manager_user_security.utils.logging.LogManager;

/**
 * Implementação do serviço de plantas utilizando armazenamento em memória.
 * Em um ambiente de produção, esta implementação seria substituída por uma que
 * utiliza um repositório de banco de dados.
 */
@Service
public class PlantaServiceImpl implements PlantaService {

    private static final Logger LOGGER = LogManager.getLogger(PlantaServiceImpl.class);
    private static final String PLANTAS_REPOSITORY = "Plantas";
    
    private final EmpresaService empresaService;
    
    // Armazenamento em memória para plantas (simulando um banco de dados)
    private final Map<Long, PlantaModel> plantas = new HashMap<>();
    private Long nextId = 1L;
    
    public PlantaServiceImpl(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @Override
    public PlantaDTO criarPlanta(PlantaModel plantaModel) {
        LogManager.info(LOGGER, LogCategory.BUSINESS,
            MessageFormatterUtil.format(MessageConstraints.PLANT_CREATING, plantaModel),
            "plantName", plantaModel.getNome(),
            "companyId", plantaModel.getEmpresa());
        
        // Verifica se já existe uma planta com o mesmo nome
        boolean nomeExistente = plantas.values().stream()
                .anyMatch(p -> p.getNome() != null && p.getNome().equalsIgnoreCase(plantaModel.getNome()));
        
        if (nomeExistente) {
            LogManager.error(LOGGER, LogCategory.BUSINESS,
                MessageFormatterUtil.format(MessageConstraints.PLANT_NAME_EXISTS, plantaModel.getNome()),
                "plantName", plantaModel.getNome(),
                "errorType", "DuplicateNameException");
            try {
                throw CustomMessageExcep.exceptionIOException(
                    "criar", "Planta", plantaModel, new RuntimeException("Nome já existe"));
            } catch (IOException e) {
                throw new RuntimeException(MessageConstraints.PLANT_NAME_EXISTS, e);
            }
        }
        
        // Verifica se a empresa existe
        EmpresaModel empresa = empresaService.findById(plantaModel.getEmpresa());
        if (empresa == null) {
            LogManager.error(LOGGER, LogCategory.BUSINESS,
                MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_NOT_FOUND_ID, plantaModel.getEmpresa()),
                "companyId", plantaModel.getEmpresa(),
                "errorType", "EntityNotFoundException");
            try {
                throw CustomMessageExcep.exceptionIOException(
                    "criar", "Planta", plantaModel, new RuntimeException(MessageConstraints.ENTERPRISE_NOT_FOUND));
            } catch (IOException e) {
                throw new RuntimeException(MessageConstraints.ENTERPRISE_NOT_FOUND, e);
            }
        }
        
        // Cria nova planta com ID gerado
        Long id = nextId++;
        
        // Armazena a planta
        plantas.put(id, plantaModel);
        LogManager.info(LOGGER, LogCategory.BUSINESS,
            MessageFormatterUtil.format(MessageConstraints.PLANT_CREATED_SUCCESS, id),
            "plantId", id,
            "plantName", plantaModel.getNome(),
            "companyId", plantaModel.getEmpresa());
        
        // Criar EmpresaDTO para PlantaDTO
        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setCodigo(plantaModel.getEmpresa());
        
        // Retorna o DTO da planta criada
        PlantaDTO plantaDTO = new PlantaDTO(id);
        plantaDTO.setNome(plantaModel.getNome());
        plantaDTO.setEmpresa(empresaDTO);
        
        return plantaDTO;
    }

    @Override
    public PlantaDTO atualizarPlanta(Long id, PlantaModel plantaModel) {
        LogManager.info(LOGGER, LogCategory.BUSINESS,
            MessageFormatterUtil.format(MessageConstraints.PLANT_UPDATING, id),
            "plantId", id,
            "plantName", plantaModel.getNome(),
            "companyId", plantaModel.getEmpresa());
        
        if (!plantas.containsKey(id)) {
            LogManager.error(LOGGER, LogCategory.BUSINESS,
                MessageFormatterUtil.formatTemplate("plant.not.found.id", id),
                "plantId", id,
                "errorType", "EntityNotFoundException");
            try {
                throw CustomMessageExcep.exceptionEntityNotFoundException(
                    id, PLANTAS_REPOSITORY, new RuntimeException(MessageConstraints.PLANT_NOT_FOUND));
            } catch (Exception e) {
                throw new RuntimeException(MessageConstraints.PLANT_NOT_FOUND, e);
            }
        }
        
        // Verifica se o nome já existe em outra planta
        boolean nomeExistente = plantas.values().stream()
                .filter(p -> !plantas.get(id).equals(p))
                .anyMatch(p -> p.getNome() != null && p.getNome().equalsIgnoreCase(plantaModel.getNome()));
        
        if (nomeExistente) {
            LogManager.error(LOGGER, LogCategory.BUSINESS,
                MessageFormatterUtil.format(MessageConstraints.PLANT_NAME_EXISTS_OTHER, plantaModel.getNome()),
                "plantId", id,
                "plantName", plantaModel.getNome(),
                "errorType", "DuplicateNameException");
            try {
                throw CustomMessageExcep.exceptionIOException(
                    "atualizar", "Planta", plantaModel, new RuntimeException(MessageConstraints.PLANT_NAME_EXISTS_OTHER));
            } catch (IOException e) {
                throw new RuntimeException(MessageConstraints.PLANT_NAME_EXISTS_OTHER, e);
            }
        }
        
        // Verifica se a empresa existe
        EmpresaModel empresa = empresaService.findById(plantaModel.getEmpresa());
        if (empresa == null) {
            LogManager.error(LOGGER, LogCategory.BUSINESS,
                MessageFormatterUtil.format(MessageConstraints.ENTERPRISE_NOT_FOUND_ID, plantaModel.getEmpresa()),
                "plantId", id,
                "companyId", plantaModel.getEmpresa(),
                "errorType", "EntityNotFoundException");
            try {
                throw CustomMessageExcep.exceptionIOException(
                    "atualizar", "Planta", plantaModel, new RuntimeException(MessageConstraints.ENTERPRISE_NOT_FOUND));
            } catch (IOException e) {
                throw new RuntimeException(MessageConstraints.ENTERPRISE_NOT_FOUND, e);
            }
        }
        
        // Atualiza a planta existente
        plantas.put(id, plantaModel);
        
        LogManager.info(LOGGER, LogCategory.BUSINESS,
            MessageFormatterUtil.format(MessageConstraints.PLANT_UPDATED_SUCCESS, id),
            "plantId", id,
            "plantName", plantaModel.getNome(),
            "companyId", plantaModel.getEmpresa());
        
        // Criar EmpresaDTO para PlantaDTO
        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setCodigo(plantaModel.getEmpresa());
        
        // Retorna o DTO da planta atualizada
        PlantaDTO plantaDTO = new PlantaDTO(id);
        plantaDTO.setNome(plantaModel.getNome());
        plantaDTO.setEmpresa(empresaDTO);
        
        return plantaDTO;
    }

    @Override
    public PlantaDTO buscarPlantaPorId(Long id) {
        LogManager.info(LOGGER, LogCategory.BUSINESS,
            MessageFormatterUtil.format(MessageConstraints.PLANT_SEARCHING, id),
            "plantId", id,
            "operation", "search");
        
        PlantaModel planta = plantas.get(id);
        
        if (planta == null) {
            LogManager.warn(LOGGER, LogCategory.BUSINESS,
                MessageFormatterUtil.formatTemplate("plant.not.found.id", id),
                "plantId", id,
                "errorType", "EntityNotFoundException");
            try {
                throw CustomMessageExcep.exceptionEntityNotFoundException(
                    id, PLANTAS_REPOSITORY, new RuntimeException(MessageConstraints.PLANT_NOT_FOUND));
            } catch (Exception e) {
                throw new RuntimeException(MessageConstraints.PLANT_NOT_FOUND, e);
            }
        }
        
        // Criar EmpresaDTO para PlantaDTO
        EmpresaDTO empresaDTO = new EmpresaDTO();
        if (planta.getEmpresa() != null) {
            empresaDTO.setCodigo(planta.getEmpresa());
        }
        
        // Retorna o DTO da planta
        PlantaDTO plantaDTO = new PlantaDTO(id);
        plantaDTO.setNome(planta.getNome());
        plantaDTO.setEmpresa(empresaDTO);
        
        return plantaDTO;
    }

    @Override
    public List<PlantaDTO> listarPlantas(Long empresaId) {
        String message = MessageConstraints.PLANT_LISTING;
        Map<String, Object> logParams = new HashMap<>();
        logParams.put("operation", "list");
        
        if (empresaId != null) {
            message = MessageFormatterUtil.format(message + MessageConstraints.PLANT_LISTING_BY_COMPANY, empresaId);
            logParams.put("companyId", empresaId);
        } else {
            message = MessageFormatterUtil.format(message, "");
            logParams.put("scope", "all");
        }
        
        LogManager.info(LOGGER, LogCategory.BUSINESS, message, logParams);
        
        List<Map.Entry<Long, PlantaModel>> listaEntradas = new ArrayList<>(plantas.entrySet());
        
        // Filtra por empresa se o ID foi fornecido
        return listaEntradas.stream()
                .filter(entry -> empresaId == null || entry.getValue().getEmpresa().equals(empresaId))
                .map(entry -> {
                    Long id = entry.getKey();
                    PlantaModel plantaModel = entry.getValue();
                    
                    // Criar EmpresaDTO para PlantaDTO
                    EmpresaDTO empresaDTO = new EmpresaDTO();
                    if (plantaModel.getEmpresa() != null) {
                        empresaDTO.setCodigo(plantaModel.getEmpresa());
                    }
                    
                    // Criar e configurar PlantaDTO
                    PlantaDTO plantaDTO = new PlantaDTO(id);
                    plantaDTO.setNome(plantaModel.getNome());
                    plantaDTO.setEmpresa(empresaDTO);
                    
                    return plantaDTO;
                })
                .toList();
    }

    @Override
    public boolean removerPlanta(Long id) {
        LogManager.info(LOGGER, LogCategory.BUSINESS,
            MessageFormatterUtil.format(MessageConstraints.PLANT_REMOVING, id),
            "plantId", id,
            "operation", "delete");
        
        if (!plantas.containsKey(id)) {
            LogManager.error(LOGGER, LogCategory.BUSINESS,
                MessageFormatterUtil.formatTemplate("plant.not.found.id", id),
                "plantId", id,
                "errorType", "EntityNotFoundException");
            try {
                throw CustomMessageExcep.exceptionEntityNotFoundException(
                    id, PLANTAS_REPOSITORY, new RuntimeException(MessageConstraints.PLANT_NOT_FOUND));
            } catch (Exception e) {
                throw new RuntimeException(MessageConstraints.PLANT_NOT_FOUND, e);
            }
        }
        
        plantas.remove(id);
        LogManager.info(LOGGER, LogCategory.BUSINESS,
            MessageFormatterUtil.format(MessageConstraints.PLANT_REMOVED_SUCCESS, id),
            "plantId", id,
            "status", "success");
        
        return true;
    }
}