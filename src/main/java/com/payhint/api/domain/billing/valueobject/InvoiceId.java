package com.payhint.api.domain.billing.valueobject;

import java.util.UUID;

import com.payhint.api.domain.shared.valueobject.EntityId;

public record InvoiceId(UUID value) implements EntityId {
    public InvoiceId {
        validateNotNull(value);
    }

    public static InvoiceId fromString(String id) {
        return new InvoiceId(EntityId.parseUUID(id));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
