package com.payhint.api.domain.billing.valueobject;

import java.util.UUID;

import com.payhint.api.domain.shared.valueobject.EntityId;

public record InstallmentId(UUID value) implements EntityId {
    public InstallmentId {
        validateNotNull(value);
    }

    public static InstallmentId fromString(String id) {
        return new InstallmentId(EntityId.parseUUID(id));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
