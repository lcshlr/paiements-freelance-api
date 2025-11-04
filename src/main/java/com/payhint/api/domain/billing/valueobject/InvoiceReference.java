package com.payhint.api.domain.billing.valueobject;

public record InvoiceReference(String value) {
    public InvoiceReference {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("InvoiceReference value cannot be null or blank.");
        }
    }
}
