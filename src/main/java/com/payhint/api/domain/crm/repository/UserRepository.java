package com.payhint.api.domain.crm.repository;

import java.util.Optional;

import com.payhint.api.domain.crm.model.User;
import com.payhint.api.domain.crm.valueobject.Email;
import com.payhint.api.domain.crm.valueobject.UserId;

public interface UserRepository {

    User register(User user);

    Optional<User> findById(UserId userId);

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);

    boolean existsById(UserId userId);

    void delete(User user);
}
