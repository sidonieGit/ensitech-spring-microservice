package com.project.academic_service.dao.repository;

import com.project.academic_service.domain.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer> {
    boolean existsByLabel(String label);
}
