package com.project.academic_service.mapper;

import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Component
public class AcademicDTOMapper implements Function<AcademicYear, AcademicDTO> {

    @Override
    public AcademicDTO apply(AcademicYear academicYear) {
        return  new AcademicDTO(
                academicYear.getLabel(),
                academicYear.getStartDate(),
                academicYear.getEndDate(),
                academicYear.getStatus());
    }

}
