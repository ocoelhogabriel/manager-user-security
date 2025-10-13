package com.ocoelhogabriel.manager_user_security.domain.services;

/**
 * Interface para Strategy Pattern de Autenticação
 * Aplica Interface Segregation Principle (ISP)
 * Permite diferentes estratégias de autenticação (JWT, Basic, OAuth, etc.)
 */
public interface AuthenticationStrategy {
    
    /**
     * Autentica um usuário baseado na estratégia implementada
     * 
     * @param credentials credenciais de autenticação
     * @return token de autenticação se bem-sucedida
     * @throws AuthenticationException se a autenticação falhar
     */
    String authenticate(String credentials);
    
    /**
     * Valida um token de autenticação
     * 
     * @param token token a ser validado
     * @return true se o token for válido
     */
    boolean validateToken(String token);
    
    /**
     * Extrai informações do usuário do token
     * 
     * @param token token de autenticação
     * @return informações do usuário
     */
    String extractUserInfo(String token);
    
    /**
     * Valida as credenciais de um usuário
     * 
     * @param username nome de usuário
     * @param password senha do usuário
     * @return true se as credenciais são válidas
     */
    boolean validateCredentials(String username, String password);
}
