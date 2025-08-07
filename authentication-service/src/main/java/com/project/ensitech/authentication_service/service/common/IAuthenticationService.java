package com.project.ensitech.authentication_service.service.common;

import com.project.ensitech.authentication_service.model.dto.AuthenticationResponse;
import com.project.ensitech.authentication_service.model.dto.LoginRequest;
import com.project.ensitech.authentication_service.model.dto.RegisterRequest;

import java.util.Optional;

public interface IAuthenticationService {
   // AuthenticationResponse register(RegisterRequest req);
    String register(RegisterRequest req);
    AuthenticationResponse login(LoginRequest req);
}
