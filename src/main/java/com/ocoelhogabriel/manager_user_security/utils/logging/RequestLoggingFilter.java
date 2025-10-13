package com.ocoelhogabriel.manager_user_security.utils.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtro para inicializar o contexto de log para cada requisição HTTP.
 * Captura informações como ID da requisição, usuário, sessão, etc.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                   @NonNull HttpServletResponse response,
                                   @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // Inicializar o contexto de rastreamento
            String traceId = LogContext.initTraceContext();
            
            // Extrair ID de correlação do cabeçalho, se existir
            String correlationId = request.getHeader("X-Correlation-ID");
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }
            LogContext.put("correlationId", correlationId);
            
            // Adicionar informações da requisição ao contexto
            LogContext.put("method", request.getMethod());
            LogContext.put("path", request.getRequestURI());
            LogContext.put("userAgent", request.getHeader("User-Agent"));
            LogContext.put("clientIP", getClientIP(request));
            
            // Obter informações do usuário autenticado, se disponível
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                LogContext.setUserId(auth.getName());
            }
            
            // Obter ID da sessão, se existir
            HttpSession session = request.getSession(false);
            if (session != null) {
                LogContext.setSessionId(session.getId());
            }
            
            // Log da requisição
            LogManager.info(LOGGER, LogCategory.API, 
                "Requisição HTTP recebida", 
                "method", request.getMethod(),
                "path", request.getRequestURI(),
                "traceId", traceId);
            
            // Definir ID de rastreamento na resposta para correlação client-side
            response.setHeader("X-Trace-ID", traceId);
            response.setHeader("X-Correlation-ID", correlationId);
            
            // Continuar com a cadeia de filtros
            filterChain.doFilter(request, response);
        } finally {
            // Log da finalização da requisição
            LogManager.info(LOGGER, LogCategory.API, 
                "Requisição HTTP finalizada", 
                "method", LogContext.get("method"),
                "path", LogContext.get("path"),
                "status", response.getStatus());
            
            // Limpar o contexto ao final da requisição
            LogContext.clear();
        }
    }
    
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Se houver múltiplos IPs, pega o primeiro (cliente original)
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}