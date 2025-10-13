package com.ocoelhogabriel.manager_user_security.utils.logging;

/**
 * Enum para categorizar logs por área funcional ou domínio,
 * facilitando a filtragem e análise dos logs.
 */
public enum LogCategory {
    SECURITY("security"),
    AUTHENTICATION("authentication"),
    AUTHORIZATION("authorization"),
    DATABASE("database"),
    API("api"),
    BUSINESS("business"),
    PERFORMANCE("performance"),
    INTEGRATION("integration"),
    AUDIT("audit"),
    SYSTEM("system");

    private final String value;

    LogCategory(String value) {
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