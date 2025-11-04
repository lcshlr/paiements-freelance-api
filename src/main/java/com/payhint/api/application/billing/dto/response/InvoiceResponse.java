package com.payhint.api.application.billing.dto.response;

public record InvoiceResponse(String id, String customerId, String status, double amountDue, String dueDate) {
}
