package com.ocoelhogabriel.manager_user_security.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ocoelhogabriel.manager_user_security.domain.entities.PlantaModel;

import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

/**
 * Testes unitários para PlantaModel
 * Aplica Clean Architecture - Domain Layer Testing
 * Testa validações e integridade da entidade PlantaModel
 */
@DisplayName("PlantaModel Tests")
class PlantaModelTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("Should create valid model successfully")
    void shouldCreateValidModelSuccessfully() {
        // Given
        final Long empresaId = 1L;
        final String nome = "Planta Teste";
        
        // When
        final PlantaModel model = new PlantaModel(empresaId, nome);
        final Set<ConstraintViolation<PlantaModel>> violations = validator.validate(model);
        
        // Then
        assertTrue(violations.isEmpty());
        assertEquals(empresaId, model.getEmpresa());
        assertEquals(nome, model.getNome());
    }
    
    @Test
    @DisplayName("Should create model with default constructor")
    void shouldCreateModelWithDefaultConstructor() {
        // When
        final PlantaModel model = new PlantaModel();
        
        // Then
        assertNull(model.getEmpresa());
        assertNull(model.getNome());
    }
    
    @Test
    @DisplayName("Should set and get properties correctly")
    void shouldSetAndGetPropertiesCorrectly() {
        // Given
        final PlantaModel model = new PlantaModel();
        final Long empresaId = 1L;
        final String nome = "Planta Teste";
        
        // When
        model.setEmpresa(empresaId);
        model.setNome(nome);
        
        // Then
        assertEquals(empresaId, model.getEmpresa());
        assertEquals(nome, model.getNome());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 100L, 9999L})
    @DisplayName("Should accept valid empresa values")
    void shouldAcceptValidEmpresaValues(Long empresaId) {
        // Given
        final PlantaModel model = new PlantaModel(empresaId, "Planta Teste");
        
        // When
        final Set<ConstraintViolation<PlantaModel>> violations = validator.validate(model);
        
        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation for null empresa")
    void shouldFailValidationForNullEmpresa() {
        // Given
        final PlantaModel model = new PlantaModel(null, "Planta Teste");
        
        // When
        final Set<ConstraintViolation<PlantaModel>> violations = validator.validate(model);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("empresa")));
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("obrigatório")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Planta 1", "Planta Teste", "Planta Muito Grande Com Nome Completo"})
    @DisplayName("Should accept valid nome values")
    void shouldAcceptValidNomeValues(String nome) {
        // Given
        final PlantaModel model = new PlantaModel(1L, nome);
        
        // When
        final Set<ConstraintViolation<PlantaModel>> violations = validator.validate(model);
        
        // Then
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("Should fail validation for invalid nome")
    void shouldFailValidationForInvalidNome(String nome) {
        // Given
        final PlantaModel model = new PlantaModel(1L, nome);
        
        // When
        final Set<ConstraintViolation<PlantaModel>> violations = validator.validate(model);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("branco") || v.getMessage().contains("obrigatório")));
    }
    
    @Test
    @DisplayName("Should implement toString method correctly")
    void shouldImplementToStringMethodCorrectly() {
        // Given
        final Long empresaId = 1L;
        final String nome = "Planta Teste";
        final PlantaModel model = new PlantaModel(empresaId, nome);
        
        // When
        final String result = model.toString();
        
        // Then
        assertTrue(result.contains("PlantaModel"));
        assertTrue(result.contains("empresa=" + empresaId));
        assertTrue(result.contains("nome=" + nome));
    }
    
    @Test
    @DisplayName("Should handle null values in toString")
    void shouldHandleNullValuesInToString() {
        // Given
        final PlantaModel model = new PlantaModel();
        
        // When
        final String result = model.toString();
        
        // Then
        assertTrue(result.contains("PlantaModel"));
        assertFalse(result.contains("empresa="));
        assertFalse(result.contains("nome="));
    }
}