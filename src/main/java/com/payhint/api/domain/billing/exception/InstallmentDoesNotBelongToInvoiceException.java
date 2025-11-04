package com.payhint.api.domain.billing.exception;

import com.payhint.api.domain.billing.valueobject.InstallmentId;
import com.payhint.api.domain.billing.valueobject.InvoiceId;
import com.payhint.api.domain.shared.exception.DomainException;

public class InstallmentDoesNotBelongToInvoiceException extends DomainException {
    public InstallmentDoesNotBelongToInvoiceException(InstallmentId installmentId, InvoiceId invoiceId) {
        super(String.format("Installment does not belong to invoice: (Installment ID: %s, Invoice ID: %s)",
                installmentId, invoiceId));
    }
}
