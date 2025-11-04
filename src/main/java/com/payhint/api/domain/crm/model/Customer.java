package com.payhint.api.domain.crm.model;

import java.time.LocalDateTime;

import com.payhint.api.domain.crm.valueobjects.CustomerId;
import com.payhint.api.domain.crm.valueobjects.Email;
import com.payhint.api.domain.crm.valueobjects.UserId;
import com.payhint.api.domain.shared.exceptions.InvalidPropertyException;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class Customer {
    private CustomerId id;
    @NonNull
    private UserId userId;
    @NonNull
    private String companyName;
    @NonNull
    private Email contactEmail;
    @NonNull
    private LocalDateTime createdAt;
    @NonNull
    private LocalDateTime updatedAt;

    public Customer(CustomerId id, @NonNull UserId userId, @NonNull String companyName, @NonNull Email contactEmail,
            @NonNull LocalDateTime createdAt, @NonNull LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.companyName = companyName;
        this.contactEmail = contactEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Customer create(UserId userId, String companyName, Email contactEmail) {
        return new Customer(null, userId, companyName, contactEmail, LocalDateTime.now(), LocalDateTime.now());
    }

    public void updateInformation(String companyName, Email contactEmail) {
        boolean isUpdated = false;

        if (companyName != null && companyName.isBlank()) {
            throw new InvalidPropertyException("Company name cannot be empty");
        }

        if (companyName != null) {
            this.companyName = companyName;
            isUpdated = true;
        }
        if (contactEmail != null) {
            this.contactEmail = contactEmail;
            isUpdated = true;
        }
        if (isUpdated) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    public boolean belongsToUser(UserId userId) {
        return this.userId != null && this.userId.equals(userId);
    }
}