package com.project.ensitech.authentication_service.controller;

import com.project.ensitech.authentication_service.model.dto.AuthenticationResponse;
import com.project.ensitech.authentication_service.model.dto.LoginRequest;
import com.project.ensitech.authentication_service.model.dto.RegisterRequest;
import com.project.ensitech.authentication_service.service.common.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthentificationController {
private final IAuthenticationService iAuthenticationService;
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/register")
    //public ResponseEntity<AuthenticationResponse> register(
    public ResponseEntity<String> register(
            @Valid
            @RequestBody RegisterRequest request
    ) {
      return ResponseEntity.ok(iAuthenticationService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(iAuthenticationService.login(request));
    }

}
