package com.ensitech.auth_service.controller;

import com.ensitech.auth_service.model.dto.*;
import com.ensitech.auth_service.service.common.IAuthService;
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
public class AuthController {

    private final IAuthService iAuthService;
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid  @RequestBody UserRegisterDto request) {
        return ResponseEntity.ok(iAuthService.register(request));
    }

   /* @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return iAuthService.login(request)
                ? ResponseEntity.ok("Connexion réussie")
                : ResponseEntity.status(401).body("Identifiants incorrects");
    }*/
   /*@PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody LoginRequest request) {
       Optional<UserDto> userOpt = iAuthService.login(request);
       return userOpt
               .<ResponseEntity<?>>map(ResponseEntity::ok)
               // .orElseGet(() -> ResponseEntity.status(401).body("Identifiants incorrects"));
               .orElseGet(() -> ResponseEntity.status(401).body( throw new RuntimeException("Identifiants incorrects"));
   }*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto request) {
        UserDto user = iAuthService.login(request)
                .orElseThrow(() -> new RuntimeException("Identifiants incorrects"));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/email/{id}")
    public ResponseEntity<String> updateEmail(@Valid @PathVariable Long id, @RequestBody UpdateEmailDto request) {
        return ResponseEntity.ok(iAuthService.updateEmail(id, request.getNewEmail()));
    }

    @PutMapping("/update-password/{id}")
    public ResponseEntity<String> updatePassword(@Valid @PathVariable Long id, @RequestBody UpdatePasswordDto request) {
       return ResponseEntity.ok(iAuthService.updatePassword(id, request));
       /* System.out.println("updatePassword controler "+request.toString());
        try {
            String result = iAuthService.updatePassword(id, request);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            System.out.println("updatePassword controler error "+e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }*/
    }
    @GetMapping("/test-error")
    public ResponseEntity<?> testError() {
        throw new RuntimeException("Erreur simulée");
    }
}