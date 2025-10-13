package com.ocoelhogabriel.manager_user_security.utils.logging;

/**
 * Enum para definir o nível de severidade do log.
 * Usado para adicionar contexto estruturado aos logs.
 */
public enum LogSeverity {
    DEBUG("debug"),
    INFO("info"),
    WARN("warn"),
    ERROR("error"),
    FATAL("fatal");

    private final String value;

    LogSeverity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}