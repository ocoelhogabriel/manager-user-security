package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.services;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ocoelhogabriel.manager_user_security.application.services.EmpresaService;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.entities.EmpresaModel;
import com.ocoelhogabriel.manager_user_security.domain.entities.PlantaModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.PlantaDTO;

/**
 * Unit tests for PlantaServiceImpl
 * Applies Clean Architecture - Infrastructure Layer Testing
 * Tests concrete service implementation
 */
@DisplayName("PlantaServiceImpl Tests")
class PlantaServiceImplTest {

    @Mock
    private EmpresaService empresaService;

    @InjectMocks
    private PlantaServiceImpl plantaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // We cannot mock static methods of MessageFormatterUtil with standard Mockito
        // We just initialize our mocks and use real values
    }

    @Test
    @DisplayName("Should create plant successfully")
    void shouldCreatePlantSuccessfully() {
        // Given
        Long empresaId = 1L;
        String nomePlanta = "Planta Teste";

        PlantaModel plantaModel = new PlantaModel(empresaId, nomePlanta);

        EmpresaModel empresaModel = new EmpresaModel(12345678901L, "Empresa Teste", "Empresa", "12345678");

        when(empresaService.findById(empresaId)).thenReturn(empresaModel);

        // When
        PlantaDTO result = plantaService.criarPlanta(plantaModel);

        // Then
        assertNotNull(result);
        assertEquals(nomePlanta, result.getNome());
        assertNotNull(result.getEmpresa());
        assertEquals(empresaId, result.getEmpresa().getCodigo());

        // Verify interactions
        verify(empresaService).findById(empresaId);
    }

    @Test
    @DisplayName("Should throw exception when creating plant with existing name")
    void shouldThrowExceptionWhenCreatingPlantWithExistingName() {
        // Given
        Long empresaId = 1L;
        String nomePlanta = "Planta Existente";

        // First create a plant
        PlantaModel firstPlanta = new PlantaModel(empresaId, nomePlanta);
        EmpresaModel empresaModel = new EmpresaModel(12345678901L, "Empresa Teste", "Empresa", "12345678");
        when(empresaService.findById(empresaId)).thenReturn(empresaModel);
        plantaService.criarPlanta(firstPlanta);

        // Try to create another with the same name
        PlantaModel duplicatePlanta = new PlantaModel(empresaId, nomePlanta);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            plantaService.criarPlanta(duplicatePlanta);
        });

        assertEquals(MessageConstraints.PLANT_NAME_EXISTS, exception.getMessage());

        // Verify interactions
        verify(empresaService, times(1)).findById(empresaId); // Only called for the first creation
    }

    @Test
    @DisplayName("Should throw exception when creating plant with non-existent empresa")
    void shouldThrowExceptionWhenCreatingPlantWithNonExistentEmpresa() {
        // Given
        Long empresaId = 999L;
        String nomePlanta = "Planta Teste";

        PlantaModel plantaModel = new PlantaModel(empresaId, nomePlanta);

        when(empresaService.findById(empresaId)).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            plantaService.criarPlanta(plantaModel);
        });

        assertEquals(MessageConstraints.ENTERPRISE_NOT_FOUND, exception.getMessage());

        // Verify interactions
        verify(empresaService).findById(empresaId);
    }

    @Test
    @DisplayName("Should update plant successfully")
    void shouldUpdatePlantSuccessfully() {
        // Given
        Long plantaId = 1L;
        Long empresaId = 1L;
        String nomeOriginal = "Planta Original";
        String nomeAtualizado = "Planta Atualizada";

        // Primeiro cria uma planta
        PlantaModel plantaOriginal = new PlantaModel(empresaId, nomeOriginal);
        EmpresaModel empresaModel = new EmpresaModel(12345678901L, "Empresa Teste", "Empresa", "12345678");
        when(empresaService.findById(empresaId)).thenReturn(empresaModel);
        plantaService.criarPlanta(plantaOriginal);

        // Agora atualiza a planta
        PlantaModel plantaAtualizada = new PlantaModel(empresaId, nomeAtualizado);

        // When
        PlantaDTO result = plantaService.atualizarPlanta(plantaId, plantaAtualizada);

        // Then
        assertNotNull(result);
        assertEquals(nomeAtualizado, result.getNome());
        assertNotNull(result.getEmpresa());
        assertEquals(empresaId, result.getEmpresa().getCodigo());

        // Verify interactions
        verify(empresaService, times(2)).findById(empresaId); // Chamado na criação e na atualização
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent plant")
    void shouldThrowExceptionWhenUpdatingNonExistentPlant() {
        // Given
        Long plantaId = 999L;
        Long empresaId = 1L;
        String nomePlanta = "Planta Inexistente";

        PlantaModel plantaModel = new PlantaModel(empresaId, nomePlanta);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            plantaService.atualizarPlanta(plantaId, plantaModel);
        });

        assertEquals(MessageConstraints.PLANT_NOT_FOUND, exception.getMessage());

        // Verify interactions
        verify(empresaService, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw exception when updating plant with existing name")
    void shouldThrowExceptionWhenUpdatingPlantWithExistingName() {
        // Given
        Long plantaId2 = 2L;
        Long empresaId = 1L;
        String nomePlanta1 = "Planta Um";
        String nomePlanta2 = "Planta Dois";

        // Cria duas plantas
        PlantaModel plantaModel1 = new PlantaModel(empresaId, nomePlanta1);
        PlantaModel plantaModel2 = new PlantaModel(empresaId, nomePlanta2);

        EmpresaModel empresaModel = new EmpresaModel(12345678901L, "Empresa Teste", "Empresa", "12345678");
        when(empresaService.findById(empresaId)).thenReturn(empresaModel);

        plantaService.criarPlanta(plantaModel1);
        plantaService.criarPlanta(plantaModel2);

        // Tenta atualizar a segunda planta para ter o mesmo nome da primeira
        PlantaModel plantaAtualizada = new PlantaModel(empresaId, nomePlanta1);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            plantaService.atualizarPlanta(plantaId2, plantaAtualizada);
        });

        assertEquals(MessageConstraints.PLANT_NAME_EXISTS_OTHER, exception.getMessage());

        // Verify interactions
        verify(empresaService, times(2)).findById(empresaId); // Chamado apenas nas criações
    }

    @Test
    @DisplayName("Should find plant by id successfully")
    void shouldFindPlantByIdSuccessfully() {
        // Given
        Long plantaId = 1L;
        Long empresaId = 1L;
        String nomePlanta = "Planta Teste";

        // Primeiro cria uma planta
        PlantaModel plantaModel = new PlantaModel(empresaId, nomePlanta);
        EmpresaModel empresaModel = new EmpresaModel(12345678901L, "Empresa Teste", "Empresa", "12345678");
        when(empresaService.findById(empresaId)).thenReturn(empresaModel);
        plantaService.criarPlanta(plantaModel);

        // When
        PlantaDTO result = plantaService.buscarPlantaPorId(plantaId);

        // Then
        assertNotNull(result);
        assertEquals(nomePlanta, result.getNome());
        assertNotNull(result.getEmpresa());
        assertEquals(empresaId, result.getEmpresa().getCodigo());
    }

    @Test
    @DisplayName("Should throw exception when finding non-existent plant")
    void shouldThrowExceptionWhenFindingNonExistentPlant() {
        // Given
        Long plantaId = 999L;

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            plantaService.buscarPlantaPorId(plantaId);
        });

        assertEquals(MessageConstraints.PLANT_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Should list all plants successfully")
    void shouldListAllPlantsSuccessfully() {
        // Given
        Long empresaId = 1L;

        // Cria algumas plantas
        PlantaModel plantaModel1 = new PlantaModel(empresaId, "Planta Um");
        PlantaModel plantaModel2 = new PlantaModel(empresaId, "Planta Dois");

        EmpresaModel empresaModel = new EmpresaModel(12345678901L, "Empresa Teste", "Empresa", "12345678");
        when(empresaService.findById(empresaId)).thenReturn(empresaModel);

        plantaService.criarPlanta(plantaModel1);
        plantaService.criarPlanta(plantaModel2);

        // When
        List<PlantaDTO> result = plantaService.listarPlantas(null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should list plants filtered by empresa successfully")
    void shouldListPlantsFilteredByEmpresaSuccessfully() {
        // Given
        Long empresaId1 = 1L;
        Long empresaId2 = 2L;

        // Cria plantas para duas empresas diferentes
        PlantaModel plantaEmpresa1Primeira = new PlantaModel(empresaId1, "Planta Um - Empresa 1");
        PlantaModel plantaEmpresa1Segunda = new PlantaModel(empresaId1, "Planta Dois - Empresa 1");
        PlantaModel plantaEmpresa2 = new PlantaModel(empresaId2, "Planta Um - Empresa 2");

        EmpresaModel empresaModel1 = new EmpresaModel(12345678901L, "Empresa Um", "Empresa 1", "12345678");
        EmpresaModel empresaModel2 = new EmpresaModel(98765432101L, "Empresa Dois", "Empresa 2", "87654321");

        when(empresaService.findById(empresaId1)).thenReturn(empresaModel1);
        when(empresaService.findById(empresaId2)).thenReturn(empresaModel2);

        plantaService.criarPlanta(plantaEmpresa1Primeira);
        plantaService.criarPlanta(plantaEmpresa1Segunda);
        plantaService.criarPlanta(plantaEmpresa2);

        // When
        List<PlantaDTO> result = plantaService.listarPlantas(empresaId1);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        for (PlantaDTO planta : result) {
            assertEquals(empresaId1, planta.getEmpresa().getCodigo());
        }
    }

    @Test
    @DisplayName("Should remove plant successfully")
    void shouldRemovePlantSuccessfully() {
        // Given
        Long plantaId = 1L;
        Long empresaId = 1L;
        String nomePlanta = "Planta para Remover";

        // Primeiro cria uma planta
        PlantaModel plantaModel = new PlantaModel(empresaId, nomePlanta);
        EmpresaModel empresaModel = new EmpresaModel(12345678901L, "Empresa Teste", "Empresa", "12345678");
        when(empresaService.findById(empresaId)).thenReturn(empresaModel);
        plantaService.criarPlanta(plantaModel);

        // When
        boolean result = plantaService.removerPlanta(plantaId);

        // Then
        assertTrue(result);

        // Verify that the plant no longer exists
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            plantaService.buscarPlantaPorId(plantaId);
        });

        assertEquals(MessageConstraints.PLANT_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent plant")
    void shouldThrowExceptionWhenRemovingNonExistentPlant() {
        // Given
        Long plantaId = 999L;

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            plantaService.removerPlanta(plantaId);
        });

        assertEquals(MessageConstraints.PLANT_NOT_FOUND, exception.getMessage());
    }
}
