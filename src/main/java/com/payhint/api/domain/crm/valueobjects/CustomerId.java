package com.payhint.api.domain.crm.valueobjects;

import java.util.UUID;

import com.payhint.api.domain.shared.valueobjects.EntityId;

public record CustomerId(UUID value) implements EntityId {

    public CustomerId {
        validateNotNull(value);
    }

    public static CustomerId fromString(String id) {
        return new CustomerId(EntityId.parseUUID(id));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
