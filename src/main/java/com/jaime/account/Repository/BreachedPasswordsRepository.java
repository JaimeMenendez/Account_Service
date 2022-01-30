package com.jaime.account.Repository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BreachedPasswordsRepository {
    List<String> breachedPasswords = Collections.synchronizedList(List.of("PasswordForJanuary",
            "PasswordForFebruary", "PasswordForMarch", "PasswordForApril", "PasswordForMay", "PasswordForJune",
            "PasswordForJuly", "PasswordForAugust", "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember",
            "PasswordForDecember"));

    final PasswordEncoder encoder;

    public BreachedPasswordsRepository(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public boolean isBreachedPassword(String password) {
        return breachedPasswords.contains(password);
    }
}
