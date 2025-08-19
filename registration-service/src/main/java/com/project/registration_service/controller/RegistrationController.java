package com.project.registration_service.controller;

import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.RegistrationDTO;
import com.project.registration_service.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;


    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public ResponseEntity<List<RegistrationDTO>> getAllRegistrations(){
        return ResponseEntity.ok(this.registrationService.getAllRegistrations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationDTO> getRegistration(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.registrationService.getRegistration(id));
    }

    @PostMapping
    public ResponseEntity<RegistrationDTO> create(@RequestBody RegistrationDTO registrationDTO){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.registrationService.createRegistration(registrationDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistrationDTO> update(@PathVariable Long id, @RequestBody RegistrationDTO registrationDTO){
        return ResponseEntity.ok(this.registrationService.updateRegistration(id,registrationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.registrationService.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }
}
