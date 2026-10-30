package org.alfa.service._dto;


import lombok.*;

import java.time.Instant;
import java.util.UUID;


@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {

    private UUID id;

    private String token;

    private UUID user;

    private Instant expiryDate;

    private boolean revoked;
}

