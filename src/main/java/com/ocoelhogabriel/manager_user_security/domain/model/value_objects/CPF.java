package com.ocoelhogabriel.manager_user_security.domain.model.value_objects;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object to represent a Brazilian CPF.
 * It ensures that any CPF instance within the domain is always valid.
 */
public record CPF(String value) {

    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");
    private static final String FIELD_NAME = "cpf";

    public CPF(final String value) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_REQUIRED_FIELD, FIELD_NAME));
        }

        final String cleanedCpf = value.replaceAll("[^\\d]", "");

        if (!CPF_PATTERN.matcher(cleanedCpf).matches() || isInvalidSequence(cleanedCpf) || !isValidChecksum(cleanedCpf)) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_INVALID_FORMAT, FIELD_NAME));
        }
        this.value = cleanedCpf;
    }

    private static boolean isInvalidSequence(final String cpf) {
        return cpf.chars().distinct().count() == 1;
    }

    private static boolean isValidChecksum(final String cpf) {
        try {
            final int[] digits = cpf.chars().map(Character::getNumericValue).toArray();
            final int firstVerifier = calculateChecksum(digits, 9);
            if (digits[9] != firstVerifier) {
                return false;
            }
            final int secondVerifier = calculateChecksum(digits, 10);
            return digits[10] == secondVerifier;
        } catch (Exception e) {
            return false;
        }
    }

    private static int calculateChecksum(final int[] digits, final int length) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += digits[i] * (length + 1 - i);
        }
        final int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }
}
