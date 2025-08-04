package com.project.registration_service.service;

import com.project.registration_service.dao.repository.RegistrationRepository;
import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.RegistrationDTO;
import com.project.registration_service.mapper.RegistrationMapper;

import java.util.List;
import java.util.Optional;

public class RegistrationServiceImpl implements RegistrationService{
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
    }


    @Override
    public RegistrationDTO getRegistration(Long id) {
        Optional<Registration> registration = registrationRepository.findById(id);
        if(registration.isPresent()){
            return this.registrationMapper.toDto(registration.get());
        }
         return null;
    }

    @Override
    public List<RegistrationDTO> getAllRegistrations() {
        return List.of();
    }

    @Override
    public RegistrationDTO createRegistration() {
        return null;
    }

    @Override
    public RegistrationDTO updateRegistration() {
        return null;
    }

    @Override
    public void deleteRegistration() {

    }


}
