package com.payhint.api.domain.billing.valueobjects;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;

import com.payhint.api.domain.billing.valueobject.InstallmentId;
import com.payhint.api.domain.shared.valueobjects.EntityIdTest;

@DisplayName("InstallmentId Value Object Tests")
class InstallmentIdTest extends EntityIdTest<InstallmentId> {

    @Override
    protected InstallmentId createEntityId(UUID uuid) {
        return new InstallmentId(uuid);
    }

    @Override
    protected InstallmentId createEntityIdFromString(String id) {
        return InstallmentId.fromString(id);
    }

    @Override
    protected String getEntityName() {
        return "InstallmentId";
    }
}
