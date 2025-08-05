package com.project.registration_service.mapper;

import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.RegistrationDTO;

public class RegistrationMapper {
    public Registration toEntity(RegistrationDTO registrationDTO){
        Registration registration = new Registration();
            registration.setRegistrationNumber(registrationDTO.registrationNumber());
            registration.setLevel(registrationDTO.level());

        return registration;
    }

    public RegistrationDTO toDto(Registration registration){
        return new RegistrationDTO(
          registration.getRegistrationNumber(),
          registration.getLevel()
        );
    }
}
