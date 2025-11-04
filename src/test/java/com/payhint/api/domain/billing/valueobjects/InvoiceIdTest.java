package com.payhint.api.domain.billing.valueobjects;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;

import com.payhint.api.domain.shared.valueobjects.EntityIdTest;

@DisplayName("InvoiceId Value Object Tests")
class InvoiceIdTest extends EntityIdTest<InvoiceId> {

    @Override
    protected InvoiceId createEntityId(UUID uuid) {
        return new InvoiceId(uuid);
    }

    @Override
    protected InvoiceId createEntityIdFromString(String id) {
        return InvoiceId.fromString(id);
    }

    @Override
    protected String getEntityName() {
        return "InvoiceId";
    }
}
