package com.payhint.api.domain.crm.model;

import java.time.LocalDateTime;

import com.payhint.api.domain.crm.valueobject.Email;
import com.payhint.api.domain.crm.valueobject.UserId;
import com.payhint.api.domain.shared.exception.InvalidPropertyException;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class User {

    private UserId id;
    @NonNull
    private Email email;
    @NonNull
    private String password;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private LocalDateTime createdAt;
    @NonNull
    private LocalDateTime updatedAt;

    public User(UserId id, @NonNull Email email, @NonNull String password, @NonNull String firstName,
            @NonNull String lastName, @NonNull LocalDateTime createdAt, @NonNull LocalDateTime updatedAt) {
        ensureNotBlankIfProvided("First name", firstName);
        ensureNotBlankIfProvided("Last name", lastName);
        ensureNotBlankIfProvided("Password", password);
        ensurePasswordIsValid(password);
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(Email email, String password, String firstName, String lastName) {
        if (email == null) {
            throw new InvalidPropertyException("Email cannot be null");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new InvalidPropertyException("First name cannot be empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new InvalidPropertyException("Last name cannot be empty");
        }
        if (password.isBlank()) {
            throw new InvalidPropertyException("Password cannot be empty");
        }
        return new User(null, email, password, firstName, lastName, LocalDateTime.now(), LocalDateTime.now());
    }

    private void ensurePasswordIsValid(String password) {
        if (password == null || password.isBlank()) {
            throw new InvalidPropertyException("Password cannot be empty");
        }
    }

    public void changePassword(String password) {
        ensurePasswordIsValid(password);
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }

    public void ensureNotBlankIfProvided(String fieldName, String value) {
        if (value != null && value.isBlank()) {
            throw new InvalidPropertyException(fieldName + " cannot be empty");
        }
    }

    public void updateProfile(Email email, String firstName, String lastName) {
        boolean isUpdated = false;

        ensureNotBlankIfProvided("First name", firstName);
        ensureNotBlankIfProvided("Last name", lastName);

        if (firstName != null) {
            this.firstName = firstName;
            isUpdated = true;
        }

        if (lastName != null) {
            this.lastName = lastName;
            isUpdated = true;
        }

        if (email != null) {
            this.email = email;
            isUpdated = true;
        }
        if (isUpdated) {
            this.updatedAt = LocalDateTime.now();
        }
    }
}
