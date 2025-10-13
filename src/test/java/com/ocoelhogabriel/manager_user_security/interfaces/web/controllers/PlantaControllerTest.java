package com.ocoelhogabriel.manager_user_security.interfaces.web.controllers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ocoelhogabriel.manager_user_security.application.services.PlantaService;
import com.ocoelhogabriel.manager_user_security.domain.entities.PlantaModel;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.EmpresaDTO;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.PlantaDTO;

/**
 * Unit tests for PlantaController
 * Applies Clean Architecture - Interface Layer Testing
 * Tests REST endpoints and service interactions
 */
@DisplayName("PlantaController Tests")
class PlantaControllerTest {

    @Mock
    private PlantaService plantaService;

    private PlantaController plantaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        plantaController = new PlantaController(plantaService);
    }

    @Test
    @DisplayName("Should create plant successfully")
    void shouldCreatePlantSuccessfully() {
        // Given
        PlantaModel requestModel = new PlantaModel(1L, "Planta Teste");

        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setCodigo(1L);

        PlantaDTO responseDTO = new PlantaDTO(1L);
        responseDTO.setNome("Planta Teste");
        responseDTO.setEmpresa(empresaDTO);

        when(plantaService.criarPlanta(any(PlantaModel.class))).thenReturn(responseDTO);

        // When
        ResponseEntity<PlantaDTO> response = plantaController.criarPlanta(requestModel);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Planta Teste", response.getBody().getNome());
        assertEquals(1L, response.getBody().getEmpresa().getCodigo());

        // Verify interactions
        verify(plantaService).criarPlanta(requestModel);
    }

    @Test
    @DisplayName("Should handle exception when creating plant")
    void shouldHandleExceptionWhenCreatingPlant() {
        // Given
        PlantaModel requestModel = new PlantaModel(1L, "Planta Teste");

        when(plantaService.criarPlanta(any(PlantaModel.class)))
                .thenThrow(new RuntimeException("Error creating plant"));

        // When
        ResponseEntity<PlantaDTO> response = plantaController.criarPlanta(requestModel);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        // Verify interactions
        verify(plantaService).criarPlanta(requestModel);
    }

    @Test
    @DisplayName("Should find plant by code successfully")
    void shouldFindPlantByCodeSuccessfully() {
        // Given
        Long codigo = 1L;

        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setCodigo(1L);

        PlantaDTO responseDTO = new PlantaDTO(codigo);
        responseDTO.setNome("Planta Teste");
        responseDTO.setEmpresa(empresaDTO);

        when(plantaService.buscarPlantaPorId(codigo)).thenReturn(responseDTO);

        // When
        ResponseEntity<PlantaDTO> response = plantaController.buscarPlantaPorCodigo(codigo);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Planta Teste", response.getBody().getNome());

        // Verify interactions
        verify(plantaService).buscarPlantaPorId(codigo);
    }

    @Test
    @DisplayName("Should return not found when plant does not exist")
    void shouldReturnNotFoundWhenPlantDoesNotExist() {
        // Given
        Long codigo = 999L;

        when(plantaService.buscarPlantaPorId(codigo))
                .thenThrow(new RuntimeException("Plant not found"));

        // When
        ResponseEntity<PlantaDTO> response = plantaController.buscarPlantaPorCodigo(codigo);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        // Verify interactions
        verify(plantaService).buscarPlantaPorId(codigo);
    }

    @Test
    @DisplayName("Should list all plants successfully")
    void shouldListAllPlantsSuccessfully() {
        // Given
        List<PlantaDTO> plantas = new ArrayList<>();

        EmpresaDTO empresaDTO1 = new EmpresaDTO();
        empresaDTO1.setCodigo(1L);

        PlantaDTO planta1 = new PlantaDTO(1L);
        planta1.setNome("Planta 1");
        planta1.setEmpresa(empresaDTO1);

        EmpresaDTO empresaDTO2 = new EmpresaDTO();
        empresaDTO2.setCodigo(2L);

        PlantaDTO planta2 = new PlantaDTO(2L);
        planta2.setNome("Planta 2");
        planta2.setEmpresa(empresaDTO2);

        plantas.add(planta1);
        plantas.add(planta2);

        when(plantaService.listarPlantas(null)).thenReturn(plantas);

        // When
        ResponseEntity<List<PlantaDTO>> response = plantaController.buscarListarPlanta();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Planta 1", response.getBody().get(0).getNome());
        assertEquals("Planta 2", response.getBody().get(1).getNome());

        // Verify interactions
        verify(plantaService).listarPlantas(null);
    }

    @Test
    @DisplayName("Should handle exception when listing plants")
    void shouldHandleExceptionWhenListingPlants() {
        // Given
        when(plantaService.listarPlantas(null))
                .thenThrow(new RuntimeException("Error listing plants"));

        // When
        ResponseEntity<List<PlantaDTO>> response = plantaController.buscarListarPlanta();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        // Verify interactions
        verify(plantaService).listarPlantas(null);
    }

    @Test
    @DisplayName("Should list plants with pagination successfully")
    void shouldListPlantsWithPaginationSuccessfully() {
        // Given
        Integer pagina = 0;
        Integer tamanho = 10;
        String nome = "Planta";
        Long empresaId = 1L;

        List<PlantaDTO> plantas = new ArrayList<>();

        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setCodigo(1L);

        PlantaDTO planta1 = new PlantaDTO(1L);
        planta1.setNome("Planta 1");
        planta1.setEmpresa(empresaDTO);

        PlantaDTO planta2 = new PlantaDTO(2L);
        planta2.setNome("Planta 2");
        planta2.setEmpresa(empresaDTO);

        plantas.add(planta1);
        plantas.add(planta2);

        when(plantaService.listarPlantas(empresaId)).thenReturn(plantas);

        // When
        ResponseEntity<Page<PlantaDTO>> response = plantaController.buscarPlantaPaginado(
                pagina, tamanho, nome, empresaId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        assertEquals(2, response.getBody().getContent().size());

        // Verify interactions
        verify(plantaService).listarPlantas(empresaId);
    }

    @Test
    @DisplayName("Should filter plants by name successfully")
    void shouldFilterPlantsByNameSuccessfully() {
        // Given
        Integer pagina = 0;
        Integer tamanho = 10;
        String nome = "Planta 1";
        Long empresaId = 1L;

        List<PlantaDTO> plantas = new ArrayList<>();

        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setCodigo(1L);

        PlantaDTO planta1 = new PlantaDTO(1L);
        planta1.setNome("Planta 1");
        planta1.setEmpresa(empresaDTO);

        PlantaDTO planta2 = new PlantaDTO(2L);
        planta2.setNome("Planta 2");
        planta2.setEmpresa(empresaDTO);

        plantas.add(planta1);
        plantas.add(planta2);

        when(plantaService.listarPlantas(empresaId)).thenReturn(plantas);

        // When
        ResponseEntity<Page<PlantaDTO>> response = plantaController.buscarPlantaPaginado(
                pagina, tamanho, nome, empresaId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("Planta 1", response.getBody().getContent().get(0).getNome());

        // Verify interactions
        verify(plantaService).listarPlantas(empresaId);
    }

    @Test
    @DisplayName("Should handle exception when listing plants with pagination")
    void shouldHandleExceptionWhenListingPlantsWithPagination() {
        // Given
        Integer pagina = 0;
        Integer tamanho = 10;
        String nome = null;
        Long empresaId = 1L;

        when(plantaService.listarPlantas(empresaId))
                .thenThrow(new RuntimeException("Error listing plants"));

        // When
        ResponseEntity<Page<PlantaDTO>> response = plantaController.buscarPlantaPaginado(
                pagina, tamanho, nome, empresaId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        // Verify interactions
        verify(plantaService).listarPlantas(empresaId);
    }

    @Test
    @DisplayName("Should update plant successfully")
    void shouldUpdatePlantSuccessfully() {
        // Given
        Long codigo = 1L;
        PlantaModel requestModel = new PlantaModel(1L, "Planta Atualizada");

        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setCodigo(1L);

        PlantaDTO responseDTO = new PlantaDTO(codigo);
        responseDTO.setNome("Planta Atualizada");
        responseDTO.setEmpresa(empresaDTO);

        when(plantaService.atualizarPlanta(codigo, requestModel)).thenReturn(responseDTO);

        // When
        ResponseEntity<PlantaDTO> response = plantaController.atualizarPlanta(codigo, requestModel);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Planta Atualizada", response.getBody().getNome());

        // Verify interactions
        verify(plantaService).atualizarPlanta(codigo, requestModel);
    }

    @Test
    @DisplayName("Should return not found when updating non-existent plant")
    void shouldReturnNotFoundWhenUpdatingNonExistentPlant() {
        // Given
        Long codigo = 999L;
        PlantaModel requestModel = new PlantaModel(1L, "Non-existent Plant");

        RuntimeException exception = new RuntimeException("Plant not found");
        when(plantaService.atualizarPlanta(codigo, requestModel)).thenThrow(exception);

        // When
        ResponseEntity<PlantaDTO> response = plantaController.atualizarPlanta(codigo, requestModel);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        // Verify interactions
        verify(plantaService).atualizarPlanta(codigo, requestModel);
    }

    @Test
    @DisplayName("Should handle general exception when updating plant")
    void shouldHandleGeneralExceptionWhenUpdatingPlant() {
        // Given
        Long codigo = 1L;
        PlantaModel requestModel = new PlantaModel(1L, "Planta Problema");

        RuntimeException exception = new RuntimeException("General error while updating");
        when(plantaService.atualizarPlanta(codigo, requestModel)).thenThrow(exception);

        // When
        ResponseEntity<PlantaDTO> response = plantaController.atualizarPlanta(codigo, requestModel);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        // Verify interactions
        verify(plantaService).atualizarPlanta(codigo, requestModel);
    }

    @Test
    @DisplayName("Should delete plant successfully")
    void shouldDeletePlantSuccessfully() {
        // Given
        Long codigo = 1L;

        when(plantaService.removerPlanta(codigo)).thenReturn(true);

        // When
        ResponseEntity<Void> response = plantaController.deletarPlanta(codigo);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        // Verify interactions
        verify(plantaService).removerPlanta(codigo);
    }

    @Test
    @DisplayName("Should return not found when deleting non-existent plant")
    void shouldReturnNotFoundWhenDeletingNonExistentPlant() {
        // Given
        Long codigo = 999L;

        RuntimeException exception = new RuntimeException("Plant not found");
        when(plantaService.removerPlanta(codigo)).thenThrow(exception);

        // When
        ResponseEntity<Void> response = plantaController.deletarPlanta(codigo);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        // Verify interactions
        verify(plantaService).removerPlanta(codigo);
    }

    @Test
    @DisplayName("Should handle general exception when deleting plant")
    void shouldHandleGeneralExceptionWhenDeletingPlant() {
        // Given
        Long codigo = 1L;

        RuntimeException exception = new RuntimeException("Internal error");
        when(plantaService.removerPlanta(codigo)).thenThrow(exception);

        // When
        ResponseEntity<Void> response = plantaController.deletarPlanta(codigo);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        // Verify interactions
        verify(plantaService).removerPlanta(codigo);
    }
}