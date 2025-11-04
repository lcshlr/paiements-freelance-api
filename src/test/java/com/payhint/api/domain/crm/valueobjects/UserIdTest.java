package com.payhint.api.domain.crm.valueobjects;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;

import com.payhint.api.domain.shared.valueobjects.EntityIdTest;

@DisplayName("UserId Value Object Tests")
class UserIdTest extends EntityIdTest<UserId> {

    @Override
    protected UserId createEntityId(UUID uuid) {
        return new UserId(uuid);
    }

    @Override
    protected UserId createEntityIdFromString(String id) {
        return UserId.fromString(id);
    }

    @Override
    protected String getEntityName() {
        return "UserId";
    }
}
