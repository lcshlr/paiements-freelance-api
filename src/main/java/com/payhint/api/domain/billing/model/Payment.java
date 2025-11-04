package com.payhint.api.domain.billing.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.payhint.api.domain.billing.valueobjects.InstallmentId;
import com.payhint.api.domain.billing.valueobjects.Money;
import com.payhint.api.domain.billing.valueobjects.PaymentId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Payment {

    private PaymentId id;
    private InstallmentId installmentId;
    private Money amount;
    private LocalDate paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Payment(InstallmentId installmentId, Money amount, LocalDate paymentDate) {
        this.installmentId = installmentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.createdAt = LocalDateTime.now();
    }

    public void updateDetails(Money amount, LocalDate paymentDate) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.updatedAt = LocalDateTime.now();
    }
}
