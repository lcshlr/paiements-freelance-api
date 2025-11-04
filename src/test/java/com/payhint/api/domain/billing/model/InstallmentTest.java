package com.payhint.api.domain.billing.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.payhint.api.domain.billing.valueobject.InstallmentId;
import com.payhint.api.domain.billing.valueobject.InvoiceId;
import com.payhint.api.domain.billing.valueobject.Money;
import com.payhint.api.domain.billing.valueobject.PaymentId;

@DisplayName("Installment Domain Model Tests")
public class InstallmentTest {
    private static final PaymentId VALID_PAYMENT_ID = new PaymentId(UUID.randomUUID());
    private static final InstallmentId VALID_INSTALLMENT_ID = new InstallmentId(UUID.randomUUID());
    private static final InvoiceId VALID_INVOICE_ID = new InvoiceId(UUID.randomUUID());
    private static final Money VALID_PAYMENT_100_AMOUNT = new Money(BigDecimal.valueOf(100.00));
    private static final LocalDate VALID_PAYMENT_DATE = LocalDate.now();
    private static final LocalDate VALID_INVOICE_DUE_DATE = LocalDate.now().plusDays(30);
    private static final Money VALID_INVOICE_200_DUE_AMOUNT = new Money(BigDecimal.valueOf(200.00));

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create installment with valid parameters using simple constructor")
        void shouldCreateInstallmentWithValidParameters() {
            Installment installment = Installment.create(VALID_INVOICE_ID, VALID_INVOICE_200_DUE_AMOUNT,
                    VALID_INVOICE_DUE_DATE);

            assertThat(installment.getInvoiceId().toString()).isEqualTo(VALID_INVOICE_ID.toString());
            assertThat(installment.getAmountDue()).isEqualTo(VALID_INVOICE_200_DUE_AMOUNT);
            assertThat(installment.getAmountPaid()).isEqualTo(Money.ZERO);
            assertThat(installment.getDueDate()).isEqualTo(VALID_INVOICE_DUE_DATE);
            assertThat(installment.getStatus()).isEqualTo(PaymentStatus.PENDING);
            assertThat(installment.getPayments()).isEmpty();
            assertThat(installment.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
            assertThat(installment.getUpdatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        }

        @Test
        @DisplayName("Should create installment with all parameters using all-args constructor")
        void shouldCreateInstallmentWithAllParameters() {
            Installment installment = new Installment(VALID_INSTALLMENT_ID, VALID_INVOICE_ID,
                    VALID_INVOICE_200_DUE_AMOUNT, Money.ZERO, VALID_INVOICE_DUE_DATE, PaymentStatus.PENDING,
                    new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());

            assertThat(installment.getId().toString()).isEqualTo(VALID_INSTALLMENT_ID.toString());
            assertThat(installment.getInvoiceId().toString()).isEqualTo(VALID_INVOICE_ID.toString());
            assertThat(installment.getAmountDue()).isEqualTo(VALID_INVOICE_200_DUE_AMOUNT);
            assertThat(installment.getAmountPaid()).isEqualTo(Money.ZERO);
            assertThat(installment.getDueDate()).isEqualTo(VALID_INVOICE_DUE_DATE);
            assertThat(installment.getStatus()).isEqualTo(PaymentStatus.PENDING);
            assertThat(installment.getPayments()).isEmpty();
        }

        @Test
        @DisplayName("Should throw error when creating installment with null invoiceId")
        void shouldThrowErrorWhenCreatingInstallmentWithNullInvoiceId() {
            assertThatThrownBy(() -> Installment.create(null, VALID_INVOICE_200_DUE_AMOUNT, VALID_INVOICE_DUE_DATE))
                    .isInstanceOf(NullPointerException.class).hasMessageContaining("invoiceId");
        }
    }
}