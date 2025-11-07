package com.payhint.api.domain.billing.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.payhint.api.domain.billing.exception.InstallmentDoesNotBelongToInvoiceException;
import com.payhint.api.domain.billing.exception.InvalidMoneyValueException;
import com.payhint.api.domain.billing.valueobject.InvoiceId;
import com.payhint.api.domain.billing.valueobject.InvoiceReference;
import com.payhint.api.domain.billing.valueobject.Money;
import com.payhint.api.domain.crm.valueobject.CustomerId;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class Invoice {

    private InvoiceId id;

    @NonNull
    private CustomerId customerId;

    @NonNull
    private InvoiceReference invoiceReference;

    @NonNull
    private Money totalAmount;

    @NonNull
    private String currency;

    private boolean isArchived;

    @NonNull
    private LocalDateTime createdAt;

    @NonNull
    private LocalDateTime updatedAt;

    private List<Installment> installments;

    public Invoice(@NonNull InvoiceId id, @NonNull CustomerId customerId, @NonNull InvoiceReference invoiceReference,
            @NonNull Money totalAmount, @NonNull String currency, @NonNull LocalDateTime createdAt,
            @NonNull LocalDateTime updatedAt, @NonNull List<Installment> installments, boolean isArchived) {
        this.id = id;
        this.customerId = customerId;
        this.invoiceReference = invoiceReference;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.isArchived = isArchived;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.installments = installments;
    }

    public static Invoice create(@NonNull CustomerId customerId, @NonNull InvoiceReference invoiceReference,
            @NonNull Money totalAmount, @NonNull String currency) {
        return new Invoice(null, customerId, invoiceReference, totalAmount, currency, LocalDateTime.now(),
                LocalDateTime.now(), new ArrayList<>(), false);
    }

    public List<Installment> getInstallments() {
        return List.copyOf(installments);
    }

    private void validateInstallmentBelonging(Installment installment) {
        if (installment.getInvoiceId() == null || !installment.getInvoiceId().equals(this.id)) {
            throw new InstallmentDoesNotBelongToInvoiceException(installment.getId(), this.id);
        }
    }

    private void ensureDueDateDoesNotExistInOtherInstallments(@NonNull LocalDate dueDate) {
        boolean dueDateExists = this.installments.stream()
                .anyMatch(installment -> installment.getDueDate().equals(dueDate));
        if (dueDateExists) {
            throw new IllegalArgumentException("An installment with due date " + dueDate + " already exists.");
        }
    }

    private void ensureTotalAmountIsNotLessThanInstallmentsTotalAmountDue(Money newTotalAmount) {
        Money totalInstallments = installments.stream().map(Installment::getAmountDue).reduce(Money.ZERO, Money::add);
        if (newTotalAmount.compareTo(totalInstallments) < 0) {
            throw new InvalidMoneyValueException("Total amount cannot be less than the total installments amount due: "
                    + totalInstallments.amount());
        }
    }

    public PaymentStatus getPaymentStatus() {
        Money totalPaid = getTotalPaid();
        if (totalPaid.compareTo(totalAmount) == 0) {
            return PaymentStatus.PAID;
        } else if (totalPaid.compareTo(Money.ZERO) > 0) {
            return PaymentStatus.PARTIALLY_PAID;
        }
        return PaymentStatus.PENDING;
    }

    public boolean isOverdue() {
        return installments.stream().anyMatch(installment -> installment.isOverdue());
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

    public void unArchive() {
        this.isArchived = false;
    }

    public void archive() {
        this.isArchived = true;
    }

    private void ensureNotArchived() {
        if (this.isArchived) {
            throw new IllegalStateException("Cannot modify an archived invoice.");
        }
    }

    public void updateDetails(InvoiceReference invoiceReference, Money totalAmount, String currency) {
        ensureNotArchived();
        boolean updated = false;
        if (invoiceReference != null && !invoiceReference.equals(this.invoiceReference)) {
            this.invoiceReference = invoiceReference;
            updated = true;
        }
        if (totalAmount != null && !totalAmount.equals(this.totalAmount)) {
            ensureTotalAmountIsNotLessThanInstallmentsTotalAmountDue(totalAmount);
            this.totalAmount = totalAmount;
            updated = true;
        }
        if (currency != null && !currency.equals(this.currency)) {
            this.currency = currency;
            updated = true;
        }
        if (updated) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    private void ensureInstallmentCanBeAdded(@NonNull Installment installment) {
        Money installmentTotal = this.installments.stream().map(Installment::getAmountDue)
                .reduce(Money.ZERO, Money::add).add(installment.getAmountDue());
        if (this.totalAmount.compareTo(installmentTotal) < 0) {
            throw new InvalidMoneyValueException("Installment amountDue exceeds invoice total amount.");
        }
        ensureDueDateDoesNotExistInOtherInstallments(installment.getDueDate());
    }

    private void ensureInstallmentCanBeUpdated(@NonNull Installment existingInstallment,
            @NonNull Installment updatedInstallment) {
        Money installmentTotal = this.installments.stream()
                .filter(inst -> !inst.getId().equals(updatedInstallment.getId())).map(Installment::getAmountDue)
                .reduce(Money.ZERO, Money::add).add(updatedInstallment.getAmountDue());
        if (this.totalAmount.compareTo(installmentTotal) < 0) {
            throw new InvalidMoneyValueException("Updated installment amountDue exceeds invoice total amount.");
        }
        if (!existingInstallment.getDueDate().equals(updatedInstallment.getDueDate())) {
            ensureDueDateDoesNotExistInOtherInstallments(updatedInstallment.getDueDate());
        }
    }

    private Installment getExistingInstallment(@NonNull Installment installment) {
        return this.installments.stream().filter(inst -> inst.getId().equals(installment.getId())).findFirst()
                .orElseThrow(() -> new InstallmentDoesNotBelongToInvoiceException(installment.getId(), this.id));
    }

    public void addInstallment(@NonNull Installment installment) {
        ensureNotArchived();
        validateInstallmentBelonging(installment);
        if (this.installments.stream().anyMatch(inst -> inst.getId().equals(installment.getId()))) {
            throw new IllegalArgumentException(
                    "Installment with id " + installment.getId() + " already exists in the invoice.");
        }
        ensureInstallmentCanBeAdded(installment);
        this.installments.add(installment);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateInstallment(@NonNull Installment updatedInstallment) {
        ensureNotArchived();
        Installment existingInstallment = getExistingInstallment(updatedInstallment);
        ensureInstallmentCanBeUpdated(existingInstallment, updatedInstallment);
        existingInstallment.updateDetails(updatedInstallment.getAmountDue(), updatedInstallment.getDueDate());
        this.updatedAt = LocalDateTime.now();
    }

    public void removeInstallment(@NonNull Installment installment) {
        ensureNotArchived();
        validateInstallmentBelonging(installment);
        this.installments.remove(installment);
        this.updatedAt = LocalDateTime.now();
    }

    public void addPayment(@NonNull Installment installment, @NonNull Payment payment) {
        ensureNotArchived();
        validateInstallmentBelonging(installment);
        Installment existingInstallment = getExistingInstallment(installment);
        existingInstallment.addPayment(payment);
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePayment(@NonNull Installment installment, @NonNull Payment updatedPayment) {
        ensureNotArchived();
        validateInstallmentBelonging(installment);
        Installment existingInstallment = getExistingInstallment(installment);
        existingInstallment.updatePayment(updatedPayment);
        this.updatedAt = LocalDateTime.now();
    }

    public void removePayment(@NonNull Installment installment, @NonNull Payment payment) {
        ensureNotArchived();
        validateInstallmentBelonging(installment);
        Installment existingInstallment = getExistingInstallment(installment);
        existingInstallment.removePayment(payment);
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Invoice invoice = (Invoice) o;

        return id != null && id.equals(invoice.id);
    }
}
