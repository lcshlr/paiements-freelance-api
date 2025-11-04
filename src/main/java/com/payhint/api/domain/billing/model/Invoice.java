package com.payhint.api.domain.billing.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.payhint.api.domain.billing.exceptions.InstallmentDoesNotBelongToInvoiceException;
import com.payhint.api.domain.billing.exceptions.InvalidMoneyValueException;
import com.payhint.api.domain.billing.valueobjects.InvoiceId;
import com.payhint.api.domain.billing.valueobjects.InvoiceReference;
import com.payhint.api.domain.billing.valueobjects.Money;
import com.payhint.api.domain.crm.valueobjects.CustomerId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private InvoiceId id;
    private CustomerId customerId;
    private InvoiceReference invoiceReference;
    private Money totalAmount;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<Installment> installments = new ArrayList<>();

    public Invoice(CustomerId customerId, InvoiceReference invoiceReference, Money totalAmount, String currency) {
        this.id = null;
        this.customerId = customerId;
        this.invoiceReference = invoiceReference;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.createdAt = null;
        this.updatedAt = null;
        this.installments = new ArrayList<>();
    }

    private void validateInstallmentBelonging(Installment installment) {
        if (!installment.getInvoiceId().equals(this.id)) {
            throw new InstallmentDoesNotBelongToInvoiceException(installment.getId(), this.id);
        }
    }

    public void updateInvoice(InvoiceReference invoiceReference, Money totalAmount, String currency) {
        this.invoiceReference = invoiceReference;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.updatedAt = LocalDateTime.now();
    }

    public void addPaymentToInstallment(Installment installment, Payment payment) {
        validateInstallmentBelonging(installment);
        if (payment.getAmount().compareTo(installment.getRemainingAmount()) > 0) {
            throw new InvalidMoneyValueException("Payment amount exceeds remaining installment amount.");
        }
        installment.addPayment(payment);
    }

    public void addInstallment(Installment installment) {
        validateInstallmentBelonging(installment);

        Money remainingAmount = getRemainingAmount();
        if (installment.getAmountDue().compareTo(remainingAmount) > 0) {
            throw new IllegalArgumentException("Installment amountDue exceeds remaining invoice amount.");
        }

        this.installments.add(installment);
    }

    public Money getTotalPaid() {
        return installments.stream().map(Installment::getAmountPaid).reduce(Money.ZERO, Money::add);
    }

    public Money getRemainingAmount() {
        return totalAmount.subtract(getTotalPaid());
    }

    public boolean isFullyPaid() {
        return getRemainingAmount().compareTo(Money.ZERO) == 0;
    }
}
