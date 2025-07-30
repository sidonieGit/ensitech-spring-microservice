package com.ensitech.auth_service.model.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordDto {
    @NotBlank(message = "Ancien mot de passe requis")
    @Size(min = 6, message = "Le mot de passe doit avoir au moins  6 characters")
    private String oldPassword;

    @NotBlank(message = "Nouveau mot de passe requis")
    @Size(min = 6, message = "Le mot de passe doit avoir au moins  6 characters")
    private String newPassword;
}
