package com.ocoelhogabriel.manager_user_security.domain.valueobject;

/**
 * Value object representing details of a generated token.
 */
public record TokenDetails(
    String username, 
    String token, 
    String issuedAt,
    String expiryIn
) {
}