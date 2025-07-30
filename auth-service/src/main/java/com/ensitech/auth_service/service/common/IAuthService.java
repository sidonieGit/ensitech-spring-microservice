package com.ensitech.auth_service.service.common;

import com.ensitech.auth_service.model.dto.UserLoginDto;
import com.ensitech.auth_service.model.dto.UserRegisterDto;
import com.ensitech.auth_service.model.dto.UpdatePasswordDto;
import com.ensitech.auth_service.model.dto.UserDto;

import java.util.Optional;

public interface IAuthService {
    String register(UserRegisterDto req);
    Optional<UserDto> login(UserLoginDto req);
    // String login(UserLoginDto req);
    String updateEmail(Long userId, String newEmail);
    String updatePassword(Long userId, UpdatePasswordDto request);
}
