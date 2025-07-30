package com.project.academic_service.dao.repository;

import com.project.academic_service.domain.Period;
import com.project.academic_service.enumeration.TypePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Integer> {
    boolean existsByTypePeriod(TypePeriod typePeriod);
    boolean existsByStartedAt(LocalDate startedAt);
    boolean existsByEntitled(String entitled);
}
