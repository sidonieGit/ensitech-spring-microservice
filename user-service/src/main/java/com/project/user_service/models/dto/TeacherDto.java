package com.project.user_service.models.dto;

import com.project.user_service.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data  // Génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les champs (utile pour les tests)
public class TeacherDto {
    private Long id; // L'identifiant unique de l'enseignant
    private String firstName;
    private  String lastName;
    private String email;
    private String address;
    private String telephone;
    private Date birthday;
    private Gender gender;
}
