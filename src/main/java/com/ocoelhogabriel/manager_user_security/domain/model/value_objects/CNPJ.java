package com.ocoelhogabriel.manager_user_security.domain.model.value_objects;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import java.util.Objects;

/**
 * Value Object to represent a Brazilian CNPJ.
 * It ensures that any CNPJ instance within the domain is always valid.
 */
public record CNPJ(String value) {

    private static final String FIELD_NAME = "cnpj";

    public CNPJ(final String value) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_REQUIRED_FIELD, FIELD_NAME));
        }

        final String cleanedCnpj = value.replaceAll("[^\\d]", "");

        if (cleanedCnpj.length() != 14 || !isValidChecksum(cleanedCnpj)) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_INVALID_FORMAT, FIELD_NAME));
        }
        this.value = cleanedCnpj;
    }

    private static boolean isValidChecksum(final String cnpj) {
        if (cnpj.chars().distinct().count() == 1) {
            return false; // Reject sequences of the same digit
        }

        try {
            final int[] digits = cnpj.chars().map(Character::getNumericValue).toArray();
            final int firstVerifier = calculateChecksum(digits, 12, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
            if (digits[12] != firstVerifier) {
                return false;
            }
            final int secondVerifier = calculateChecksum(digits, 13, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
            return digits[13] == secondVerifier;
        } catch (Exception e) {
            return false;
        }
    }

    private static int calculateChecksum(final int[] digits, final int length, final int[] weights) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += digits[i] * weights[i];
        }
        final int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }
}
