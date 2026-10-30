package org.alfa.service._mapper;


import org.alfa.database.model.Subscription;
import org.alfa.database.model.User;
import org.alfa.service._dto.SubscriptionDto;
import org.alfa.service._dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "tariff.id", target = "tariffId")
    SubscriptionDto modelToDto(Subscription subscription);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "tariffId", target = "tariff.id")
    Subscription dtoToModel(SubscriptionDto dto);
}
