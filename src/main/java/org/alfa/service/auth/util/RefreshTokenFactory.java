package org.alfa.service.auth.util;

import lombok.RequiredArgsConstructor;
import org.alfa.database.model.RefreshToken;
import org.alfa.database.model.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenFactory {

    public RefreshToken create(User user, String token) {
        return RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plus(30, ChronoUnit.DAYS))
                .revoked(false)
                .build();
    }
}
