package com.project.registration_service.domain;

import com.project.registration_service.enumeration.Level;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter @Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long registrationNumber;

    @Enumerated(EnumType.STRING)
    private Level level;

    private LocalDateTime dateOfRegistration;

    @PrePersist
    void OnCreate(){
        this.dateOfRegistration = LocalDateTime.now();
    }

}
