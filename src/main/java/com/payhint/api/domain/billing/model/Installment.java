package com.payhint.api.domain.billing.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import com.payhint.api.domain.billing.exception.InvalidMoneyValueException;
import com.payhint.api.domain.billing.valueobject.InstallmentId;
import com.payhint.api.domain.billing.valueobject.InvoiceId;
import com.payhint.api.domain.billing.valueobject.Money;
import com.payhint.api.domain.billing.valueobject.PaymentId;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class Installment {

    private InstallmentId id;
    @NonNull
    private InvoiceId invoiceId;
    @NonNull
    private Money amountDue;
    @NonNull
    private Money amountPaid;
    @NonNull
    private LocalDate dueDate;
    @NonNull
    private PaymentStatus status;
    @NonNull
    private Map<PaymentId, Payment> payments;
    @NonNull
    private LocalDateTime createdAt;
    @NonNull
    private LocalDateTime updatedAt;
    @NonNull
    private LocalDateTime statusUpdatedAt;

    public Installment(InstallmentId id, @NonNull InvoiceId invoiceId, @NonNull Money amountDue,
            @NonNull Money amountPaid, @NonNull LocalDate dueDate, @NonNull PaymentStatus status,
            @NonNull Map<PaymentId, Payment> payments, @NonNull LocalDateTime createdAt,
            @NonNull LocalDateTime updatedAt, @NonNull LocalDateTime statusUpdatedAt) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.dueDate = dueDate;
        this.status = status;
        this.payments = payments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public static Installment create(InvoiceId invoiceId, Money amountDue, LocalDate dueDate) {
        return new Installment(null, invoiceId, amountDue, Money.ZERO, dueDate, PaymentStatus.PENDING,
                new LinkedHashMap<>(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
    }

    public boolean isPaid() {
        return status == PaymentStatus.PAID;
    }

    public boolean isOverdue() {
        return !isPaid() && LocalDate.now().isAfter(dueDate);
    }

    public Money getRemainingAmount() {
        return amountDue.subtract(amountPaid);
    }

    public void updateDetails(Money amountDue, LocalDate dueDate) {
        boolean updated = false;
        if (amountDue != null) {
            this.amountDue = amountDue;
            updated = true;
        }
        if (dueDate != null) {
            this.dueDate = dueDate;
            updated = true;
        }
        if (updated) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    private void updateStatus(@NonNull PaymentStatus newStatus) {
        if (newStatus != this.status) {
            this.status = newStatus;
            this.updatedAt = LocalDateTime.now();
            this.statusUpdatedAt = LocalDateTime.now();
        }
    }

    private void updatePaymentStatus() {
        Money remaining = getRemainingAmount();
        if (remaining.amount().compareTo(BigDecimal.ZERO) == 0) {
            updateStatus(PaymentStatus.PAID);
        } else if (remaining.compareTo(amountDue) < 0) {
            updateStatus(PaymentStatus.PARTIALLY_PAID);
        } else {
            updateStatus(PaymentStatus.PENDING);
        }
    }

    private void validatePaymentUpdate(@NonNull Money oldAmount, @NonNull Money newAmount) {
        Money maxAllowedNewAmount = getRemainingAmount().add(oldAmount);

        if (newAmount.compareTo(maxAllowedNewAmount) > 0) {
            throw new InvalidMoneyValueException("Updated payment amount exceeds remaining installment amount");
        }
    }

    void addPayment(@NonNull Payment payment) {
        if (payment.getAmount().compareTo(getRemainingAmount()) > 0) {
            throw new InvalidMoneyValueException("Payment amount exceeds remaining installment amount");
        }
        this.payments.put(payment.getId(), payment);
        this.amountPaid = this.amountPaid.add(payment.getAmount());
        updatePaymentStatus();
    }

    void updatePayment(@NonNull Payment newPayment) {
        if (newPayment.getId() == null) {
            throw new IllegalArgumentException("Payment ID cannot be null when updating a payment");
        }
        Payment existingPayment = this.payments.get(newPayment.getId());

        if (existingPayment == null) {
            throw new IllegalArgumentException("Payment to update not found in installment");
        }

        validatePaymentUpdate(existingPayment.getAmount(), newPayment.getAmount());

        this.payments.put(newPayment.getId(), newPayment);
        this.amountPaid = this.amountPaid.subtract(existingPayment.getAmount()).add(newPayment.getAmount());
        updatePaymentStatus();
    }

    void removePayment(@NonNull Payment payment) {
        if (payment.getId() == null) {
            throw new IllegalArgumentException("Payment ID cannot be null when removing a payment");
        }
        if (this.payments.remove(payment.getId()) != null) {
            this.amountPaid = this.amountPaid.subtract(payment.getAmount());
            updatePaymentStatus();
        } else {
            throw new IllegalArgumentException("Payment not found in installment");
        }
    }
}