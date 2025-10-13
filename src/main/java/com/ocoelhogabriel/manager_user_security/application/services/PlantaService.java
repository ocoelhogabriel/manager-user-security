package com.ocoelhogabriel.manager_user_security.application.services;

import java.util.List;

import com.ocoelhogabriel.manager_user_security.domain.entities.PlantaModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.PlantaDTO;

/**
 * Interface que define os serviços para gerenciamento de plantas.
 */
public interface PlantaService {
    
    /**
     * Cria uma nova planta no sistema.
     *
     * @param plantaModel Dados da planta a ser criada
     * @return A planta criada
     */
    PlantaDTO criarPlanta(PlantaModel plantaModel);
    
    /**
     * Atualiza os dados de uma planta existente.
     *
     * @param id ID da planta a ser atualizada
     * @param plantaModel Novos dados da planta
     * @return A planta atualizada
     */
    PlantaDTO atualizarPlanta(Long id, PlantaModel plantaModel);
    
    /**
     * Busca uma planta pelo seu ID.
     *
     * @param id ID da planta a ser buscada
     * @return A planta encontrada ou null se não existir
     */
    PlantaDTO buscarPlantaPorId(Long id);
    
    /**
     * Lista todas as plantas no sistema, com opção de filtragem por empresa.
     *
     * @param empresaId ID da empresa para filtrar (opcional)
     * @return Lista de plantas
     */
    List<PlantaDTO> listarPlantas(Long empresaId);
    
    /**
     * Remove uma planta do sistema.
     *
     * @param id ID da planta a ser removida
     * @return true se a planta foi removida com sucesso, false caso contrário
     */
    boolean removerPlanta(Long id);
}