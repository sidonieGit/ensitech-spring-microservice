package com.ensitech.auth_service.model.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateEmailDto {
    @NotBlank(message = "L'email  est obligatoire")
    @Email(message = "Email doit être valide")
    private String newEmail;
}
