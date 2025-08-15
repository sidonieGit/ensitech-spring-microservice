package com.project.registration_service.dto;

import com.project.registration_service.enumeration.Level;

// pour la methode post
public record CreateRegistrationDTO(
        Long registrationNumber,
        String matricule,
        Level level
) {

}
