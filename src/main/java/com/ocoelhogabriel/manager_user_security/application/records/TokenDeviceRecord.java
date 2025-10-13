package com.ocoelhogabriel.manager_user_security.application.records;

/**
 * Record para representar token de dispositivo
 * Aplica Object Calisthenics - Regra 3: Wrap all primitives and Strings
 * Moved to application.records package for better organization
 */
public record TokenDeviceRecord(String token, String device) {
    
    public TokenDeviceRecord {
        // Validação relaxada - permite null para tokens vazios
        if (token != null && token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be empty");
        }
        if (device != null && device.trim().isEmpty()) {
            throw new IllegalArgumentException("Device cannot be empty");
        }
    }
    
    public boolean isDeviceToken() {
        return device != null && !device.trim().isEmpty();
    }
    
    public boolean isUserToken() {
        return token != null && !token.trim().isEmpty() && (device == null || device.trim().isEmpty());
    }
    
    public boolean hasToken() {
        return token != null && !token.trim().isEmpty();
    }
    
    public boolean hasDevice() {
        return device != null && !device.trim().isEmpty();
    }
}
