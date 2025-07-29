package com.project.academic_service.dto;

import com.project.academic_service.enumeration.AcademicYearStatus;

import java.time.LocalDate;

public record AcademicDTO(
         String label,
         LocalDate startDate,
         LocalDate endDate,
         AcademicYearStatus status
) {
}
