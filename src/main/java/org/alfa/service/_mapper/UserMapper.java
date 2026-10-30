package org.alfa.service._mapper;


import org.alfa.database.model.User;
import org.alfa.service._dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto modelToDto(User user);

    User dtoToModel(UserDto dto);
}
