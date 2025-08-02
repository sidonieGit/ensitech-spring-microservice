package com.project.course_service.service.implementation;

import com.project.course_service.entities.Course;
import com.project.course_service.entities.dto.CourseDto;
import com.project.course_service.exception.ResourceNotFoundException;
import com.project.course_service.feign.UserRestClient;
import com.project.course_service.repositories.CourseRepository;
import com.project.course_service.service.common.ICourseService;
import com.project.course_service.service.mappeur.CourseMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseServiceImpl implements ICourseService {
    private final CourseRepository courseRepository;
    private final UserRestClient userRestClient;
    private final CourseMapper courseMapper;

    // --- Méthodes CRUD de base ---

    @Override
    @Transactional
    public CourseDto createCourse(CourseDto dto) {
        log.info("Création d'un cours avec le titre: {}", dto.getTitle());
        Course course = courseMapper.toEntity(dto);

        // Si un teacherId est fourni à la création, on le valide et on l'assigne.
        if (dto.getTeacherId() != null) {
            validateTeacherExists(dto.getTeacherId());
            course.setTeacherId(dto.getTeacherId());
        }

        Course savedCourse = courseRepository.save(course);
        log.info("Cours créé avec succès. ID: {}", savedCourse.getId());
        // Pas besoin d'enrichir à la création, on retourne les IDs.
        return courseMapper.toDto(savedCourse);
    }

    @Override
    @Transactional
    public CourseDto updateCourse(Long courseId, CourseDto dto) {
        log.info("Mise à jour du cours ID: {}", courseId);
        Course existingCourse = findCourseById(courseId);

        existingCourse.setTitle(dto.getTitle());
        existingCourse.setCoefficient(dto.getCoefficient());
        existingCourse.setHours(dto.getHours());

        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toDto(updatedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto getCourseById(Long id) {
        log.info("Récupération du cours ID: {}", id);
        Course course = findCourseById(id);
        return enrichDto(courseMapper.toDto(course), course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getAllCourses() {
        log.info("Récupération de tous les cours.");
        List<Course> courses = courseRepository.findAll();
        // On enrichit chaque cours de la liste
        return courses.stream()
                .map(course -> {
                    CourseDto dto = courseMapper.toDto(course); // <-- Si courseMapper est null, ça plante ici.
                    return enrichDto(dto, course); // enrichDto utilise userRestClient.
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cours non trouvé avec l'ID : " + id);
        }
        courseRepository.deleteById(id);
        log.info("Cours ID: {} supprimé avec succès.", id);
    }

    // --- Méthodes d'association ---

    @Override
    @Transactional
    public CourseDto assignTeacherToCourse(Long courseId, Long teacherId) {
        log.info("Association de l'enseignant ID {} au cours ID {}", teacherId, courseId);
        validateTeacherExists(teacherId); // Valide que l'enseignant existe dans user-service
        Course course = findCourseById(courseId);
        course.setTeacherId(teacherId);
        Course savedCourse = courseRepository.save(course);
        return enrichDto(courseMapper.toDto(savedCourse), savedCourse);
    }

    @Override
    @Transactional
    public CourseDto enrollStudentInCourse(Long courseId, Long studentId) {
        log.info("Inscription de l'étudiant ID {} au cours ID {}", studentId, courseId);
        // 1. Valider que l'étudiant existe en appelant user-service.
        //    Si l'étudiant n'existe pas, le client Feign lèvera une exception
        //    et la transaction sera annulée (rollback).
        validateStudentExists(studentId); // Valide que l'étudiant existe

        // 2. Récupérer l'entité Course depuis la base de données locale.
        Course course = findCourseById(courseId);

        // 3. Ajouter l'ID de l'étudiant à la collection.
        //    L'utilisation d'un Set gère automatiquement les doublons (un étudiant ne peut pas être inscrit deux fois).
        boolean added = course.getStudentIds().add(studentId);
        if (!added) {
            log.warn("L'étudiant ID {} est déjà inscrit au cours ID {}", studentId, courseId);
            // On ne lève pas d'erreur, l'état final est correct.
        }

        // 4. Sauvegarder l'entité Course mise à jour.
        Course savedCourse = courseRepository.save(course);
        log.info("Étudiant ID {} inscrit avec succès au cours ID {}", studentId, courseId);

        // 5. Enrichir le DTO de retour avec les détails complets et le renvoyer.
        return enrichDto(courseMapper.toDto(savedCourse), savedCourse);
    }

    @Override
    @Transactional
    public CourseDto removeStudentFromCourse(Long courseId, Long studentId) {
        log.info("Désinscription de l'étudiant ID {} du cours ID {}", studentId, courseId);
        // 1. Récupérer l'entité Course.
        Course course = findCourseById(courseId);
        // 2. Retirer l'ID de l'étudiant de la collection.
        boolean removed = course.getStudentIds().remove(studentId);
        if (!removed) {
            log.warn("L'étudiant ID {} n'était pas inscrit au cours ID {}", studentId, courseId);
            // Pas besoin de lever une erreur, l'état final est correct.
        }
        // 3. Sauvegarder les changements.
        Course savedCourse = courseRepository.save(course);
        log.info("Étudiant ID {} désinscrit avec succès du cours ID {}", studentId, courseId);

        // 4. Enrichir et retourner le DTO mis à jour.
        return enrichDto(courseMapper.toDto(savedCourse), savedCourse);
    }

    // --- Méthodes utilitaires privées ---

    private Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID : " + courseId));
    }

    private void validateTeacherExists(Long teacherId) {
        try {
            userRestClient.getTeacherById(teacherId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Validation échouée : Enseignant non trouvé avec l'ID : " + teacherId);
        }
    }

    private void validateStudentExists(Long studentId) {
        try {
            // L'appel à Feign sert de validation. S'il échoue, une exception est levée.
            userRestClient.getStudentById(studentId);
        } catch (Exception e) {
            // On "traduit" l'exception technique de Feign en une exception métier claire.
            throw new ResourceNotFoundException("Validation échouée : Étudiant non trouvé avec l'ID : " + studentId);
        }
}
    private CourseDto enrichDto(CourseDto dto, Course course) {
        // Enrichir avec le teacher via Feign
        if (course.getTeacherId() != null) {
            try {
                dto.setTeacher(userRestClient.getTeacherById(course.getTeacherId()));
            } catch (Exception e) { log.warn("Impossible de charger l'enseignant ID {}", course.getTeacherId()); }
        }

        // Enrichir avec les students via Feign (un seul appel réseau)
        if (course.getStudentIds() != null && !course.getStudentIds().isEmpty()) {
            try {
                dto.setStudents(new HashSet<>(userRestClient.getStudentsByIds(List.copyOf(course.getStudentIds()))));
            } catch (Exception e) { log.warn("Impossible de charger les étudiants pour le cours ID {}", course.getId()); }
        }
        return dto;
    }
}
