package com.payhint.api.domain.shared.valueobject;

import java.util.UUID;

import com.payhint.api.domain.shared.exception.InvalidPropertyException;

public interface EntityId {
    UUID value();

    default void validateNotNull(UUID value) {
        if (value == null) {
            throw new InvalidPropertyException("ID cannot be null");
        }
    }

    static UUID parseUUID(String id) {
        if (id == null) {
            throw new InvalidPropertyException("Invalid ID format: null");
        }
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidPropertyException("Invalid ID format: " + id);
        }
    }
}
