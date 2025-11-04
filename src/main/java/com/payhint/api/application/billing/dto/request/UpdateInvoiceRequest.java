package com.payhint.api.application.billing.dto.request;

public record UpdateInvoiceRequest(String customerId, Double amountDue, String dueDate) {
}