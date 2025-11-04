package com.payhint.api.domain.billing.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.payhint.api.domain.billing.valueobjects.InstallmentId;
import com.payhint.api.domain.billing.valueobjects.InvoiceId;
import com.payhint.api.domain.billing.valueobjects.Money;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Installment {

    private InstallmentId id;
    private InvoiceId invoiceId;
    private Money amountDue;
    @Builder.Default
    private Money amountPaid = Money.ZERO;
    private LocalDate dueDate;
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    public Installment(InvoiceId invoiceId, Money amountDue, LocalDate dueDate) {
        this.invoiceId = invoiceId;
        this.amountDue = amountDue;
        this.dueDate = dueDate;
    }

    private void updateStatus(PaymentStatus newStatus) {
        this.status = newStatus;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate)
                && (status == PaymentStatus.PENDING || status == PaymentStatus.PARTIALLY_PAID);
    }

    public Money getRemainingAmount() {
        return amountDue.subtract(amountPaid);
    }

    public void addPayment(Payment payment) {
        if (payment.getAmount().compareTo(getRemainingAmount()) > 0) {
            throw new IllegalArgumentException("Payment amount exceeds remaining installment amount.");
        }
        this.payments.add(payment);
        this.amountPaid = this.amountPaid.add(payment.getAmount());
        updatePaymentStatus();
    }

    private void updatePaymentStatus() {
        Money remaining = getRemainingAmount();
        if (remaining.amount().compareTo(BigDecimal.ZERO) == 0) {
            updateStatus(PaymentStatus.PAID);
        } else if (remaining.compareTo(amountDue) < 0) {
            updateStatus(PaymentStatus.PARTIALLY_PAID);
        } else if (isOverdue()) {
            updateStatus(PaymentStatus.LATE);
        }
    }
}
