package com.project.academic_service.service;

import com.project.academic_service.dao.repository.AcademicYearRepository;
import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicDTO;
import com.project.academic_service.exception.AcademicYearAlreadyExistsException;
import com.project.academic_service.mapper.AcademicDTOMapper;
import com.project.academic_service.mapper.AcademicEntityMapper;
import com.project.academic_service.mapper.AcademicYearUpdateMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcademicYearServiceImpl implements IAcademicYearService{
    private final AcademicYearRepository academicYearRepository;
    private final AcademicDTOMapper academicDTOMapper;
    private final AcademicEntityMapper academicEntityMapper;
    private final AcademicYearUpdateMapper academicYearUpdateMapper;

    public AcademicYearServiceImpl(AcademicYearRepository academicYearRepository, AcademicDTOMapper academicDTOMapper, AcademicEntityMapper academicEntityMapper, AcademicYearUpdateMapper academicYearUpdateMapper) {
        this.academicYearRepository = academicYearRepository;
        this.academicDTOMapper = academicDTOMapper;
        this.academicEntityMapper = academicEntityMapper;
        this.academicYearUpdateMapper = academicYearUpdateMapper;
    }

    @Override
    public AcademicDTO get(int id) {
        return this.academicYearRepository.findById(id)
                .map(academicDTOMapper)
                .orElseThrow(()-> new NoSuchElementException("There nothing about"+ id));
    }

    @Override
    public List<AcademicDTO> getYears() {
        return this.academicYearRepository.findAll()
                .stream()
                .map(academicDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public AcademicYear addAcademicYear(AcademicDTO academicDTO) {
        if(this.academicYearRepository.existsByLabel(academicDTO.label()))
            throw new AcademicYearAlreadyExistsException("Already exists!");

        AcademicYear entity = academicEntityMapper.apply(academicDTO);
        return this.academicYearRepository.save(entity);
    }


    @Override
    public AcademicYear create(AcademicYear academicYear) {
        Optional<AcademicYear> existingYear= this.academicYearRepository.findById(academicYear.getId());
        if(existingYear.isPresent()){
            throw new AcademicYearAlreadyExistsException("This academic Already exists!");
        }
        academicYear.setCreatedAt(LocalDateTime.now());
        academicYear.setUpdatedAt(LocalDateTime.now());

        return this.academicYearRepository.save(academicYear);
    }


    @Override
    public AcademicYear update(int id, AcademicYear academicYear) {
        AcademicYear existingYear = academicYearRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Academic year not found with id: " + id));

        existingYear.setLabel(academicYear.getLabel());
        existingYear.setStartDate(academicYear.getStartDate());
        existingYear.setEndDate(academicYear.getEndDate());
        existingYear.setStatus(academicYear.getStatus());
        existingYear.setUpdatedAt(LocalDateTime.now());

        return academicYearRepository.save(existingYear);
    }

    @Override
    public AcademicDTO updateAcademicYear(int id, AcademicDTO academicDTO) {
        AcademicYear existingYear = this.academicYearRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Academic year not found with "+ id));

        AcademicYear updated = academicYearUpdateMapper.apply(academicDTO, existingYear);

        AcademicYear saved = academicYearRepository.save(updated);
        return academicDTOMapper.apply(saved);

    }


    @Override
    public void delete(int id) {
        this.academicYearRepository.deleteById(id);
    }
}
