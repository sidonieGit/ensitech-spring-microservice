package com.project.registration_service.mapper;

import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.CreateRegistrationDTO;
import com.project.registration_service.dto.RegistrationStudentDTO;
import com.project.registration_service.enumeration.Level;
import com.project.registration_service.model.Student;

public final class StudentRegistrationMapper {
    private StudentRegistrationMapper() {}

//    public static Registration toEntity(RegistrationStudentDTO dto) {
//        if (dto == null) return null;
//        return Registration.builder()
//                .registrationNumber(Long.valueOf(dto.registrationNumber()))
//                .level(dto.level() == null ? null : Level.valueOf(dto.level()))
//                .dateOfRegistration(dto.dateOfRegistration().atStartOfDay())
//                .matricule(dto.matricule())
//                .build();
//    }

    public static RegistrationStudentDTO toDto(Registration entity) {
        if (entity == null) return null;
        return RegistrationStudentDTO.builder()
                .registrationNumber(String.valueOf(entity.getRegistrationNumber()))
                .level(entity.getLevel() == null ? null : String.valueOf(entity.getLevel()))
                .dateOfRegistration(entity.getDateOfRegistration().toLocalDate())
                .matricule(entity.getMatricule())
                .student(null) // on enrichira après via Feign
                .build();
    }
    public static RegistrationStudentDTO toDto(Registration entity, Student student) {
        if (entity == null) return null;

        return RegistrationStudentDTO.builder()
                .registrationNumber(entity.getRegistrationNumber() != null
                        ? String.valueOf(entity.getRegistrationNumber())
                        : null)
                .level(entity.getLevel() != null ? entity.getLevel().name() : null)
                .dateOfRegistration(entity.getDateOfRegistration() != null
                        ? entity.getDateOfRegistration().toLocalDate()
                        : null)
                .matricule(entity.getMatricule())
                .student(student) // <-- null si expandStudent=false
                .build();
    }
    public static Registration toEntity(CreateRegistrationDTO dto) {
        return Registration.builder()
                .registrationNumber(dto.registrationNumber())
                .matricule(dto.matricule())
                .level(dto.level())
                .build();
    }


    public static CreateRegistrationDTO toCreateDto(Registration entity) {
        return new CreateRegistrationDTO(
                entity.getRegistrationNumber(),
                entity.getMatricule(),
                entity.getLevel()
        );
    }

}
