package com.project.registration_service.service;

import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.RegistrationDTO;

import java.util.List;

public interface RegistrationService {
    RegistrationDTO getRegistration(Long id);
    List<RegistrationDTO> getAllRegistrations();
    RegistrationDTO createRegistration();
    RegistrationDTO updateRegistration();
    void deleteRegistration();

}
