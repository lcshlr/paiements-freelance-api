package com.payhint.api.domain.billing.exceptions;

import com.payhint.api.domain.shared.exceptions.DomainException;

public class InvalidMoneyValueException extends DomainException {
    public InvalidMoneyValueException(String message) {
        super(message);
    }
}
