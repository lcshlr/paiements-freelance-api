package com.payhint.api.domain.crm.valueobject;

import java.util.UUID;

import com.payhint.api.domain.shared.valueobject.EntityId;

public record UserId(UUID value) implements EntityId {

    public UserId {
        validateNotNull(value);
    }

    public static UserId fromString(String id) {
        return new UserId(EntityId.parseUUID(id));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
