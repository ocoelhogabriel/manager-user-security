package com.ocoelhogabriel.manager_user_security.application.dto;

import java.time.LocalDateTime;

/**
 * Data transfer object for token details.
 * Using a record for an immutable data carrier.
 */
public record TokenDetails(
        String username,
        String token,
        LocalDateTime issuedAt,
        LocalDateTime expiresAt) {
}
