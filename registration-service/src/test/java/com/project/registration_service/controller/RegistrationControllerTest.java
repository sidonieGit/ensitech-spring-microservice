package com.project.registration_service.controller;

import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.RegistrationDTO;
import com.project.registration_service.enumeration.Level;
import com.project.registration_service.mapper.RegistrationMapper;
import com.project.registration_service.service.RegistrationService;
import com.project.registration_service.service.RegistrationServiceImpl;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrationServiceImpl registrationService;

    @Mock
    private RegistrationMapper registrationMapper;

    private Registration registration;
    private RegistrationDTO registrationDTO;
    @BeforeEach
    void setUp(){
        registrationDTO = new RegistrationDTO(1L, Level.L1);
        registration = new Registration();
        registration.setRegistrationNumber(1L);
        registration.setLevel(Level.L1);
    }

    @Test
    void iShouldGetAllRegistrations(){
//        when(registrationService.getAllRegistrations()).thenReturn();
    }

    @Test
    void itShouldGetRegistration() throws Exception{
        // Arrange - préparer le mock du service
        Mockito.when(registrationService.getRegistration(1L)).thenReturn(registrationDTO);

        // Act & Assert - exécuter et vérifier
        mockMvc.perform(MockMvcRequestBuilders.get("/api/registrations/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void itShouldGetAllRegistrations() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldCreateRegistration() throws Exception{

        Mockito.when(this.registrationService.createRegistration(registrationDTO))
                        .thenReturn(registrationDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "registrationNumber": 1,
                          "level": "L1"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registrationNumber").value(1L))
                .andExpect(jsonPath("$.level").value("L1"));

                Mockito.verify(registrationService, Mockito.times(1))
                        .createRegistration(any(RegistrationDTO.class));


    }




}
