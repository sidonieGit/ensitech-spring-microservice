package com.project.ensitech.training_service.service.implementation;

import com.project.ensitech.training_service.exception.ResourceNotFoundException;
import com.project.ensitech.training_service.model.dto.courseDto.CourseDto;
import com.project.ensitech.training_service.model.dto.courseDto.CreateCourseDto;
import com.project.ensitech.training_service.model.entity.Course;
import com.project.ensitech.training_service.repository.CourseRepository;
import com.project.ensitech.training_service.service.common.ICourseService;
import com.project.ensitech.training_service.service.mapper.CourseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {
    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper; // MapStruct mapper



    @Override
    public CourseDto createCourse(CreateCourseDto createCourseDto) {

        Course course = courseMapper.toEntity(createCourseDto);
        Course saved = courseRepository.save(course);
        return courseMapper.toDto(saved);
    }

    @Override
    public CourseDto getCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        return courseMapper.toDto(course);
    }

    @Override
    public CourseDto updateCourse( CourseDto courseDto) {
        Course existing = courseRepository.findById(courseDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course inexistant ", "id", courseDto.getId()));

        // Apply updates
        existing.setTitle(courseDto.getTitle());
        existing.setCoefficient(courseDto.getCoefficient());
        existing.setHours(courseDto.getHours());
        Course updated = courseRepository.save(existing);
        return courseMapper.toDto(updated);
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course", "id", id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    public List<CourseDto> searchByTitle(String title) {
        /*return courseRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(courseMapper::toDto)
                .toList();*/
        log.info("searchByTitle: ",title);
        return courseRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }
}