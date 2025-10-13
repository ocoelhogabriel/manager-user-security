package com.ocoelhogabriel.manager_user_security.interfaces.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for system-level information endpoints.
 */
@RestController
@RequestMapping("/api/system/v1")
public class SystemController {

    @Value("${spring.application.name:user-security}")
    private String applicationName;

    @Value("${spring.application.version:1.0.0}")
    private String applicationVersion;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    /**
     * Get information about the running application.
     *
     * @return a map containing application information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", applicationName);
        info.put("version", applicationVersion);
        info.put("profile", activeProfile);
        info.put("status", "UP");
        
        return ResponseEntity.ok(info);
    }
    
    /**
     * Simple health check endpoint.
     *
     * @return a simple status message
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
