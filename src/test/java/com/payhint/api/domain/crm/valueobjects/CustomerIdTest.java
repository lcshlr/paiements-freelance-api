package com.payhint.api.domain.crm.valueobjects;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;

import com.payhint.api.domain.crm.valueobject.CustomerId;
import com.payhint.api.domain.shared.valueobjects.EntityIdTest;

@DisplayName("CustomerId Value Object Tests")
class CustomerIdTest extends EntityIdTest<CustomerId> {

    @Override
    protected CustomerId createEntityId(UUID uuid) {
        return new CustomerId(uuid);
    }

    @Override
    protected CustomerId createEntityIdFromString(String id) {
        return CustomerId.fromString(id);
    }

    @Override
    protected String getEntityName() {
        return "CustomerId";
    }
}
