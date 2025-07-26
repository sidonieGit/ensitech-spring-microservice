package com.project.user_service.repository;

import com.project.user_service.models.entities.Person;
import com.project.user_service.models.entities.Student;
import com.project.user_service.models.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository // Indique à Spring que c'est un composant de la couche d'accès aux données
public interface PersonRepository extends JpaRepository <Person, Long>{
    // Spring Data JPA est magique. En déclarant cette méthode, il sait automatiquement
    // comment chercher une Person par son email, car 'email' est un champ de l'entité Person.

    Optional<Person> findByEmail(String email);

//    Optional<Person> findByfirstName(String firstName);

    @Query("SELECT t FROM Teacher t WHERE t.id = :id")
    Optional<Teacher> findTeacherById(Long id);

    // Pour obtenir UNIQUEMENT les enseignants, nous pouvons utiliser une requête JPQL.
    // 'SELECT t FROM Teacher t' est très explicite : "Sélectionne les entités t où t est de type Teacher".
    // Hibernate saura traduire cela en une requête SQL avec "WHERE person_type = 1".
    @Query("SELECT t FROM Teacher t")
    List<Teacher> findAllTeachers();

    // De même pour les étudiants.
    @Query("SELECT s FROM Student s")
    List<Student> findAllStudents();



}
