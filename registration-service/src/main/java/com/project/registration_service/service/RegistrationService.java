package com.project.registration_service.service;

import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.CreateRegistrationDTO;
import com.project.registration_service.dto.RegistrationDTO;
import com.project.registration_service.dto.RegistrationStudentDTO;

import java.util.List;

public interface RegistrationService {
    RegistrationDTO getRegistration(Long id);
    List<RegistrationDTO> getAllRegistrations();
    RegistrationDTO createRegistration(RegistrationDTO registrationDTO);
    RegistrationDTO updateRegistration(Long id, RegistrationDTO registrationDTO);
    void deleteRegistration(Long id);
    List<RegistrationStudentDTO> listByStudent(String matricule, boolean expandStudent);
    RegistrationStudentDTO getById(Long id, boolean expandStudent);
    RegistrationStudentDTO create(CreateRegistrationDTO dto);

}
