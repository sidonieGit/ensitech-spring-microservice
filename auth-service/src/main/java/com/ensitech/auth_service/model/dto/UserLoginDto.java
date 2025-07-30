package com.ensitech.auth_service.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDto {
    // private String username;
    @NotBlank(message = "L'email  est obligatoire")
    @Email(message = "Email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe  est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit avoir au moins  6 characters")
    private String password;
}
