package com.payhint.api.domain.billing.valueobjects;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;

import com.payhint.api.domain.billing.valueobject.PaymentId;
import com.payhint.api.domain.shared.valueobjects.EntityIdTest;

@DisplayName("PaymentId Value Object Tests")
class PaymentIdTest extends EntityIdTest<PaymentId> {

    @Override
    protected PaymentId createEntityId(UUID uuid) {
        return new PaymentId(uuid);
    }

    @Override
    protected PaymentId createEntityIdFromString(String id) {
        return PaymentId.fromString(id);
    }

    @Override
    protected String getEntityName() {
        return "PaymentId";
    }
}
