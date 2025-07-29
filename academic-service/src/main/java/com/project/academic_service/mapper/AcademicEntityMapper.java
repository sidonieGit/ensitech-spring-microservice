package com.project.academic_service.mapper;

import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
public class AcademicEntityMapper implements Function<AcademicDTO, AcademicYear> {

    @Override
    public AcademicYear apply(AcademicDTO academicDTO) {
        AcademicYear academicYear = new AcademicYear();
        academicYear.setLabel(academicDTO.label());
        academicYear.setStartDate(academicDTO.startDate());
        academicYear.setEndDate(academicDTO.endDate());
        academicYear.setStatus(academicDTO.status());
        academicYear.setCreatedAt(LocalDateTime.now());
        academicYear.setUpdatedAt(LocalDateTime.now());

        return academicYear;
    }
}
