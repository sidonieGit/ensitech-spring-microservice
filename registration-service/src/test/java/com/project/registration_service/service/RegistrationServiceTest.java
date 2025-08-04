package com.project.registration_service.service;

import com.project.registration_service.dao.repository.RegistrationRepository;
import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.RegistrationDTO;
import com.project.registration_service.enumeration.Level;
import com.project.registration_service.mapper.RegistrationMapper;
import com.project.registration_service.service.RegistrationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {


    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private RegistrationMapper registrationMapper;

    private Registration registration;
    private RegistrationDTO registrationDTO;

    @BeforeEach
    void setUp(){
        // Initialisation des objets de test
        registration = new Registration();
        registration.setRegistrationNumber(1L);
        registration.setLevel(Level.L1);

        registrationDTO = new RegistrationDTO(1L, Level.L1);
    }
    @Test
    void shouldFindRegistrationById(){

        // When findById(1L) is called, it must return the 'registration' object that we've created.
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));

        // Behavior of the mapping
        //When we call the toDto method with our objet 'registration', we expect to have the good result.
        when(registrationMapper.toDto(registration)).thenReturn(registrationDTO);

        // Call of the service that we're testing
        RegistrationDTO found = registrationService.getRegistration(1L);

        // Assertions : to check if our results are good
        assertThat(found).isNotNull();
        assertThat(found.level()).isEqualTo(registrationDTO.level());

        // Verify the repository was call once
        verify(registrationRepository, times(1)).findById(1L);


    }

}
