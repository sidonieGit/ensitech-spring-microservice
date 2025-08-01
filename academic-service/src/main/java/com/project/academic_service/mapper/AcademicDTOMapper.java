package com.project.academic_service.mapper;

import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicDTO;
import com.project.academic_service.dto.PeriodDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Component
public class AcademicDTOMapper implements Function<AcademicYear, AcademicDTO> {

    private final PeriodDTOMapper periodDTOMapper;

    public AcademicDTOMapper(PeriodDTOMapper periodDTOMapper) {
        this.periodDTOMapper = periodDTOMapper;
    }

    @Override
    public AcademicDTO apply(AcademicYear academicYear) {
        return  new AcademicDTO(
                academicYear.getLabel(),
                academicYear.getStartDate(),
                academicYear.getEndDate(),
                academicYear.getStatus(),
                academicYear.getPeriods()
                        .stream()
                        .map(periodDTOMapper)
                        .toList()
        );
    }

}
