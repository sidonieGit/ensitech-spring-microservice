package com.project.user_service.service.implementation;


import com.project.user_service.models.dto.StudentDto;
import com.project.user_service.models.entities.Student;
import com.project.user_service.repository.PersonRepository;
import com.project.user_service.service.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock // On crée un faux Repository
    private PersonRepository personRepository;

    @Mock // On crée un faux Mapper
    private StudentMapper studentMapper;

    @InjectMocks // Crée une instance de StudentServiceImpl et y injecte les mocks
    private StudentServiceImpl studentService;

    @Test
    void createStudent_shouldGenerateMatricule_andSaveStudent() {
        // Arrange (Préparation)
        StudentDto inputDto = new StudentDto(); // Remplir avec des données de test
        inputDto.setEmail("test@student.com");

        Student studentWithoutMatricule = new Student();
        Student savedStudentWithMatricule = new Student();
        savedStudentWithMatricule.setId(1L);
        savedStudentWithMatricule.setMatricule("ENS-GENERATED");

        // Définir le comportement des mocks
        when(studentMapper.toEntity(inputDto)).thenReturn(studentWithoutMatricule);
        when(personRepository.save(any(Student.class))).thenReturn(savedStudentWithMatricule);
        when(studentMapper.toDto(savedStudentWithMatricule)).thenReturn(new StudentDto()); // Le DTO de retour

        // Act (Action)
        studentService.createStudent(inputDto);

        // Assert (Vérification)
        // On vérifie que la méthode save a bien été appelée
        verify(personRepository, times(1)).save(any(Student.class));

        // On peut vérifier que le matricule a bien été positionné sur l'entité avant la sauvegarde
        assertThat(studentWithoutMatricule.getMatricule()).isNotNull();
        assertThat(studentWithoutMatricule.getMatricule()).startsWith("ENS-");
    }
}