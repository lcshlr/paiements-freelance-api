package com.payhint.api.domain.billing.valueobjects;

public record InvoiceReference(String value) {
    public InvoiceReference {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("InvoiceReference value cannot be null or blank.");
        }
    }
}
