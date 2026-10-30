package org.alfa.service._mapper;

import org.alfa.database.model.Payment;
import org.alfa.service._dto.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "tariffId", source = "tariff.id")
    PaymentDto modelToDto(Payment payment);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tariff", ignore = true)
    Payment dtoToModel(PaymentDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tariff", ignore = true)
    @Mapping(target = "trackingId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paidAt", ignore = true)
    void updateModel(PaymentDto dto, @MappingTarget Payment payment);
}
