package com.ocoelhogabriel.manager_user_security.domain.constraints;

/**
 * Classe que contém as chaves de todos os templates de mensagens utilizados na aplicação.
 * Esta classe é utilizada em conjunto com MessageConstraints para acessar mensagens parametrizadas.
 */
public final class MessageTemplateKeys {
    
    // Chaves de templates para entidades
    public static final String PLANT_NOT_FOUND_ID = "plant.not.found.id";
    public static final String USER_NOT_FOUND_ID = "user.not.found.id";
    public static final String ENTERPRISE_NOT_FOUND_ID = "enterprise.not.found.id";
    public static final String RESOURCE_NOT_FOUND_ID = "resource.not.found.id";
    public static final String PROFILE_NOT_FOUND_ID = "profile.not.found.id";
    public static final String PROFILE_NOT_FOUND_NAME = "profile.not.found.name";
    public static final String ENTITY_GENERIC = "entity.not.found.id";
    
    // Chaves de templates para autenticação
    public static final String AUTH_USER_NOT_FOUND = "auth.user.not.found";
    public static final String AUTH_TOKEN_VALIDATION_ERROR = "auth.token.validation.error";
    public static final String AUTH_TOKEN_REFRESH_ERROR = "auth.token.refresh.error";
    public static final String AUTH_LOGIN_ERROR = "auth.login.error";
    
    // Chaves de templates para logs
    public static final String LOG_OPERATION_START = "log.operation.start";
    public static final String LOG_OPERATION_END = "log.operation.end";
    public static final String LOG_OPERATION_ERROR = "log.operation.error";
    
    // Construtor privado para evitar instanciação
    private MessageTemplateKeys() {}
}