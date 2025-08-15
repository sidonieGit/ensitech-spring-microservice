package com.project.registration_service.service;

import com.project.registration_service.dao.repository.RegistrationRepository;
import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.RegistrationDTO;
import com.project.registration_service.mapper.RegistrationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
        List<Registration> registrations = this.registrationRepository.findAll();
        return this.registrationMapper.toDtoList(registrations);
    }

    @Override
    public RegistrationDTO createRegistration(RegistrationDTO registrationDTO) {
           if(this.registrationRepository.existsByRegistrationNumber(registrationDTO.registrationNumber())){
               throw new IllegalArgumentException("Registration number already exists");
           }
           Registration registration = this.registrationMapper.toEntity(registrationDTO);
           this.registrationRepository.save(registration);
        return registrationDTO;

    }

    @Override
    public RegistrationDTO updateRegistration(Long id, RegistrationDTO registrationDTO) {

        Registration existingYear = this.registrationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Already exists!"));

        existingYear.setRegistrationNumber(registrationDTO.registrationNumber());
        existingYear.setLevel(registrationDTO.level());

        Registration registration = this.registrationRepository.save(existingYear);

        return registrationMapper.toDto(registration);


    }

    @Override
    public void deleteRegistration(Long id) {
        if(this.registrationRepository.findById(id).isPresent()){
            this.registrationRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("Not deleted!");
        }

    }


}
