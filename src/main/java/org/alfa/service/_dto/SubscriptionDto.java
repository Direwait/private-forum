package org.alfa.service._dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {

    private UUID id;

    private UUID userId;

    private UUID tariffId;

    private boolean isActive;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
