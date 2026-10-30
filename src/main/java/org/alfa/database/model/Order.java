package org.alfa.database.model;


import jakarta.persistence.*;
import lombok.*;
import org.alfa.database.enums.Currency;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "total_amount")
    private long totalAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;
}
