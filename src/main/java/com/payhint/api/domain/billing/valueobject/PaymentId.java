package com.payhint.api.domain.billing.valueobject;

import java.util.UUID;

import com.payhint.api.domain.shared.valueobject.EntityId;

public record PaymentId(UUID value) implements EntityId {
    public PaymentId {
        validateNotNull(value);
    }

    public static PaymentId fromString(String id) {
        return new PaymentId(EntityId.parseUUID(id));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
