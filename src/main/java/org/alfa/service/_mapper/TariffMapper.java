package org.alfa.service._mapper;

import org.alfa.database.model.Tariff;
import org.alfa.service._dto.TariffDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TariffMapper {


    TariffDto modelToDto(Tariff tariff);

    Tariff dtoToModel(TariffDto dto);
}
