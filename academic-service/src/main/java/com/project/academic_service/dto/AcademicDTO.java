package com.project.academic_service.dto;

import com.project.academic_service.domain.Period;
import com.project.academic_service.enumeration.AcademicYearStatus;

import java.time.LocalDate;
import java.util.List;

public record AcademicDTO(
         String label,
         LocalDate startDate,
         LocalDate endDate,
         AcademicYearStatus status,
         List<PeriodDTO> periods
         ) {
}
