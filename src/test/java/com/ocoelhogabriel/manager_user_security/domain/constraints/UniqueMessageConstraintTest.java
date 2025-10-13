package com.ocoelhogabriel.manager_user_security.domain.constraints;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UniqueMessageConstraintTest {

    private UniqueMessageConstraint constraint;
    
    @BeforeEach
    void setUp() {
        constraint = UniqueMessageConstraint.getInstance(Duration.ofSeconds(2));
        constraint.clearCache();
    }
    
    @Test
    void shouldAcceptUniqueMessage() {
        assertTrue(constraint.canProcessMessage("Mensagem teste", "contexto1"));
    }
    
    @Test
    void shouldRejectDuplicateMessage() {
        assertTrue(constraint.canProcessMessage("Mensagem teste", "contexto1"));
        assertFalse(constraint.canProcessMessage("Mensagem teste", "contexto1"));
    }
    
    @Test
    void shouldDifferentiateByContext() {
        assertTrue(constraint.canProcessMessage("Mensagem teste", "contexto1"));
        assertTrue(constraint.canProcessMessage("Mensagem teste", "contexto2"));
    }
    
    @Test
    void shouldAcceptSameMessageAfterExpiration() {
        // Configurar clock inicial
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        constraint.setClock(fixedClock);
        
        // Primeira tentativa com a mensagem
        assertTrue(constraint.canProcessMessage("Mensagem expirável", "contexto"));
        
        // Avançar o clock em 3 segundos (após o tempo de expiração de 2 segundos)
        Clock advancedClock = Clock.fixed(
            Instant.now().plusSeconds(3),
            ZoneId.systemDefault()
        );
        constraint.setClock(advancedClock);
        
        // A mesma mensagem deve ser aceita após expiração
        assertTrue(constraint.canProcessMessage("Mensagem expirável", "contexto"));
    }
    
    @Test
    void shouldAcceptDifferentMessages() {
        assertTrue(constraint.canProcessMessage("Mensagem 1", "contexto"));
        assertTrue(constraint.canProcessMessage("Mensagem 2", "contexto"));
        assertTrue(constraint.canProcessMessage("Mensagem 3", "contexto"));
    }
    
    @Test
    void shouldThrowExceptionForEmptyMessage() {
        assertThrows(IllegalArgumentException.class, () -> {
            constraint.canProcessMessage("", "contexto");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            constraint.canProcessMessage(null, "contexto");
        });
    }
    
    @Test
    void shouldHandleNullContext() {
        assertTrue(constraint.canProcessMessage("Mensagem teste", null));
        assertFalse(constraint.canProcessMessage("Mensagem teste", null));
    }
    
    @Test
    void shouldCountCacheCorrectly() {
        constraint.clearCache();
        assertEquals(0, constraint.getCacheSize());
        
        constraint.canProcessMessage("Mensagem 1", "ctx1");
        assertEquals(1, constraint.getCacheSize());
        
        constraint.canProcessMessage("Mensagem 2", "ctx1");
        assertEquals(2, constraint.getCacheSize());
        
        constraint.canProcessMessage("Mensagem 1", "ctx2");
        assertEquals(3, constraint.getCacheSize());
    }
    
    @Test
    void shouldClearCache() {
        constraint.canProcessMessage("Mensagem 1", "ctx1");
        constraint.canProcessMessage("Mensagem 2", "ctx1");
        
        assertTrue(constraint.getCacheSize() > 0);
        
        constraint.clearCache();
        assertEquals(0, constraint.getCacheSize());
    }
}