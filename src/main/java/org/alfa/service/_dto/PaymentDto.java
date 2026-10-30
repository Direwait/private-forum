package org.alfa.service._dto;

import lombok.*;
import org.alfa.database.enums.Currency;
import org.alfa.database.enums.PaymentStatus;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private UUID id;

    private UUID userId;

    private UUID tariffId;

    private String trackingId;

    private PaymentStatus status;

    private long amount;

    private Currency currency;

    private String description;

    private String payerEmail;

    private String payerPhone;

    private LocalDateTime paidAt;

    private String gatewayId;

    private String refId;

    private String errorMessage;
}
