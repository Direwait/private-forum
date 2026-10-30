package org.alfa.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.alfa.database.enums.Currency;
import org.alfa.database.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @Column(name = "tracking_id", nullable = false, unique = true)
    private String trackingId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "amount")
    private long amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "description")
    private String description;

    @Column(name = "payer_email")
    private String payerEmail;

    @Column(name = "payer_phone", length = 20)
    private String payerPhone;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "gateway_id")
    private String gatewayId;

    @Column(name = "ref_id")
    private String refId;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
}
