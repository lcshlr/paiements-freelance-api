package com.payhint.api.domain.billing.exception;

import com.payhint.api.domain.shared.exception.DomainException;

public class InvalidMoneyValueException extends DomainException {
    public InvalidMoneyValueException(String message) {
        super(message);
    }
}
