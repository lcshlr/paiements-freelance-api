package com.payhint.api.domain.billing.exceptions;

import com.payhint.api.domain.billing.valueobjects.InstallmentId;
import com.payhint.api.domain.billing.valueobjects.InvoiceId;
import com.payhint.api.domain.shared.exceptions.DomainException;

public class InstallmentDoesNotBelongToInvoiceException extends DomainException {
    public InstallmentDoesNotBelongToInvoiceException(InstallmentId installmentId, InvoiceId invoiceId) {
        super(String.format("Installment does not belong to invoice: (Installment ID: %s, Invoice ID: %s)",
                installmentId, invoiceId));
    }
}
