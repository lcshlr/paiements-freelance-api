package com.payhint.api.application.billing.dto.request;

public record CreateInvoiceRequest(String customerId, double amountDue, String dueDate) {
}
