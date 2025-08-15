package com.project.ensitech.training_service.service.implementation;

import com.project.ensitech.training_service.model.dto.evaluationDto.CreateEvaluationDto;
import com.project.ensitech.training_service.model.dto.evaluationDto.EvaluationDto;
import com.project.ensitech.training_service.model.dto.specialityDto.SpecialityDto;
import com.project.ensitech.training_service.model.entity.Course;
import com.project.ensitech.training_service.model.entity.Evaluation;
import com.project.ensitech.training_service.repository.CourseRepository;
import com.project.ensitech.training_service.repository.EvaluationRepository;
import com.project.ensitech.training_service.service.common.IEvaluationService;
import com.project.ensitech.training_service.service.mapper.EvaluationMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationServiceImpl implements IEvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final CourseRepository courseRepository;
    private final EvaluationMapper evaluationMapper;


    @Override
    public EvaluationDto createEvaluation(CreateEvaluationDto request) {
        Evaluation evaluation = new Evaluation();

        // Check if course exists
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + request.getCourseId()));

        evaluation.setCode(request.getCode());
        evaluation.setDateEvaluation(request.getDateEvaluation());
        evaluation.setGrade(request.getGrade());
        evaluation.setDescription(request.getDescription());
        evaluation.setType(request.getType());
        evaluation.setStatus(request.getStatus());
        evaluation.setCourse(course);
        evaluation.setStudentId(request.getStudentId());

        Evaluation saved = evaluationRepository.save(evaluation);

        return evaluationMapper.toDto(saved);
    }

    @Override
    public EvaluationDto getEvaluation(Long id) {
        Evaluation e = evaluationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluation not found: " + id));
        return  evaluationMapper.toDto(e);
    }

    @Override
    public List<EvaluationDto> getEvaluationsByStudentId(Long studentId) {
        return evaluationRepository.findByStudentId(studentId).stream()
                .map(evaluationMapper::toDto)
                .toList();
    }

    @Override
    public List<EvaluationDto> getEvaluationsByCourseId(Long courseId) {
        return evaluationRepository.findByCourseId(courseId).stream()
                .map(evaluationMapper::toDto)
                .toList();
    }

    @Override
    public EvaluationDto updateEvaluation(EvaluationDto request) {
        Evaluation existing = evaluationRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Evaluation not found: " + request.getId()));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + request.getCourseId()));

        existing.setCode(request.getCode());
        existing.setDateEvaluation(request.getDateEvaluation());
        existing.setGrade(request.getGrade());
        existing.setDescription(request.getDescription());
        existing.setType(request.getType());
        existing.setStatus(request.getStatus());
        existing.setCourse(course);
        existing.setStudentId(request.getStudentId());

        Evaluation updated = evaluationRepository.save(existing);
        return evaluationMapper.toDto(updated);
    }

    @Override
    public void deleteEvaluation(Long id) {
        if (!evaluationRepository.existsById(id)) {
            throw new EntityNotFoundException("Evaluation not found: " + id);
        }
        evaluationRepository.deleteById(id);
    }

    @Override
    public List<EvaluationDto> getAllEvaluations() {
        return evaluationRepository.findAll()
                .stream()
                .map(evaluationMapper::toDto)
                .toList();
    }

    /*private EvaluationDto mapToDto(Evaluation e) {
        return new EvaluationDto(
                e.getId(),
                e.getCode(),
                e.getDate(),
                e.getGrade(),
                e.getDescription(),
                e.getType(),
                e.getStatus(),
                e.getCourse().getId(),
                e.getStudentId()
        );
    }*/
}

