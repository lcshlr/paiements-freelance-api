package com.payhint.api.application.billing.usecase;

import com.payhint.api.application.billing.dto.request.CreateInvoiceRequest;
import com.payhint.api.application.billing.dto.request.UpdateInvoiceRequest;
import com.payhint.api.application.billing.dto.response.InvoiceResponse;
import com.payhint.api.domain.billing.valueobject.InvoiceReference;

public interface InvoiceManagementUseCase {
    InvoiceResponse createInvoice(CreateInvoiceRequest request);

    InvoiceResponse viewInvoice(InvoiceReference invoiceReference);

    InvoiceResponse updateInvoice(InvoiceReference invoiceReference, UpdateInvoiceRequest request);

    void deleteInvoice(InvoiceReference invoiceReference);
}
