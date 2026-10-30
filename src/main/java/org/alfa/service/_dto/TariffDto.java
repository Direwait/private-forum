package org.alfa.service._dto;


import jakarta.persistence.*;
import lombok.*;
import org.alfa.database.enums.Currency;

import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TariffDto {

    private UUID id;

    private String name;

    private String description;

    private long price;

    private Currency currency;

    private int durationDays;
}
