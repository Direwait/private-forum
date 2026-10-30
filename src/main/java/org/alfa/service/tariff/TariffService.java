package org.alfa.service.tariff;

import org.alfa.service._dto.TariffDto;

import java.util.List;
import java.util.UUID;

public interface TariffService {

    List<TariffDto> getAllTariffs();

    TariffDto getById(UUID tariffId);
}
