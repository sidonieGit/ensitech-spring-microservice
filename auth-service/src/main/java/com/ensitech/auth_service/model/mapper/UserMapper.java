package com.ensitech.auth_service.model.mapper;

import com.ensitech.auth_service.model.dto.UserDto;
import com.ensitech.auth_service.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    User toUserEntity(UserDto user);
}