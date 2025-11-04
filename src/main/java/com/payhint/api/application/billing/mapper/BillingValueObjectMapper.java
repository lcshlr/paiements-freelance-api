package com.payhint.api.application.billing.mapper;

import java.math.BigDecimal;

import org.mapstruct.Mapper;

import com.payhint.api.domain.billing.valueobjects.InstallmentId;
import com.payhint.api.domain.billing.valueobjects.InvoiceId;
import com.payhint.api.domain.billing.valueobjects.InvoiceReference;
import com.payhint.api.domain.billing.valueobjects.Money;
import com.payhint.api.domain.billing.valueobjects.PaymentId;

@Mapper(componentModel = "spring")
public interface BillingValueObjectMapper {
    default String map(InvoiceId invoiceId) {
        return invoiceId == null ? null : invoiceId.value().toString();
    }

    default InvoiceId mapToInvoiceId(String invoiceId) {
        return invoiceId == null ? null : InvoiceId.fromString(invoiceId);
    }

    default String map(InstallmentId installmentId) {
        return installmentId == null ? null : installmentId.value().toString();
    }

    default InstallmentId mapToInstallmentId(String installmentId) {
        return installmentId == null ? null : InstallmentId.fromString(installmentId);
    }

    default String map(PaymentId paymentId) {
        return paymentId == null ? null : paymentId.value().toString();
    }

    default PaymentId mapToPaymentId(String paymentId) {
        return paymentId == null ? null : PaymentId.fromString(paymentId);
    }

    default String map(InvoiceReference invoiceReference) {
        return invoiceReference == null ? null : invoiceReference.value();
    }

    default InvoiceReference mapToInvoiceReference(String invoiceReference) {
        return invoiceReference == null ? null : new InvoiceReference(invoiceReference);
    }

    default String map(Money money) {
        return money == null ? "0" : money.amount().toString();
    }

    default Money mapToMoney(String amount) {
        return amount == null ? null : new Money(BigDecimal.valueOf(Double.parseDouble(amount)));
    }
}
