package org.alfa.database.model;


import jakarta.persistence.*;
import lombok.*;
import org.alfa.database.enums.Currency;
import org.alfa.database.enums.SubTier;

import java.util.UUID;

@Entity
@Table(name = "tariffs")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "tier")
    @Enumerated(EnumType.STRING)
    private SubTier tier;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private long price;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "duration_days")
    private int durationDays;
}
