package com.payhint.api.domain.billing.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.payhint.api.domain.billing.valueobjects.InstallmentId;
import com.payhint.api.domain.billing.valueobjects.InvoiceId;
import com.payhint.api.domain.billing.valueobjects.Money;

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
    private List<Payment> payments;
    @NonNull
    private LocalDateTime createdAt;
    @NonNull
    private LocalDateTime updatedAt;

    public Installment(InstallmentId id, @NonNull InvoiceId invoiceId, @NonNull Money amountDue,
            @NonNull Money amountPaid, @NonNull LocalDate dueDate, @NonNull PaymentStatus status,
            @NonNull List<Payment> payments, @NonNull LocalDateTime createdAt, @NonNull LocalDateTime updatedAt) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.dueDate = dueDate;
        this.status = status;
        this.payments = payments;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Installment create(InvoiceId invoiceId, Money amountDue, LocalDate dueDate) {
        return new Installment(null, invoiceId, amountDue, Money.ZERO, dueDate, PaymentStatus.PENDING,
                new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate)
                && (status == PaymentStatus.PENDING || status == PaymentStatus.PARTIALLY_PAID);
    }

    public Money getRemainingAmount() {
        return amountDue.subtract(amountPaid);
    }

    private void updateStatus(PaymentStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    void updatePaymentStatus() {
        Money remaining = getRemainingAmount();
        if (remaining.amount().compareTo(BigDecimal.ZERO) == 0) {
            updateStatus(PaymentStatus.PAID);
        } else if (remaining.compareTo(amountDue) < 0) {
            updateStatus(PaymentStatus.PARTIALLY_PAID);
        } else if (isOverdue()) {
            updateStatus(PaymentStatus.LATE);
        }
    }

    void addPayment(Payment payment) {
        if (payment.getAmount().compareTo(getRemainingAmount()) > 0) {
            throw new IllegalArgumentException("Payment amount exceeds remaining installment amount.");
        }
        this.payments.add(payment);
        this.amountPaid = this.amountPaid.add(payment.getAmount());
        updatePaymentStatus();
    }

    void updatePayment(Payment oldPayment, Payment newPayment) {
        int index = this.payments.indexOf(oldPayment);
        if (index != -1) {
            this.payments.set(index, newPayment);
            this.amountPaid = this.amountPaid.subtract(oldPayment.getAmount()).add(newPayment.getAmount());
            updatePaymentStatus();
        } else {
            throw new IllegalArgumentException("Old payment not found in installment.");
        }
    }

    void removePayment(Payment payment) {
        if (this.payments.remove(payment)) {
            this.amountPaid = this.amountPaid.subtract(payment.getAmount());
            updatePaymentStatus();
        } else {
            throw new IllegalArgumentException("Payment not found in installment.");
        }
    }
}
