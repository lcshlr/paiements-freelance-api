package com.payhint.api.domain.billing.valueobject;

import java.math.BigDecimal;

import com.payhint.api.domain.billing.exception.InvalidMoneyValueException;

public record Money(BigDecimal amount) {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMoneyValueException("Money amount cannot be null or negative");
        }
    }

    public Money add(Money other) {
        if (other == null) {
            throw new InvalidMoneyValueException("Cannot add null Money");
        }
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        if (other == null) {
            throw new InvalidMoneyValueException("Cannot subtract null Money");
        }
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMoneyValueException("Cannot subtract: result would be negative");
        }
        return new Money(result);
    }

    public int compareTo(Money other) {
        if (other == null) {
            throw new InvalidMoneyValueException("Cannot compare with null Money");
        }
        return this.amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Money other))
            return false;
        return this.amount.compareTo(other.amount) == 0;
    }

    @Override
    public int hashCode() {
        return this.amount.stripTrailingZeros().hashCode();
    }
}
